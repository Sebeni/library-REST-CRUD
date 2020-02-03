package pl.seb.czech.library.services;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.service.DataNotFoundException;
import pl.seb.czech.library.service.UserService;
import pl.seb.czech.library.visualTesting.DataPreparer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTestSuite {
    @Autowired
    UserService userService;
    @Autowired
    DataPreparer dataPreparer;


    @BeforeEach
    public void populateData() {
        dataPreparer.prepareData();
    }

    @AfterEach
    public void cleanUp() {
        dataPreparer.cleanUp();
    }
    
    @Test
    public void addFindDeleteUserTest() {
        String firstName = "Added";
        String lastName = "User";
        User user = new User(firstName, lastName, LocalDate.now());
        userService.addUser(user);
        Long id = user.getId();
        
        assertAll(
                () -> assertEquals(DataPreparer.getUserList().size() + 1, userService.findAllUsers().size()),
                () -> assertEquals(user, userService.findUserById(id)),
                () -> assertEquals(user, userService.findUserByName(firstName, lastName)));
        
        userService.deleteUser(id);
        
        assertAll(
                () -> assertEquals(DataPreparer.getUserList().size(), userService.findAllUsers().size()),
                () -> assertThrows(DataNotFoundException.class, () -> userService.findUserById(id))
        );
        
        
    }


}
