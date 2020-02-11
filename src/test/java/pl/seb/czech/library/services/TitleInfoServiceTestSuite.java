package pl.seb.czech.library.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.BookStatus;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.service.BookService;
import pl.seb.czech.library.service.exceptions.DataAlreadyFoundException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
import pl.seb.czech.library.service.TitleInfoService;
import pl.seb.czech.library.initDB.DataPreparer;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class TitleInfoServiceTestSuite {
    @Autowired
    DataPreparer dataPreparer;


    @Autowired
    TitleInfoService titleInfoService;

    @Autowired
    TitleInfoRepository titleInfoRepository;

    @Autowired
    BookService bookService;

    @Test
    public void getNumOfAvailableBooksTest() {
        String title = "It";
        TitleInfo titleInfo = titleInfoService.findByAuthor("Stephen King").get(0);

        assertEquals(8, titleInfoService.getNumOfAvailableBooks(title));


        Book book = bookService.addNewBook(titleInfo);


        assertEquals(9, titleInfoService.getNumOfAvailableBooks(title));

        bookService.deleteById(book.getId());

        assertEquals(8, titleInfoService.getNumOfAvailableBooks(title));


    }

    @Test
    public void findByAuthorTest() {
        String author = "Georg Orwell";

        List<TitleInfo> booksWithAuthorInputNum = dataPreparer.getTitleInfoList().stream()
                .filter(titleInfo -> titleInfo.getAuthor().equals(author))
                .collect(Collectors.toList());

        List<TitleInfo> booksWithAuthorDBNum = titleInfoService.findByAuthor(author);

        Assertions.assertTrue(booksWithAuthorInputNum.containsAll(booksWithAuthorDBNum));
    }

    @Test
    public void findByAuthorAndTitleTest() {
        String author = "Georg Orwell";
        String title = "Nineteen Eighty-Four";

        TitleInfo titleInfoInput = dataPreparer.getTitleInfoList().stream()
                .filter(titleInfo -> titleInfo.getAuthor().equals(author) && titleInfo.getTitle().equals(title))
                .findAny().get();

        TitleInfo titleInfoDB = titleInfoService.findByAuthorAndTitle(author, title);


        assertAll(
                () -> assertEquals(titleInfoInput, titleInfoDB),
                () -> Assertions.assertThrows(DataNotFoundException.class, () -> titleInfoService.findByAuthorAndTitle(author + "s", title))
        );
    }

    @Test
    public void addNewTitleTest() {
        String author = "Georg Orwell";
        String title = "Nineteen Eighty-Four";
        Integer addedPublicationYear = 1984;
        Integer oldPublicationYear = 1949;


        TitleInfo addedTitleInfo = titleInfoService.addTitleInfo(title, author, addedPublicationYear, BigDecimal.valueOf(10.99));

        assertAll(
                () -> assertEquals(dataPreparer.getTitleInfoList().size() + 1, titleInfoRepository.count()),
                () -> Assertions.assertThrows(DataAlreadyFoundException.class, () -> titleInfoService.addTitleInfo(title, author, oldPublicationYear, BigDecimal.valueOf(10.99)))
        );

        titleInfoService.deleteById(addedTitleInfo.getId());

        assertEquals(dataPreparer.getTitleInfoList().size(), titleInfoRepository.count());
    }

}
