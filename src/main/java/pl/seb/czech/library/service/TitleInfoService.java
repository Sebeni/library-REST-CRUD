package pl.seb.czech.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.exceptions.DataAlreadyFoundException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;

import java.math.BigDecimal;
import java.util.List;


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
    
    public TitleInfo addTitleInfo(String title, String authorName, Integer publicationYear, BigDecimal price) {
        if(!titleInfoRepository.findByTitleAndAuthorAndPublicationYear(title, authorName, publicationYear).isPresent()){
            return titleInfoRepository.save(new TitleInfo(title, authorName, publicationYear, price));
        } else {
            throw new DataAlreadyFoundException("titleInfo", title, authorName, publicationYear.toString(), String.valueOf(price));
        }
    }
    
    public TitleInfo addTitleInfo(TitleInfo titleInfo) {
        return addTitleInfo(titleInfo.getTitle(), titleInfo.getAuthor(), titleInfo.getPublicationYear(), titleInfo.getPrice());
    }
    
    public void deleteById(Long id) {
        titleInfoRepository.deleteById(id);
    }
    
    public TitleInfo findById(Long id) {return titleInfoRepository.findById(id).orElseThrow(() -> new DataNotFoundException("titleInfo", id.toString()));}


    @Autowired
    public void setTitleInfoRepository(TitleInfoRepository titleInfoRepository) {
        this.titleInfoRepository = titleInfoRepository;
    }
}
