package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.exceptions.DataAlreadyFoundException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;

import java.util.List;

@AllArgsConstructor
@Service
public class TitleInfoService {
    private TitleInfoRepository titleInfoRepository;
    
    public long getNumOfAvailableBooks(String title) {
        return titleInfoRepository.getNumOfBooksAvb(title.trim());
    }
    
    public long getNumOfAllBooks(String title)  {
        return titleInfoRepository.getNumOfAllBooks(title.trim());
    }
    
    public List<TitleInfo> findByAuthor(String authorsName) {
        return titleInfoRepository.findByAuthor(authorsName.trim());
    }
    
    public TitleInfo findByAuthorAndTitle(String authorName, String title)  {
        return titleInfoRepository.findByAuthorAndTitle(authorName.trim(), title.trim()).orElseThrow(() -> new DataNotFoundException("titleInfo", authorName, title));
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
    
    public TitleInfo findById(Long id) {return titleInfoRepository.findById(id).orElseThrow(() -> new DataNotFoundException("titleInfo", id.toString()));}
    
}
