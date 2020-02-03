package pl.seb.czech.library.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.DataAlreadyFoundException;
import pl.seb.czech.library.service.DataNotFoundException;
import pl.seb.czech.library.service.TitleInfoService;
import pl.seb.czech.library.visualTesting.DataPreparer;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TitleInfoServiceTestSuite {
    @Autowired
    DataPreparer dataPreparer;

    @Autowired
    TitleInfoService titleInfoService;

    @Autowired
    TitleInfoRepository titleInfoRepository;

    @BeforeEach
    public void populateData() {
        dataPreparer.prepareData();
    }

    @AfterEach
    public void cleanUp() {
        dataPreparer.cleanUp();
    }

    @Test
    public void getNumOfAvailableBooksTest() {
        String title = "It";

        Long availableItBooksInputNum = DataPreparer.getTitleInfoList().stream()
                .filter(titleInfo -> titleInfo.getTitle().equals(title))
                .flatMap(titleInfo -> titleInfo.getBookList().stream())
                .filter(book -> book.getBookStatus().equals(BookStatus.AVAILABLE))
                .count();

        long countAvbFromDb = titleInfoService.getNumOfAvailableBooks(title);
        Assertions.assertEquals(availableItBooksInputNum, countAvbFromDb);
    }

    @Test
    public void findByAuthorTest() {
        String author = "Georg Orwell";

        List<TitleInfo> booksWithAuthorInputNum = DataPreparer.getTitleInfoList().stream()
                .filter(titleInfo -> titleInfo.getAuthor().equals(author))
                .collect(Collectors.toList());

        List<TitleInfo> booksWithAuthorDBNum = titleInfoService.findByAuthor(author);

        Assertions.assertTrue(booksWithAuthorInputNum.containsAll(booksWithAuthorDBNum));
    }

    @Test
    public void findByAuthorAndTitleTest() {
        String author = "Georg Orwell";
        String title = "Nineteen Eighty-Four";

        TitleInfo titleInfoInput = DataPreparer.getTitleInfoList().stream()
                .filter(titleInfo -> titleInfo.getAuthor().equals(author) && titleInfo.getTitle().equals(title))
                .findAny().get();

        TitleInfo titleInfoDB = titleInfoService.findByAuthorAndTitle(author, title);


        Assertions.assertAll(
                () -> Assertions.assertEquals(titleInfoInput, titleInfoDB),
                () -> Assertions.assertThrows(DataNotFoundException.class, () -> titleInfoService.findByAuthorAndTitle(author + "s", title))
        );
    }

    @Test
    public void addNewTitleTest() {
        String author = "Georg Orwell";
        String title = "Nineteen Eighty-Four";
        Integer addedPublicationYear = 1984;
        Integer oldPublicationYear = 1949;


        TitleInfo addedTitleInfo = titleInfoService.addTitleInfo(title, author, addedPublicationYear, 10.99);

        Assertions.assertAll(
                () -> Assertions.assertEquals(DataPreparer.getTitleInfoList().size() + 1, titleInfoRepository.count()),
                () -> Assertions.assertThrows(DataAlreadyFoundException.class, () -> titleInfoService.addTitleInfo(title, author, oldPublicationYear, 10.99))
        );

        titleInfoService.deleteById(addedTitleInfo.getId());
        
        Assertions.assertEquals(DataPreparer.getTitleInfoList().size(), titleInfoRepository.count());
    }

}
