package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.dto.BookDto;
import pl.seb.czech.library.domain.dto.UserDto;
import pl.seb.czech.library.mapper.LibraryMapper;
import pl.seb.czech.library.service.BookService;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping("/library/book")
public class BookController {
    private LibraryMapper libraryMapper;
    private BookService bookService;

    public final static String ADD_NEW_BOOK_URL = "addNewBook";
    public final static String GET_BOOK_URL = "getBook";
    public final static String WHO_RENTED_URL = "whoRented";
    public final static String WHEN_RETURNED_URL = "whenReturned";
    public final static String CHANGE_STATUS_URL = "changeStatus";
    public final static String DELETE_URL = "delete";
    


    @RequestMapping(method = RequestMethod.POST, value = ADD_NEW_BOOK_URL, params = {"title", "authorName", "publicationYear"})
    public BookDto addNewBook(@RequestParam String title, @RequestParam String authorName, @RequestParam Integer publicationYear) {
        return libraryMapper.mapToBookDto(bookService.addNewBook(title, authorName, publicationYear));
    }

    @RequestMapping(method = RequestMethod.POST, value = ADD_NEW_BOOK_URL, params = "titleInfoId")
    public BookDto addNewBook(@RequestParam Long titleInfoId) {
        return libraryMapper.mapToBookDto(bookService.addNewBook(titleInfoId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = GET_BOOK_URL)
    public BookDto getBook(@RequestParam Long bookId) {
        return libraryMapper.mapToBookDto(bookService.findById(bookId));
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = WHO_RENTED_URL)
    public UserDto findWhoRented(@RequestParam Long bookId) {
        return  libraryMapper.mapToUserDto(bookService.findWhoRented(bookId));
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = WHEN_RETURNED_URL)
    public LocalDate findWhenReturned(@RequestParam Long bookId) {
        return bookService.findWhenReturned(bookId);
    }
    
    
    @RequestMapping(method = RequestMethod.PUT, value = CHANGE_STATUS_URL)
    public BookDto changeBookStatus(@RequestParam Long bookId, @RequestParam BookStatus bookStatus) {
        return libraryMapper.mapToBookDto(bookService.changeBookStatusByIdFromController(bookId, bookStatus));
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, value = DELETE_URL)
    public void deleteBookByID(@RequestParam Long bookId) {
        bookService.deleteById(bookId);
    }
}
