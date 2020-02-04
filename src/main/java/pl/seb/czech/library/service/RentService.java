package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.*;
import pl.seb.czech.library.repositories.RentRepository;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
import pl.seb.czech.library.service.exceptions.RentException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
public class RentService {
    private UserService userService;
    private BookService bookService;
    private RentRepository rentRepository;

    public Rent findRentById(Long id) {
        return rentRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public Rent rentBook(Long userId, Long bookId) {
        if (countRentedBooksByUser(userId) >= 5) {
            throw new RentException("You have rented maximum number of books.");
        }
        User rentingUser = userService.findUserById(userId);

        if (rentingUser.getFine() >= 1.00) {
            throw new RentException("Before renting a new book you must pay your fines");
        }
        Book rentedBook = bookService.findById(bookId);
        if (rentedBook.getBookStatus().equals(BookStatus.AVAILABLE)) {
            Rent rent = new Rent(rentingUser, rentedBook);
            rentRepository.save(rent);
            rentedBook.setRent(rent);
            bookService.saveBook(rentedBook);
            bookService.changeBookStatusByBook(rentedBook, BookStatus.RENTED);
            return rent;
        } else {
            throw new RentException("Book is currently not available");
        }
    }

    public long countRentedBooksByUser(Long userId) {
        return rentRepository.countByUserId(userId);
    }
    
    public long getAllRentsCount(){
        return rentRepository.count();
    }

    public void deleteById(Long rentId) {
        Rent rentToDelete = findRentById(rentId);
        bookService.changeBookStatusByBook(rentToDelete.getBook(), BookStatus.AVAILABLE);
        rentRepository.delete(rentToDelete);
    }
    
    public void returnBook(Long rentId) {
        Rent rent = findRentById(rentId);
        if(rent.getDueDate().isBefore(LocalDate.now())) {
            userService.addFine(rent.getUser(), calculateFine(rent));
        }
        deleteById(rentId);
    }
    
    private double calculateFine(Rent rent) {
        long daysOverdue = ChronoUnit.DAYS.between(rent.getDueDate(), LocalDate.now());
        return  daysOverdue * Fines.PER_DAY_OVERDUE;
    }
    
    public void reportLostDestroyed(Long rentId) {
        Rent rent = findRentById(rentId);
        User user = rent.getUser();
        Book lostOrDestroyed = rent.getBook();
        deleteById(rentId);
        userService.addFine(user, lostOrDestroyed.getTitleInfo().getPrice() + Fines.LOST_OR_DESTROYED);
        bookService.changeBookStatusByBook(lostOrDestroyed, BookStatus.LOST_OR_DESTROYED);
    }
    
    public Rent prolong(Long rentId) {
        Rent rent = findRentById(rentId);
        if(!rent.isDueDateProlonged()) {
            rent.prolong();
            return rentRepository.save(rent);
        } else {
            throw new RentException("This book was already prolonged");
        }
        
    }
}
