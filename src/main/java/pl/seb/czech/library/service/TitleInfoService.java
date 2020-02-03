package pl.seb.czech.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.repositories.TitleInfoRepository;

import java.util.List;

@Service
public class TitleInfoService {
    @Autowired
    TitleInfoRepository titleInfoRepository;
    
    public long getNumOfAvailableBooks(String title) {
        return titleInfoRepository.getNumOfBooksAvb(title.trim());
    }
    
    public List<TitleInfo> findByAuthor(String authorsName) {
        return titleInfoRepository.findByAuthor(authorsName.trim());
    }
    
    public TitleInfo findByAuthorAndTitle(String authorName, String title) throws DataNotFoundException {
        return titleInfoRepository.findByAuthorAndTitle(authorName.trim(), title.trim()).orElseThrow(() -> new DataNotFoundException(String.format(
                "No title was found with these parameters: author's name %s, title %s", authorName, title)));
    }
    
    public TitleInfo addTitleInfo(String title, String authorName, Integer publicationYear, double price) {
        if(!titleInfoRepository.findByTitleAndAuthorAndPublicationYear(title, authorName, publicationYear).isPresent()){
            return titleInfoRepository.save(new TitleInfo(title, authorName, publicationYear, price));
        } else {
            throw new DataAlreadyFoundException(String.format("The title is already in database"));
        }
    }
    
    public void deleteById(Long id) {
        titleInfoRepository.deleteById(id);
    }
    
}
