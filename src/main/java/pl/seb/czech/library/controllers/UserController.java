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
    
    @RequestMapping(method = RequestMethod.POST, value = "addUser")
    public UserDto addNewUser(@RequestParam String firstName, String lastName, String ddMMyyyy) {
        return libraryMapper.mapToUserDto(userService.addNewUser(firstName, lastName, ddMMyyyy));
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "changeFirstName")
    public UserDto changeFirstName(@RequestParam Long userId, @RequestParam String changedFirstName) {
        return libraryMapper.mapToUserDto(userService.updateUserFirstName(userId, changedFirstName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "changeLastName")
    public UserDto changeLastName(@RequestParam Long userId, @RequestParam String changedLastName) {
        return libraryMapper.mapToUserDto(userService.updateUserLastName(userId, changedLastName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "changeBirthDate")
    public UserDto changeBirthDate(@RequestParam Long userId, @RequestParam LocalDate changedDob) {
        return libraryMapper.mapToUserDto(userService.updateUserBirthDate(userId, changedDob));
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "deleteUser")
    public void deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "findUserById")
    public UserDto findUserById(@RequestParam Long userId) {
        return libraryMapper.mapToUserDto(userService.findUserById(userId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "findUserByName")
    public List<UserDto> findUserByName(@RequestParam String firstName, @RequestParam String lastName) {
        return libraryMapper.mapToUserDtoList(userService.findUserByName(firstName, lastName));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "getAllUsers")
    public List<UserDto> findAllUsers() {
        return libraryMapper.mapToUserDtoList(userService.findAllUsers());
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "addFine")
    public UserDto addFine(@RequestParam Long userId, @RequestParam BigDecimal howMuch) {
        return libraryMapper.mapToUserDto(userService.addFine(userId, howMuch));
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "payFine")
    public UserDto payFine(@RequestParam Long userId, @RequestParam BigDecimal howMuch) {
        return libraryMapper.mapToUserDto(userService.payFine(userId, howMuch));
    }
    
}
