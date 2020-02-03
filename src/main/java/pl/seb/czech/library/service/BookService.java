package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.repositories.BookRepository;


@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;


    
}
