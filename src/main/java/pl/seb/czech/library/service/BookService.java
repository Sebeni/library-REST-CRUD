package pl.seb.czech.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.repositories.BookRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.exceptions.BookException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class BookService {
    private BookRepository bookRepository;
    private TitleInfoRepository titleInfoRepository;
    private TitleInfoService titleInfoService;

    public Book addNewBook(String title, String authorName, Integer publicationYear) {
        Optional<TitleInfo> titleInfo = titleInfoRepository.findByTitleAndAuthorAndPublicationYear(title, authorName, publicationYear);
        if (titleInfo.isPresent()) {
            TitleInfo current = titleInfo.get();
            return addNewBook(current);
        } else {
            throw new DataNotFoundException("book", title, authorName, publicationYear.toString());
        }
    }

    public Book addNewBook(TitleInfo titleInfo) {
        Book bookToAdd = new Book(titleInfo, BookStatus.AVAILABLE);
        return saveBook(bookToAdd);
    }
    
    public Book addNewBook(Long titleInfoId) {
        return addNewBook(titleInfoService.findById(titleInfoId));
    }

    public Book changeBookStatusById(Long bookId, BookStatus changedStatus) throws DataNotFoundException {
        Book book = findById(bookId);
        return changeBookStatusByBook(book, changedStatus);
    }

    public Book changeBookStatusByBook(Book book, BookStatus changedStatus) {
        book.setBookStatus(changedStatus);
        return saveBook(book);
    }
    
    public Book changeBookStatusByIdFromController(Long bookId, BookStatus changedStatus) {
        Book book = findById(bookId);
        if(!book.getBookStatus().equals(BookStatus.RENTED) && !changedStatus.equals(BookStatus.RENTED)) {
            return changeBookStatusById(bookId, changedStatus);
        } else if (book.getBookStatus().equals(BookStatus.RENTED)) {
            throw new BookException("Can't change rented status");
        } else if (changedStatus.equals(BookStatus.RENTED)) {
            throw new BookException("Can't change to rented status");
        } else {
            throw new BookException(changedStatus + "does not exist");
        }
    }

    public void deleteById(Long bookId) {
        Book bookToDelete = findById(bookId);
        if (!bookToDelete.getBookStatus().equals(BookStatus.RENTED)) {
            bookRepository.deleteById(bookId);
        } else {
            throw new BookException("Book is rented so it can't be deleted");
        }
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new DataNotFoundException("book", bookId.toString()));
    }

    public User findWhoRented(Long bookId) {
        Book book = findById(bookId);
        if (book.getBookStatus().equals(BookStatus.RENTED)) {
            return book.getRent().getUser();
        } else {
            throw new DataNotFoundException("user", bookId.toString());
        }
    }
    
    public LocalDate findWhenReturned(Long bookId) {
        Book book = findById(bookId);
        if (book.getBookStatus().equals(BookStatus.RENTED)) {
            return book.getRent().getDueDate();
        } else {
            throw new DataNotFoundException("This book is not rented");
        }
    }
    
    public Book saveBook(Book bookToSave){
        return bookRepository.save(bookToSave);
    }


    @Autowired
    private void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Autowired
    private void setTitleInfoRepository(TitleInfoRepository titleInfoRepository) {
        this.titleInfoRepository = titleInfoRepository;
    }

    @Autowired
    private void setTitleInfoService(TitleInfoService titleInfoService) {
        this.titleInfoService = titleInfoService;
    }
}
