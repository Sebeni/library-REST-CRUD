package pl.seb.czech.library.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.seb.czech.library.domain.Book;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    
    Optional<Book> findById(Long id);
    
    
}
