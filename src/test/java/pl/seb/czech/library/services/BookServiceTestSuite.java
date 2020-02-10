package pl.seb.czech.library.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.repositories.BookRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.BookService;
import pl.seb.czech.library.service.exceptions.BookException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
import pl.seb.czech.library.visualTesting.DataPreparer;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class BookServiceTestSuite {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    TitleInfoRepository titleInfoRepository;
    @Autowired
    DataPreparer dataPreparer;



    @Test
    public void addChangeDeleteTest() {
        String title = "Brave New World";
        String author = "Aldous Huxley";
        Integer publicationYear = 1932;
        Integer wrongPublicationYear = 2001;
        
        long bookInputNumCount = dataPreparer.getBookList().stream()
                .filter(book -> book.getTitleInfo().getTitle().equals(title) && book.getTitleInfo().getAuthor().equals(author))
                .count();
        
        
        
        Book bookToAdd = bookService.addNewBook(title, author, publicationYear);
        long idBookToAdd = bookToAdd.getId();

        assertAll(
                () -> assertEquals(bookInputNumCount + 1, titleInfoRepository.getNumOfAllBooks(title)),
                () -> assertEquals(BookStatus.AVAILABLE, bookService.findById(idBookToAdd).getBookStatus()),
                () -> assertThrows(DataNotFoundException.class, () -> bookService.addNewBook(title, author, wrongPublicationYear))
        );
        
        Book bookAfterChange = bookService.changeBookStatusById(idBookToAdd, BookStatus.RENTED);
        
        assertAll(
                () ->   assertEquals(BookStatus.RENTED, bookService.findById(idBookToAdd).getBookStatus()),
                () ->   assertEquals(idBookToAdd, bookAfterChange.getId()),
                () ->   assertThrows(BookException.class, () -> bookService.deleteById(idBookToAdd))
        );
        
        bookService.changeBookStatusById(idBookToAdd, BookStatus.AVAILABLE);
        bookService.deleteById(idBookToAdd);
        
        assertEquals(bookInputNumCount, titleInfoRepository.getNumOfAllBooks(title));
    }
    
    @Test
    public void findWhoAndForHowLongRentedTestSuite() {
        User userWhoRented = dataPreparer.getUserList().get(0);
        Book rentedBook = dataPreparer.getBookList().stream()
                .filter(book -> book.getBookStatus().equals(BookStatus.RENTED))
                .findAny().get();
        Long bookId = rentedBook.getId();
        
        assertAll(
                () -> assertEquals(userWhoRented, bookService.findWhoRented(bookId)),
                () -> assertEquals(rentedBook.getRent().getDueDate(), bookService.findWhenReturned(bookId))
        );
    }
}
