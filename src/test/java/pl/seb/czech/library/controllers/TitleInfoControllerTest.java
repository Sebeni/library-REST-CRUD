package pl.seb.czech.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.domain.dto.TitleInfoDto;
import pl.seb.czech.library.initDB.DataPreparer;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TitleInfoControllerTest {
    private final String controllerPath = "/library/title/";

    @Autowired
    DataPreparer dataPreparer;

    @Autowired
    MockMvc mvc;

    @Test
    void getNumOfAvailableBooks() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);
        long avbBooks = titleInfo.getBookList().stream().filter(book -> book.getBookStatus().equals(BookStatus.AVAILABLE))
                .count();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + TitleInfoController.getAvailableBooksURL)
                .queryParam("title", titleInfo.getTitle()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        long bookCountFromResult = Long.parseLong(result.getResponse().getContentAsString());

        assertEquals(avbBooks, bookCountFromResult);
    }

    @Test
    void getNumOfAllBooks() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + TitleInfoController.getAllBooksURL)
                .queryParam("title", titleInfo.getTitle()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        long bookCountFromResult = Long.parseLong(result.getResponse().getContentAsString());

        assertEquals(titleInfo.getBookList().size(), bookCountFromResult);
    }

    @Test
    void findByAuthor() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + TitleInfoController.getAllBooksByAuthorURL)
                .queryParam("authorName", titleInfo.getAuthor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].titleInfoId").value(titleInfo.getId()));

    }

    @Test
    void findByAuthorAndTitle() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + TitleInfoController.getAllBooksByAuthorAndTitleURL)
                .queryParam("authorName", titleInfo.getAuthor())
                .queryParam("title", titleInfo.getTitle()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titleInfoId").value(titleInfo.getId()));
    }

    @Test
    void findByAuthorAndTitleNoDataFound() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + TitleInfoController.getAllBooksByAuthorAndTitleURL)
                .queryParam("authorName", titleInfo.getAuthor() + "test")
                .queryParam("title", titleInfo.getTitle()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void addTitleInfoAlreadyFound() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        TitleInfoDto titleInfoDtoInputAlreadyInDB = new TitleInfoDto(null, titleInfo.getTitle(), titleInfo.getAuthor(), titleInfo.getPublicationYear(), titleInfo.getPrice());
        
        mvc.perform(MockMvcRequestBuilders
        .post(controllerPath + TitleInfoController.addTitleInfo)
                .content(new ObjectMapper().writeValueAsString(titleInfoDtoInputAlreadyInDB))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteByIdWhenRentedBookExist() throws Exception {
        TitleInfo titleInfo = dataPreparer.getRentList().get(0).getBook().getTitleInfo();

        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + TitleInfoController.deleteByIdURL)
                .queryParam("titleInfoId", titleInfo.getId().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteByIdWhenIdNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + TitleInfoController.deleteByIdURL)
                .queryParam("titleInfoId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void addTitleInfoAndDeleteSuccess() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        String title = "TEST";
        
        TitleInfoDto titleInfoDtoInputAlreadyInDB = new TitleInfoDto(null, title, titleInfo.getAuthor(), titleInfo.getPublicationYear(), titleInfo.getPrice());
        
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + TitleInfoController.addTitleInfo)
                .content(new ObjectMapper().writeValueAsString(titleInfoDtoInputAlreadyInDB))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(titleInfo.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title))
                .andReturn();

        String createdTitleInfoId = JsonPath.read(result.getResponse().getContentAsString(), "$.titleInfoId").toString();
        
        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + TitleInfoController.getAllBooksByAuthorAndTitleURL)
                .queryParam("authorName", titleInfo.getAuthor())
                .queryParam("title", title))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + TitleInfoController.deleteByIdURL)
                .queryParam("titleInfoId", createdTitleInfoId))
                .andDo(print())
                .andExpect(status().isOk());
        
    }

    @Test
    void findById() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + TitleInfoController.getByIdURL)
                .queryParam("titleInfoId", titleInfo.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titleInfoId").value(titleInfo.getId()));
        
    }
}