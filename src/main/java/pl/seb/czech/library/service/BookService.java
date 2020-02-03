package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.repositories.BookRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;

import java.util.Optional;


@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private TitleInfoRepository titleInfoRepository;

    public Book addNewBook(String title, String authorName, Integer publicationYear) throws DataNotFoundException {
        Optional<TitleInfo> titleInfo =  titleInfoRepository.findByTitleAndAuthorAndPublicationYear(title, authorName, publicationYear);
        if(titleInfo.isPresent()) {
            TitleInfo current = titleInfo.get();
            
            Book bookToAdd = new Book(current, BookStatus.AVAILABLE);
            current.getBookList().add(bookToAdd);
            current = titleInfoRepository.save(current);
            return current.getBookList().get(current.getBookList().size()-1);
        } else {
            throw new DataNotFoundException(String.format("No title was found with these parameters: author - %s, title - %s, publication year - %d", authorName, title, publicationYear));
        }
    }
    
    public Book changeBookStatus(Long id, BookStatus changedStatus) throws DataNotFoundException {
        Optional<Book> bookOptional = bookRepository.findById(id);
        
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setBookStatus(changedStatus);
            bookRepository.save(book);
            return book;
        } else {
            throw new DataNotFoundException("No book was found with id = " + id);
        }
        
    }
    
    public void deleteById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        
        if(bookOptional.isPresent()){
            Book bookToDelete = bookOptional.get();

            TitleInfo titleInfo = titleInfoRepository.findById(bookToDelete.getTitleInfo().getId()).orElseThrow(DataNotFoundException::new);
            titleInfo.getBookList().remove(bookToDelete);

            titleInfoRepository.save(titleInfo);
        } else {
            throw new DataNotFoundException("No book was found with id = " + id);
        }
        
        
    }
    
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No book was found with id = " + id));
    }
    
}
