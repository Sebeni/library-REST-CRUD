package pl.seb.czech.library.services;

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

    @Test
    public void addFindDeleteUserTest() {
        String firstName = "Added";
        String lastName = "User";
        User user = new User(firstName, lastName, LocalDate.now());
        userService.saveUser(user);
        Long id = user.getId();
        
        assertAll(
                () -> assertEquals(dataPreparer.getUserList().size() + 1, userService.findAllUsers().size()),
                () -> assertEquals(user, userService.findUserById(id)),
                () -> assertEquals(user, userService.findUserByName(firstName, lastName).get(0)));
        
        userService.deleteUser(id);
        
        assertAll(
                () -> assertEquals(dataPreparer.getUserList().size(), userService.findAllUsers().size()),
                () -> assertThrows(DataNotFoundException.class, () -> userService.findUserById(id))
        );
    }
    
    @Test
    public void addUserWithStringsTest() {
        String firstName = "String";
        String lastName = "User";
        
        userService.addNewUser(firstName, lastName, "11012020");
        
        User user = userService.findUserByName(firstName, lastName).get(0);
        Long id = user.getId();

        assertEquals(dataPreparer.getUserList().size() + 1, userService.findAllUsers().size());
        
        userService.deleteUser(id);

        assertEquals(dataPreparer.getUserList().size(), userService.findAllUsers().size());
        
    }
    
    @Test
    public void addAndPayFineTest() {
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
    
    @Test
    public void updateUserTest() {
        User userToChange = userService.findUserById(dataPreparer.getUserList().get(0).getId());
        Long id = userToChange.getId();
        String oldName = userToChange.getFirstName();
        String oldLastName = userToChange.getLastName();
        LocalDate oldDoB = userToChange.getBirthDate();
        
        
        String test = "TEST";
        String newName = oldName + test;
        String newLastName = oldLastName + test;
        LocalDate newDoB = oldDoB.plusDays(1);
        
        userService.updateUserFirstName(id, newName);
        userService.updateUserLastName(id, newLastName);
        userService.updateUserBirthDate(id, newDoB);
        
        assertAll(
                () -> assertEquals(newName, userService.findUserById(id).getFirstName()),
                () -> assertEquals(newLastName, userService.findUserById(id).getLastName()),
                () -> assertEquals(newDoB, userService.findUserById(id).getBirthDate())
        );

        userService.updateUserFirstName(id, oldName);
        userService.updateUserLastName(id, oldLastName);
        User userAfterChange = userService.updateUserBirthDate(id, oldDoB);
        
        assertEquals(userService.findUserById(id), userAfterChange);
    }
}
