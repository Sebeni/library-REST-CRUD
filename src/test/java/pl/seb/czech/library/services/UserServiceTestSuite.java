package pl.seb.czech.library.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
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
        userService.saveUser(user);
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
    
    @Test
    public void addUserWithStrings() {
        String firstName = "String";
        String lastName = "User";
        
        userService.addUser(firstName, lastName, "11012020");
        
        User user = userService.findUserByName(firstName, lastName);
        Long id = user.getId();

        assertEquals(DataPreparer.getUserList().size() + 1, userService.findAllUsers().size());
        
        userService.deleteUser(id);

        assertEquals(DataPreparer.getUserList().size(), userService.findAllUsers().size());
        
    }
    
    @Test
    public void addAndPayFine() {
        String firstName = "Added";
        String lastName = "User";
        User user = new User(firstName, lastName, LocalDate.now());
        userService.saveUser(user);
        
        user = userService.findUserById(user.getId());
        double accountBefore = user.getFine();
        user = userService.addFine(user, 10);
        assertEquals(accountBefore + 10, user.getFine());
        user = userService.payFine(user, 5);
        assertEquals(accountBefore + 5, user.getFine());
        user = userService.payFine(user, 5);
        assertEquals(accountBefore, user.getFine());

        userService.deleteUser(user.getId());
        
    }
}
