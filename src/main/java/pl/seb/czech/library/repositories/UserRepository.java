package pl.seb.czech.library.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.seb.czech.library.domain.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);
    
    @Override
    List<User> findAll();
}
