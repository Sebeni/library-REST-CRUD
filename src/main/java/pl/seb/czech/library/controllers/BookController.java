package pl.seb.czech.library.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.dto.BookDto;
import pl.seb.czech.library.domain.dto.TitleInfoDto;
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
    
    
    @RequestMapping(method = RequestMethod.POST, value = "addNewBook", params = {"title", "authorName", "publicationYear"})
    public BookDto addNewBook(@RequestParam String title, @RequestParam String authorName, @RequestParam Integer publicationYear) {
        return libraryMapper.mapToBookDto(bookService.addNewBook(title, authorName, publicationYear));
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "addNewBook", params = "titleInfoId")
    public BookDto addNewBook(@RequestParam Long titleInfoId) {
        return libraryMapper.mapToBookDto(bookService.addNewBook(titleInfoId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "getBook")
    public BookDto getBook(@RequestParam Long bookId) {
        return libraryMapper.mapToBookDto(bookService.findById(bookId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "whoRented")
    public UserDto findWhoRented(@RequestParam Long bookId) {
        return  libraryMapper.mapToUserDto(bookService.findWhoRented(bookId));
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "whenReturned")
    public LocalDate findWhenReturned(@RequestParam Long bookId) {
        return bookService.findWhenReturned(bookId);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "changeStatus")
    public BookDto changeBookStatus(@RequestParam long bookId, @RequestParam BookStatus bookStatus) {
        return libraryMapper.mapToBookDto(bookService.changeBookStatusByIdFromController(bookId, bookStatus));
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "delete")
    public void deleteBookByID(@RequestParam long bookId) {
        bookService.deleteById(bookId);
    }
}
