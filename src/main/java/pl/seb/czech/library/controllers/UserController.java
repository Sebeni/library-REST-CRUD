package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.seb.czech.library.domain.dto.UserDto;
import pl.seb.czech.library.mapper.LibraryMapper;
import pl.seb.czech.library.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/library/user")
public class UserController {
    private LibraryMapper libraryMapper;
    private UserService userService;
    
    public static final String ADD_USER_URL = "addUser";
    public static final String CHANGE_FIRST_NAME_URL = "changeFirstName";
    public static final String CHANGE_LAST_NAME_URL = "changeLastName";
    public static final String CHANGE_BIRTH_DATE_URL = "changeBirthDate";
    public static final String DELETE_USER_URL = "deleteUser";
    public static final String FIND_USER_BY_ID_URL = "findUserById";
    public static final String FIND_USER_BY_NAME_URL = "findUserByName";
    public static final String GET_ALL_USERS_URL = "getAllUsers";
    public static final String ADD_FINE_URL = "addFine";
    public static final String PAY_FINE_URL = "payFine";
    
    @RequestMapping(method = RequestMethod.POST, value = ADD_USER_URL)
    public UserDto addNewUser(@RequestParam String firstName, String lastName, String ddMMyyyy) {
        return libraryMapper.mapToUserDto(userService.addNewUser(firstName, lastName, ddMMyyyy));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = DELETE_USER_URL)
    public void deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = CHANGE_FIRST_NAME_URL)
    public UserDto changeFirstName(@RequestParam Long userId, @RequestParam String changedFirstName) {
        return libraryMapper.mapToUserDto(userService.updateUserFirstName(userId, changedFirstName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = CHANGE_LAST_NAME_URL)
    public UserDto changeLastName(@RequestParam Long userId, @RequestParam String changedLastName) {
        return libraryMapper.mapToUserDto(userService.updateUserLastName(userId, changedLastName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = CHANGE_BIRTH_DATE_URL)
    public UserDto changeBirthDate(@RequestParam Long userId, @RequestParam String ddMMyyyy) {
        return libraryMapper.mapToUserDto(userService.updateUserBirthDate(userId, ddMMyyyy));
    }

    @RequestMapping(method = RequestMethod.GET, value = FIND_USER_BY_ID_URL)
    public UserDto findUserById(@RequestParam Long userId) {
        return libraryMapper.mapToUserDto(userService.findUserById(userId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = FIND_USER_BY_NAME_URL)
    public List<UserDto> findUserByName(@RequestParam String firstName, @RequestParam String lastName) {
        return libraryMapper.mapToUserDtoList(userService.findUserByName(firstName, lastName));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = GET_ALL_USERS_URL)
    public List<UserDto> findAllUsers() {
        return libraryMapper.mapToUserDtoList(userService.findAllUsers());
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = ADD_FINE_URL)
    public UserDto addFine(@RequestParam Long userId, @RequestParam BigDecimal howMuch) {
        return libraryMapper.mapToUserDto(userService.addFine(userId, howMuch));
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = PAY_FINE_URL)
    public UserDto payFine(@RequestParam Long userId, @RequestParam BigDecimal howMuch) {
        return libraryMapper.mapToUserDto(userService.payFine(userId, howMuch));
    }
    
}
