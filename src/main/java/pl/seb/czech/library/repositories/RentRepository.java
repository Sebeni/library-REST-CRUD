package pl.seb.czech.library.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.seb.czech.library.domain.Rent;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface RentRepository extends CrudRepository<Rent, Long> {
}
