package pl.seb.czech.library.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
import pl.seb.czech.library.service.UserService;
import pl.seb.czech.library.initDB.DataPreparer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceTestSuite {
    @Autowired
    UserService userService;

    @Autowired
    DataPreparer dataPreparer;


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
        BigDecimal accountBefore = user.getFine();
        user = userService.addFine(user, BigDecimal.valueOf(10));
        assertEquals(accountBefore.add(BigDecimal.valueOf(10)), user.getFine());
        user = userService.payFine(user, BigDecimal.valueOf(5));
        assertEquals(accountBefore.add(BigDecimal.valueOf(5)), user.getFine());
        user = userService.payFine(user, BigDecimal.valueOf(5));
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
        userService.updateUserBirthDate(id, newDoB.format(DateTimeFormatter.ofPattern("ddMMyyyy")));
        
        assertAll(
                () -> assertEquals(newName, userService.findUserById(id).getFirstName()),
                () -> assertEquals(newLastName, userService.findUserById(id).getLastName()),
                () -> assertEquals(newDoB, userService.findUserById(id).getBirthDate())
        );

        userService.updateUserFirstName(id, oldName);
        userService.updateUserLastName(id, oldLastName);
        User userAfterChange = userService.updateUserBirthDate(id, oldDoB.format(DateTimeFormatter.ofPattern("ddMMyyyy")));
        
        assertEquals(userService.findUserById(id), userAfterChange);
    }
}
