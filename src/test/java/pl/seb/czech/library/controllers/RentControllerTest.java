package pl.seb.czech.library.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.seb.czech.library.domain.*;
import pl.seb.czech.library.initDB.DataPreparer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RentControllerTest {

    private String controllerPath = "/library/rent/";

    @Autowired
    DataPreparer dataPreparer;

    @Autowired
    MockMvc mvc;

    @Test
    void findRentById() throws Exception {
        Rent rent = dataPreparer.getRentList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + RentController.findByIdURL)
                .queryParam("rentId", rent.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentId").value(rent.getId().toString()));

    }

    @Test
    void findRentByIdNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + RentController.findByIdURL)
                .queryParam("rentId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void rentProlongAndReturnBook() throws Exception {
        User user = dataPreparer.getUserList().get(0);
        Book avbBook = dataPreparer.getBookList().stream().filter(book -> book.getBookStatus().equals(BookStatus.AVAILABLE)).findFirst().get();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + RentController.rentBookURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("bookId", avbBook.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookDto.bookId").value(avbBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userDto.userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dueDateProlonged").value("false"))
                .andReturn();

        String createdRentId = JsonPath.read(result.getResponse().getContentAsString(), "$.rentId").toString();
        String dueDate = JsonPath.read(result.getResponse().getContentAsString(), "$.dueDate").toString();
        LocalDate dueDateLD = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + RentController.findByIdURL)
                .queryParam("rentId", createdRentId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentId").value(createdRentId));


        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + RentController.prolongURL)
                .queryParam("rentId", createdRentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(dueDateLD.plusWeeks(2).toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dueDateProlonged").value("true"));


        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + RentController.prolongURL)
                .queryParam("rentId", createdRentId))
                .andDo(print())
                .andExpect(status().isBadRequest());


        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + RentController.returnBookURL)
                .queryParam("rentId", createdRentId))
                .andDo(print())
                .andExpect(status().isOk());


        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + RentController.findByIdURL)
                .queryParam("rentId", createdRentId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void countRentedBooksByUser() throws Exception {
        long userId = dataPreparer.getUserList().get(0).getId();
        long countRentedBooksWithUserId = dataPreparer.getRentList().stream()
                .filter(rent -> rent.getUser().getId().equals(userId))
                .count();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + RentController.countUserBooksURL)
                .queryParam("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        long bookCountFromResult = Long.parseLong(result.getResponse().getContentAsString());

        assertEquals(countRentedBooksWithUserId, bookCountFromResult);
    }


    @Test
    void reportLostDestroyed() throws Exception {
        Rent rent = dataPreparer.getRentList().get(0);
        String rentId = rent.getId().toString();
        Book lostBook = rent.getBook();
        String userId = rent.getUser().getId().toString();

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + RentController.findByIdURL)
                .queryParam("rentId", rentId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentId").value(rentId));

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + RentController.reportLostURL)
                .queryParam("rentId", rentId))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + RentController.findByIdURL)
                .queryParam("rentId", rentId))
                .andDo(print())
                .andExpect(status().isNotFound());

//must pay fine
        mvc.perform(MockMvcRequestBuilders
                .put("/library/user/" + UserController.payFineURL)
                .queryParam("userId", userId)
                .queryParam("howMuch", lostBook.getTitleInfo().getPrice().add(Fine.LOST_OR_DESTROYED).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fine").value("0.0"));
        
    }
}