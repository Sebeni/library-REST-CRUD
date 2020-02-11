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
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.initDB.DataPreparer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerTest {
    private final String controllerPath = "/library/user/";

    @Autowired
    DataPreparer dataPreparer;

    @Autowired
    MockMvc mvc;

    @Test
    void addAndDeleteNewUser() throws Exception {
        String name = "TESTname";
        String lastName = "TESTlastName";
        String ddMMyyyy = "01012010";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate dob = LocalDate.parse(ddMMyyyy, formatter);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + UserController.addUserURL)
                .queryParam("firstName", name)
                .queryParam("lastName", lastName)
                .queryParam("ddMMyyyy", ddMMyyyy))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(dob.toString()))
                .andReturn();

        String createdUserId = JsonPath.read(result.getResponse().getContentAsString(), "$.userId").toString();

        mvc.perform(MockMvcRequestBuilders
                .post(controllerPath + UserController.addUserURL)
                .queryParam("firstName", name)
                .queryParam("lastName", lastName)
                .queryParam("ddMMyyyy", ddMMyyyy))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + UserController.deleteUserURL)
                .queryParam("userId", createdUserId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserWhoHasRentedBooks() throws Exception {
        User user = dataPreparer.getRentList().get(0).getUser();

        mvc.perform(MockMvcRequestBuilders
                .delete(controllerPath + UserController.deleteUserURL)
                .queryParam("userId", user.getId().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeFirstName() throws Exception {
        User user = dataPreparer.getUserList().get(0);
        String nameBeforeChange = user.getFirstName();
        String changedName = "TESTname";

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeFirstNameURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("changedFirstName", changedName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(changedName));

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeFirstNameURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("changedFirstName", nameBeforeChange))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(nameBeforeChange));

    }

    @Test
    void changeLastName() throws Exception {
        User user = dataPreparer.getUserList().get(0);
        String lastNameBeforeChange = user.getLastName();
        String changedLastName = "TESTname";

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeLastNameURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("changedLastName", changedLastName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(changedLastName));

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeLastNameURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("changedLastName", lastNameBeforeChange))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastNameBeforeChange));
    }

    @Test
    void changeBirthDate() throws Exception {
        User user = dataPreparer.getUserList().get(0);
        String dobBeforeChange = user.getBirthDate().format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        LocalDate changedDobDate = LocalDate.now();
        String changedDob = changedDobDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeBirthDateURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("ddMMyyyy", changedDob))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(changedDobDate.toString()));

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeBirthDateURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("ddMMyyyy", dobBeforeChange))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(user.getBirthDate().toString()));
    }

    @Test
    void changeFirstLastNameDobWhereIdIsNotFound() throws Exception {
        String changedName = "TESTname";
        String changedLastName = "TESTname";
        LocalDate changedDobDate = LocalDate.now();
        String changedDob = changedDobDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeFirstNameURL)
                .queryParam("userId", String.valueOf(-1))
                .queryParam("changedFirstName", changedName))
                .andDo(print())
                .andExpect(status().isNotFound());

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeLastNameURL)
                .queryParam("userId", String.valueOf(-1))
                .queryParam("changedLastName", changedLastName))
                .andDo(print())
                .andExpect(status().isNotFound());

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.changeBirthDateURL)
                .queryParam("userId", String.valueOf(-1))
                .queryParam("ddMMyyyy", changedDob))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void findUserById() throws Exception {
        User user = dataPreparer.getUserList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + UserController.findUserByIdURL)
                .queryParam("userId", user.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdOn").value(user.getCreatedOn().toString()));

    }

    @Test
    void findUserByIdNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + UserController.findUserByIdURL)
                .queryParam("userId", String.valueOf(-1)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserByName() throws Exception {
        User user = dataPreparer.getUserList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + UserController.findUserByNameURL)
                .queryParam("firstName", user.getFirstName())
                .queryParam("lastName", user.getLastName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value(user.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value(user.getLastName()));
    }

    @Test
    void findAllUsers() throws Exception {
        User user = dataPreparer.getUserList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .get(controllerPath + UserController.getAllUsersURL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value(user.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value(user.getLastName()));
    }

    @Test
    void addFineAndPayFine() throws Exception {
        User user = dataPreparer.getUserList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.addFineURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("howMuch", String.valueOf(10.99)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fine").value(user.getFine().add(BigDecimal.valueOf(10.99)).toString()));

        
        
        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.payFineURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("howMuch", String.valueOf(10.99)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fine").value("0.0"));


    }

    @Test
    void payGreaterFineThanThereIs() throws Exception {
        User user = dataPreparer.getUserList().get(0);

        mvc.perform(MockMvcRequestBuilders
                .put(controllerPath + UserController.payFineURL)
                .queryParam("userId", user.getId().toString())
                .queryParam("howMuch", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        
    }
}