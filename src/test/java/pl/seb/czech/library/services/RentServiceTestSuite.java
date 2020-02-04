package pl.seb.czech.library.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.*;
import pl.seb.czech.library.repositories.RentRepository;
import pl.seb.czech.library.service.BookService;
import pl.seb.czech.library.service.RentService;
import pl.seb.czech.library.service.TitleInfoService;
import pl.seb.czech.library.service.UserService;
import pl.seb.czech.library.service.exceptions.RentException;
import pl.seb.czech.library.visualTesting.DataPreparer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class RentServiceTestSuite {
    @Autowired
    RentService rentService;
    @Autowired
    TitleInfoService titleInfoService;
    @Autowired
    BookService bookService;
    @Autowired
    RentRepository rentRepository;
    @Autowired
    UserService userService;

    @Autowired
    DataPreparer dataPreparer;

    @BeforeEach
    public void populateData() {
        dataPreparer.prepareData();
    }

    @AfterEach
    public void cleanUp() {
        dataPreparer.cleanUp();
    }

    @Test
    public void rentBookTest() {
        long rentsNumBeforeRenting = rentService.getAllRentsCount();
        TitleInfo titleInfo = DataPreparer.getTitleInfoList().get(0);
        List<Book> avbBooksList = titleInfo.getBookList().stream()
                .filter(book -> book.getBookStatus().equals(BookStatus.AVAILABLE))
                .collect(Collectors.toList());

        List<Book> notAvbBookList = new ArrayList<>(titleInfo.getBookList());
        notAvbBookList.removeAll(avbBooksList);

        User userWithOneBook = DataPreparer.getUserList().get(0);
        int numOfBooksRented = 4;


        List<Rent> rents = new ArrayList<>();
        for (int i = 0; i < numOfBooksRented; i++) {
            rents.add(rentService.rentBook(userWithOneBook.getId(), avbBooksList.get(i).getId()));
        }

        long rentsNumAfterRenting = rentService.getAllRentsCount();

        long numOfAvbBooksAfterRent = titleInfoService.getNumOfAvailableBooks(titleInfo.getTitle());
        long numOfNotAvbBooksAfterREnt = titleInfoService.getNumOfAllBooks(titleInfo.getTitle()) - numOfAvbBooksAfterRent;

        assertAll(
                () -> assertEquals(avbBooksList.size() - numOfBooksRented, numOfAvbBooksAfterRent),
                () -> assertEquals(notAvbBookList.size() + numOfBooksRented, numOfNotAvbBooksAfterREnt),
                () -> assertEquals(BookStatus.RENTED, bookService.findById(avbBooksList.get(0).getId()).getBookStatus()),
                () -> assertThrows(RentException.class, () -> rentService.rentBook(userWithOneBook.getId(), avbBooksList.get(numOfBooksRented).getId())),
                () -> assertThrows(RentException.class, () -> rentService.rentBook(userWithOneBook.getId(), notAvbBookList.get(0).getId())),
                () -> assertEquals(numOfBooksRented + 1, rentService.countRentedBooksByUser(userWithOneBook.getId())),
                () -> assertEquals(rents.get(0), bookService.findById(avbBooksList.get(0).getId()).getRent()),
                () -> assertEquals(rentsNumBeforeRenting + numOfBooksRented, rentsNumAfterRenting)
        );

        rents.forEach(rent -> rentService.deleteById(rent.getId()));

        assertAll(
                () -> assertEquals(rentsNumBeforeRenting, rentService.getAllRentsCount()),
                () -> assertEquals(avbBooksList.size(), titleInfoService.getNumOfAvailableBooks(titleInfo.getTitle()))
        );
    }
    
    @Test
    public void returnBookAndCalculateFineTest() {
        Book avbBook = DataPreparer.getBookList().stream().filter(book -> book.getBookStatus().equals(BookStatus.AVAILABLE)).findFirst().get();
        
        long rentsNumBeforeRenting = rentService.getAllRentsCount();
        Rent rent = DataPreparer.getRentList().get(0);
       
        long overdue = 100L;
        
        rent.setDueDate(LocalDate.now().minusDays(overdue));
        rent = rentRepository.save(rent);
        long userId = rent.getUser().getId();

        rentService.returnBook(rent.getId());
        
        User user = userService.findUserById(userId);
        
        assertAll(
                () -> assertEquals(overdue * Fines.PER_DAY_OVERDUE, user.getFine()),
                () -> assertEquals(rentsNumBeforeRenting - 1, rentService.getAllRentsCount()),
                () -> assertThrows(RentException.class, () -> rentService.rentBook(user.getId(), avbBook.getId()))
        );
    }
    
    @Test
    public void reportDestroyedBookTest() {
        long rentId = DataPreparer.getRentList().get(0).getId();
        
        Rent rent = rentService.findRentById(rentId);
        long bookId = rent.getBook().getId();
        long userId = rent.getUser().getId();
        
        assertAll(
                () -> assertEquals(BookStatus.RENTED, bookService.findById(bookId).getBookStatus()),
                () -> assertEquals(1, rentService.countRentedBooksByUser(userId)),
                () -> assertEquals(0, userService.findUserById(userId).getFine())
        );
        
        rentService.reportLostDestroyed(rentId);

        double fine = bookService.findById(bookId).getTitleInfo().getPrice() + Fines.LOST_OR_DESTROYED;
        
        assertAll(
                () -> assertEquals(BookStatus.LOST_OR_DESTROYED, bookService.findById(bookId).getBookStatus()),
                () -> assertEquals(0, rentService.countRentedBooksByUser(userId)),
                () -> assertEquals(fine, userService.findUserById(userId).getFine())
        );
        
    }
    
    @Test
    public void prolongTest() {
        long rentId = DataPreparer.getRentList().get(0).getId();
        Rent rent = rentService.findRentById(rentId);
        LocalDate origDueDate = rent.getDueDate();
        
        rent = rentService.prolong(rentId);
        
        assertAll(
                () -> assertEquals(origDueDate.plusWeeks(2), rentService.findRentById(rentId).getDueDate()),
                () -> assertThrows(RentException.class, () -> rentService.prolong(rentId))
        );
        
        
    }
    
}
