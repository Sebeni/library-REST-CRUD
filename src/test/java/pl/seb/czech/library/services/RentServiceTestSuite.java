package pl.seb.czech.library.services;

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


    @Test
    public void rentBookTest() {


        User user = new User("First", "Name", LocalDate.now());
        userService.saveUser(user);
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);
        List<Book> bookList = new ArrayList<>();

        int numOfNewBooks = 6;
        for (int i = 0; i < numOfNewBooks; i++) {
            bookList.add(bookService.addNewBook(titleInfo));
        }

        long avbBooksBeforeRent = titleInfoService.getNumOfAvailableBooks(titleInfo.getTitle());
        long allBooksBeforeRent = titleInfoService.getNumOfAllBooks(titleInfo.getTitle());

        List<Rent> rentList = new ArrayList<>();

        int numOfRentedBooks = 5;
        for (int i = 0; i < numOfRentedBooks; i++) {
            rentList.add(rentService.rentBook(user.getId(), bookList.get(i).getId()));
        }


        long avbBooksAfterRent = titleInfoService.getNumOfAvailableBooks(titleInfo.getTitle());

        assertAll(
                () -> assertEquals(avbBooksBeforeRent - numOfRentedBooks, avbBooksAfterRent),
                () -> assertEquals(allBooksBeforeRent, titleInfoService.getNumOfAllBooks(titleInfo.getTitle())),
                () -> assertEquals(BookStatus.RENTED, bookService.findById(bookList.get(0).getId()).getBookStatus()),
                () -> assertThrows(RentException.class, () -> rentService.rentBook(user.getId(), bookList.get(0).getId())),
                () -> assertThrows(RentException.class, () -> rentService.rentBook(user.getId(), bookList.get(numOfRentedBooks).getId())),
                () -> assertEquals(numOfRentedBooks, rentService.countRentedBooksByUser(user.getId())),
                () -> assertEquals(rentList.get(0), bookService.findById(bookList.get(0).getId()).getRent())
        );


        rentList.forEach(rent -> rentService.deleteById(rent.getId()));
        userService.deleteUser(user.getId());
        bookList.forEach(book -> bookService.deleteById(book.getId()));
      
    }

    @Test
    public void returnBookAndCalculateFineTest() {
        User user = new User("First", "Name", LocalDate.now());
        userService.saveUser(user);
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);
        Book book = bookService.addNewBook(titleInfo);
        Book book1 = bookService.addNewBook(titleInfo);
        Rent rent = rentService.rentBook(user.getId(), book.getId());


        long overdue = 100L;

        rent.setDueDate(LocalDate.now().minusDays(overdue));
        rent = rentRepository.save(rent);


        rentService.returnBook(rent.getId());

        double fine = overdue * Fine.PER_DAY_OVERDUE;

        assertAll(
                () -> assertEquals(fine, userService.findUserById(user.getId()).getFine()),
                () -> assertThrows(RentException.class, () -> rentService.rentBook(user.getId(), book1.getId()))
        );

        user.payFine(fine);
       
        userService.deleteUser(user.getId());
        bookService.deleteById(book.getId());
        bookService.deleteById(book1.getId());


    }

    @Test
    public void reportDestroyedBookTest() {
        User user = new User("First", "Name", LocalDate.now());
        userService.saveUser(user);
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);
        Book book = bookService.addNewBook(titleInfo);
        Rent rent = rentService.rentBook(user.getId(), book.getId());

        assertAll(
                () -> assertEquals(BookStatus.RENTED, bookService.findById(book.getId()).getBookStatus()),
                () -> assertEquals(1, rentService.countRentedBooksByUser(user.getId())),
                () -> assertEquals(0, userService.findUserById(user.getId()).getFine())
        );

        rentService.reportLostDestroyed(rent.getId());

        double fine = bookService.findById(book.getId()).getTitleInfo().getPrice() + Fine.LOST_OR_DESTROYED;

        assertAll(
                () -> assertEquals(BookStatus.LOST_OR_DESTROYED, bookService.findById(book.getId()).getBookStatus()),
                () -> assertEquals(0, rentService.countRentedBooksByUser(user.getId())),
                () -> assertEquals(fine, userService.findUserById(user.getId()).getFine())
        );

        user.payFine(fine);

        userService.deleteUser(user.getId());
        bookService.deleteById(book.getId());

    }

    @Test
    public void prolongTest() {
        User user = new User("First", "Name", LocalDate.now());
        userService.saveUser(user);
        TitleInfo titleInfo = dataPreparer.getTitleInfoList().get(0);
        Book book = bookService.addNewBook(titleInfo);
        Rent rent = rentService.rentBook(user.getId(), book.getId());

        LocalDate origDueDate = rent.getDueDate();

        rent = rentService.prolong(rent.getId());
        long rentId = rent.getId();


        assertAll(
                () -> assertEquals(origDueDate.plusWeeks(2), rentService.findRentById(rentId).getDueDate()),
                () -> assertThrows(RentException.class, () -> rentService.prolong(rentId))
        );

        rentService.returnBook(rent.getId());
        userService.deleteUser(user.getId());
        bookService.deleteById(book.getId());


    }

}
