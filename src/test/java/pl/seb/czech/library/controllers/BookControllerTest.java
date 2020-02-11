package pl.seb.czech.library.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.Rent;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.initDB.DataPreparer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookControllerTest {
    private String controllerPath = "/library/book/";

    @Autowired
    DataPreparer dataPreparer;

    @Autowired
    MockMvc mvc;


    @Test
    void addNewBookWithParametersTest() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + BookController.addNewBookURL)
                .queryParam("title", titleInfo.getTitle())
                .queryParam("authorName", titleInfo.getAuthor())
                .queryParam("publicationYear", titleInfo.getPublicationYear().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titleInfoDto.titleInfoId").value(titleInfo.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titleInfoDto.price").value(titleInfo.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookStatus").value(BookStatus.AVAILABLE.toString()));
    }

    @Test
    void addNewBookWithParametersNotFoundParameterTest() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + BookController.addNewBookURL)
                .queryParam("title", titleInfo.getTitle() + "test")
                .queryParam("authorName", titleInfo.getAuthor())
                .queryParam("publicationYear", titleInfo.getPublicationYear().toString()))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void addNewBookWithTitleInfoIdTest() throws Exception {
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + BookController.addNewBookURL)
                .queryParam("titleInfoId", titleInfo.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titleInfoDto.titleInfoId").value(titleInfo.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titleInfoDto.price").value(titleInfo.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookStatus").value(BookStatus.AVAILABLE.toString()));


    }

    @Test
    void addNewBookWithTitleInfoIdNotFoundTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + BookController.addNewBookURL)
                .queryParam("titleInfoId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBook() throws Exception {
        Book book = dataPreparer.getBookList().get(0);
        Long bookId = book.getId();

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + BookController.getBookURL)
                .queryParam("bookId", bookId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(bookId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titleInfoDto.titleInfoId").value(book.getTitleInfo().getId()));
    }

    @Test
    void testGetBookIdNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + BookController.getBookURL)
                .queryParam("bookId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findWhoRented() throws Exception {
        Rent rent = dataPreparer.getRentList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + "whoRented")
                .queryParam("bookId", rent.getBook().getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(rent.getUser().getId()));

    }

    @Test
    void findWhoRentedWhenBookIsNotRented() throws Exception {
        Book bookNotRented = dataPreparer.getBookList().stream().filter(book1 -> !book1.getBookStatus().equals(BookStatus.RENTED)).findAny().get();

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + "whoRented")
                .queryParam("bookId", bookNotRented.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findWhenReturned() throws Exception {
        Rent rent = dataPreparer.getRentList().get(0);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + BookController.whenReturnedURL)
                .queryParam("bookId", rent.getBook().getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromResult = LocalDate.parse(result.getResponse().getContentAsString().replaceAll("\"", ""), formatter);

        assertEquals(rent.getDueDate(), fromResult);
    }

    @Test
    void findWhenReturnedWhenBookIsNotRented() throws Exception {
        Book bookNotRented = dataPreparer.getBookList().stream().filter(book1 -> !book1.getBookStatus().equals(BookStatus.RENTED)).findAny().get();

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + BookController.whenReturnedURL)
                .queryParam("bookId", bookNotRented.getId().toString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void changeBookStatusFromOrToRented() throws Exception {
        Book rented = dataPreparer.getBookList().stream().filter(book1 -> book1.getBookStatus().equals(BookStatus.RENTED)).findAny().get();
        Book available = dataPreparer.getBookList().stream().filter(book1 -> book1.getBookStatus().equals(BookStatus.AVAILABLE)).findAny().get();

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + BookController.changeStatusURL)
                .queryParam("bookId", rented.getId().toString())
                .queryParam("bookStatus", BookStatus.RENTED.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + BookController.changeStatusURL)
                .queryParam("bookId", available.getId().toString())
                .queryParam("bookStatus", BookStatus.RENTED.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void changeBookStatusOK() throws Exception {
        Book available = dataPreparer.getBookList().stream().filter(book1 -> book1.getBookStatus().equals(BookStatus.AVAILABLE)).findAny().get();
       
        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + BookController.changeStatusURL)
                .queryParam("bookId", available.getId().toString())
                .queryParam("bookStatus", BookStatus.LOST_OR_DESTROYED.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(available.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookStatus").value(BookStatus.LOST_OR_DESTROYED.toString()));

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + BookController.changeStatusURL)
                .queryParam("bookId", available.getId().toString())
                .queryParam("bookStatus", BookStatus.AVAILABLE.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(available.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookStatus").value(BookStatus.AVAILABLE.toString()));
    }
    
    @Test
    void changeBookStatusIdNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + BookController.changeStatusURL)
                .queryParam("bookId", String.valueOf(-1))
                .queryParam("bookStatus", BookStatus.AVAILABLE.toString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAvailableBook() throws Exception {
        Book available = dataPreparer.getBookList().stream().filter(book1 -> book1.getBookStatus().equals(BookStatus.AVAILABLE)).findAny().get();
        
        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + BookController.deleteURL)
                .queryParam("bookId", available.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteRentedBook() throws Exception {
        Book rented = dataPreparer.getBookList().stream().filter(book1 -> book1.getBookStatus().equals(BookStatus.RENTED)).findAny().get();

        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + BookController.deleteURL)
                .queryParam("bookId", rented.getId().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deleteBookIdNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + BookController.deleteURL)
                .queryParam("bookId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}