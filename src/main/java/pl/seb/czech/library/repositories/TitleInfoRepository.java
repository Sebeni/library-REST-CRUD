package pl.seb.czech.library.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.seb.czech.library.domain.TitleInfo;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface TitleInfoRepository extends CrudRepository<TitleInfo, Long> {
}
