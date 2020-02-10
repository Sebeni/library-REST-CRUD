package pl.seb.czech.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.repositories.RentRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.exceptions.DataAlreadyFoundException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
import pl.seb.czech.library.service.exceptions.RentException;

import java.math.BigDecimal;
import java.util.List;


@Service
public class TitleInfoService {
    private TitleInfoRepository titleInfoRepository;
    private RentRepository rentRepository;
    
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
    
    public void deleteById(Long titleInfoId) {
        if(titleInfoRepository.existsById(titleInfoId)) {
            if(rentRepository.findByBookTitleInfoId(titleInfoId).size() == 0){
                titleInfoRepository.deleteById(titleInfoId);
            } else {
                throw new RentException("Can't delete titleInfo - there are rented books");
            }
        } else {
            throw new DataNotFoundException("titleInfo", titleInfoId.toString());
        }
    }
    
    public TitleInfo findById(Long titleInfoId) {return titleInfoRepository.findById(titleInfoId).orElseThrow(() -> new DataNotFoundException("titleInfo", titleInfoId.toString()));}


    @Autowired
    public void setTitleInfoRepository(TitleInfoRepository titleInfoRepository) {
        this.titleInfoRepository = titleInfoRepository;
    }

    @Autowired
    public void setRentRepository(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }
}
