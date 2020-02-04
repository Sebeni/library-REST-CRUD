package pl.seb.czech.library.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;

import javax.persistence.NamedNativeQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TitleInfoRepository extends CrudRepository<TitleInfo, Long> {
    @Override
    List<TitleInfo> findAll();
    
    List<TitleInfo> findByAuthor(String authorName);

    
    Optional<TitleInfo> findByAuthorAndTitle(String authorName, String title);
    
    Optional<TitleInfo> findByTitleAndAuthorAndPublicationYear(String title, String author, Integer publicationYear);

    @Query(
            value = "SELECT COUNT(B.STATUS) FROM BOOKS B" +
                    " INNER JOIN TITLES_INFO T" +
                    " ON B.TITLE_INFO_ID = T.ID" +
                    " WHERE B.STATUS = 'AVAILABLE'" +
                    " AND T.TITLE = :TITLE" +
                    " GROUP BY(B.STATUS);",
            nativeQuery = true
    )
    long getNumOfBooksAvb(@Param("TITLE")String title);

    @Query(
            value = "SELECT COUNT(*) FROM BOOKS B" +
                    " INNER JOIN TITLES_INFO T" +
                    " ON B.TITLE_INFO_ID = T.ID" + 
                    " WHERE T.TITLE = :TITLE" +
                    " GROUP BY(T.TITLE);",
            nativeQuery = true
    )
    long getNumOfAllBooks(@Param("TITLE") String title);
    

    
}
