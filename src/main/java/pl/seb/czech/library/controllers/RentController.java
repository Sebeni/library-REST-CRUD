package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.seb.czech.library.domain.dto.RentDto;
import pl.seb.czech.library.mapper.LibraryMapper;
import pl.seb.czech.library.service.RentService;

@AllArgsConstructor
@RestController
@RequestMapping("/library/rent")
public class RentController {
    private LibraryMapper libraryMapper;
    private RentService rentService;
    
    public static final String findByIdURL = "findById";
    public static final String rentBookURL = "rentBook";
    public static final String countUserBooksURL = "countUserBooks";
    public static final String returnBookURL = "returnBook";
    public static final String reportLostURL = "reportLost";
    public static final String prolongURL = "prolong";

    @RequestMapping(method = RequestMethod.GET, value = findByIdURL)
    public RentDto findRentById(@RequestParam Long rentId) {
        return libraryMapper.mapToRentDto(rentService.findRentById(rentId));
    }

    @RequestMapping(method = RequestMethod.POST, value = rentBookURL)
    public RentDto rentBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return libraryMapper.mapToRentDto(rentService.rentBook(userId, bookId));
    }

    @RequestMapping(method = RequestMethod.GET, value = countUserBooksURL)
    public Long countRentedBooksByUser(@RequestParam Long userId) {
        return rentService.countRentedBooksByUser(userId);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = returnBookURL)
    public void returnBook(@RequestParam Long rentId) {
        rentService.returnBook(rentId);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = reportLostURL)
    public void reportLostDestroyed(@RequestParam Long rentId) {
        rentService.reportLostDestroyed(rentId);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = prolongURL)
    public RentDto prolongRent(@RequestParam Long rentId) {
        return libraryMapper.mapToRentDto(rentService.prolong(rentId));
    }

}
