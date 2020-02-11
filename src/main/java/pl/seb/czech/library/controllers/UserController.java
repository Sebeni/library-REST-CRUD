package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.seb.czech.library.domain.dto.UserDto;
import pl.seb.czech.library.mapper.LibraryMapper;
import pl.seb.czech.library.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/library/user")
public class UserController {
    private LibraryMapper libraryMapper;
    private UserService userService;
    
    public static final String addUserURL = "addUser";
    public static final String changeFirstNameURL = "changeFirstName";
    public static final String changeLastNameURL = "changeLastName";
    public static final String changeBirthDateURL = "changeBirthDate";
    public static final String deleteUserURL = "deleteUser";
    public static final String findUserByIdURL = "findUserById";
    public static final String findUserByNameURL = "findUserByName";
    public static final String getAllUsersURL = "getAllUsers";
    public static final String addFineURL = "addFine";
    public static final String payFineURL = "payFine";
    
    @RequestMapping(method = RequestMethod.POST, value = addUserURL)
    public UserDto addNewUser(@RequestParam String firstName, String lastName, String ddMMyyyy) {
        return libraryMapper.mapToUserDto(userService.addNewUser(firstName, lastName, ddMMyyyy));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = deleteUserURL)
    public void deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = changeFirstNameURL)
    public UserDto changeFirstName(@RequestParam Long userId, @RequestParam String changedFirstName) {
        return libraryMapper.mapToUserDto(userService.updateUserFirstName(userId, changedFirstName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = changeLastNameURL)
    public UserDto changeLastName(@RequestParam Long userId, @RequestParam String changedLastName) {
        return libraryMapper.mapToUserDto(userService.updateUserLastName(userId, changedLastName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = changeBirthDateURL)
    public UserDto changeBirthDate(@RequestParam Long userId, @RequestParam String ddMMyyyy) {
        return libraryMapper.mapToUserDto(userService.updateUserBirthDate(userId, ddMMyyyy));
    }

    @RequestMapping(method = RequestMethod.GET, value = findUserByIdURL)
    public UserDto findUserById(@RequestParam Long userId) {
        return libraryMapper.mapToUserDto(userService.findUserById(userId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = findUserByNameURL)
    public List<UserDto> findUserByName(@RequestParam String firstName, @RequestParam String lastName) {
        return libraryMapper.mapToUserDtoList(userService.findUserByName(firstName, lastName));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = getAllUsersURL)
    public List<UserDto> findAllUsers() {
        return libraryMapper.mapToUserDtoList(userService.findAllUsers());
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = addFineURL)
    public UserDto addFine(@RequestParam Long userId, @RequestParam BigDecimal howMuch) {
        return libraryMapper.mapToUserDto(userService.addFine(userId, howMuch));
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = payFineURL)
    public UserDto payFine(@RequestParam Long userId, @RequestParam BigDecimal howMuch) {
        return libraryMapper.mapToUserDto(userService.payFine(userId, howMuch));
    }
    
}
