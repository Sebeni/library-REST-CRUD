package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
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
    
    public static final String FIND_BY_ID_URL = "findById";
    public static final String RENT_BOOK_URL = "rentBook";
    public static final String COUNT_USER_BOOKS_URL = "countUserBooks";
    public static final String RETURN_BOOK_URL = "returnBook";
    public static final String REPORT_LOST_URL = "reportLost";
    public static final String PROLONG_URL = "prolong";

    @RequestMapping(method = RequestMethod.GET, value = FIND_BY_ID_URL)
    public RentDto findRentById(@RequestParam Long rentId) {
        return libraryMapper.mapToRentDto(rentService.findRentById(rentId));
    }

    @RequestMapping(method = RequestMethod.POST, value = RENT_BOOK_URL)
    public RentDto rentBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return libraryMapper.mapToRentDto(rentService.rentBook(userId, bookId));
    }

    @RequestMapping(method = RequestMethod.GET, value = COUNT_USER_BOOKS_URL)
    public Long countRentedBooksByUser(@RequestParam Long userId) {
        return rentService.countRentedBooksByUser(userId);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = RETURN_BOOK_URL)
    public void returnBook(@RequestParam Long rentId) {
        rentService.returnBook(rentId);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = REPORT_LOST_URL)
    public void reportLostDestroyed(@RequestParam Long rentId) {
        rentService.reportLostDestroyed(rentId);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = PROLONG_URL)
    public RentDto prolongRent(@RequestParam Long rentId) {
        return libraryMapper.mapToRentDto(rentService.prolong(rentId));
    }

}
