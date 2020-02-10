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

    public final static String addNewBookURL = "addNewBook";
    public final static String getBookURL = "getBook";
    public final static String whoRentedURL = "whoRented";
    public final static String whenReturnedURL = "whenReturned";
    public final static String changeStatusURL = "changeStatus";
    public final static String deleteURL = "delete";
    


    @RequestMapping(method = RequestMethod.POST, value = addNewBookURL, params = {"title", "authorName", "publicationYear"})
    public BookDto addNewBook(@RequestParam String title, @RequestParam String authorName, @RequestParam Integer publicationYear) {
        return libraryMapper.mapToBookDto(bookService.addNewBook(title, authorName, publicationYear));
    }

    @RequestMapping(method = RequestMethod.POST, value = addNewBookURL, params = "titleInfoId")
    public BookDto addNewBook(@RequestParam Long titleInfoId) {
        return libraryMapper.mapToBookDto(bookService.addNewBook(titleInfoId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = getBookURL)
    public BookDto getBook(@RequestParam Long bookId) {
        return libraryMapper.mapToBookDto(bookService.findById(bookId));
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = whoRentedURL)
    public UserDto findWhoRented(@RequestParam Long bookId) {
        return  libraryMapper.mapToUserDto(bookService.findWhoRented(bookId));
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = whenReturnedURL)
    public LocalDate findWhenReturned(@RequestParam Long bookId) {
        return bookService.findWhenReturned(bookId);
    }
    
    
    @RequestMapping(method = RequestMethod.PUT, value = changeStatusURL)
    public BookDto changeBookStatus(@RequestParam Long bookId, @RequestParam BookStatus bookStatus) {
        return libraryMapper.mapToBookDto(bookService.changeBookStatusByIdFromController(bookId, bookStatus));
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, value = deleteURL)
    public void deleteBookByID(@RequestParam Long bookId) {
        bookService.deleteById(bookId);
    }
}
