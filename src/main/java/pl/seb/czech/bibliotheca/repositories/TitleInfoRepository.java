package pl.seb.czech.bibliotheca.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.seb.czech.bibliotheca.domain.TitleInfo;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface TitleInfoRepository extends CrudRepository<TitleInfo, Long> {
}
