package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.repositories.BookRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;

import java.time.LocalDate;
import java.util.Optional;


@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private TitleInfoRepository titleInfoRepository;

    public Book addNewBook(String title, String authorName, Integer publicationYear) throws DataNotFoundException {
        Optional<TitleInfo> titleInfo = titleInfoRepository.findByTitleAndAuthorAndPublicationYear(title, authorName, publicationYear);
        if (titleInfo.isPresent()) {
            TitleInfo current = titleInfo.get();
            return addNewBook(current);
        } else {
            throw new DataNotFoundException(String.format("No title was found with these parameters: author - %s, title - %s, publication year - %d", authorName, title, publicationYear));
        }
    }

    public Book addNewBook(TitleInfo titleInfo) {
        Book bookToAdd = new Book(titleInfo, BookStatus.AVAILABLE);
//        titleInfo.getBookList().add(bookToAdd);
//        titleInfoRepository.save(titleInfo);
        return bookRepository.save(bookToAdd);
        
    }

    public Book changeBookStatusById(Long id, BookStatus changedStatus) throws DataNotFoundException {
        Book book = findById(id);
        return changeBookStatusByBook(book, changedStatus);
    }

    public Book changeBookStatusByBook(Book book, BookStatus changedStatus) {
        book.setBookStatus(changedStatus);
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        Book bookToDelete = findById(id);
        if (bookToDelete.getBookStatus() != BookStatus.RENTED) {
            TitleInfo titleInfo = titleInfoRepository.findById(bookToDelete.getTitleInfo().getId()).orElseThrow(DataNotFoundException::new);
            titleInfo.getBookList().remove(bookToDelete);
            titleInfoRepository.save(titleInfo);
        } else {
            throw new IllegalArgumentException("Book is rented so it can't be deleted");
        }
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No book was found with id = " + id));
    }

    public User findWhoRented(Long bookId) {
        Book book = findById(bookId);
        if (book.getBookStatus().equals(BookStatus.RENTED)) {
            return book.getRent().getUser();
        } else {
            throw new DataNotFoundException("This book is not rented");
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
    
    public void saveBook(Book bookToSave){
        bookRepository.save(bookToSave);
    }

}
