package pl.seb.czech.library.initDB;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.seb.czech.library.domain.*;
import pl.seb.czech.library.repositories.BookRepository;
import pl.seb.czech.library.repositories.RentRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataPreparer implements ApplicationRunner {
    
    private List<User> userList = new ArrayList<>();
    private List<TitleInfo> titleInfoList = new ArrayList<>();
    private final List<Book> bookList = new ArrayList<>();
    private final List<Rent> rentList = new ArrayList<>();

    private final BookRepository bookRepository;
    private final RentRepository rentRepository;
    private final TitleInfoRepository titleInfoRepository;
    private final UserRepository userRepository;


    public DataPreparer(BookRepository bookRepository, RentRepository rentRepository, TitleInfoRepository titleInfoRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.rentRepository = rentRepository;
        this.titleInfoRepository = titleInfoRepository;
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) {
       
            initializeDB();
        
    }

    private void initializeDB() {
       
        TitleInfo it = new TitleInfo("It", "Stephen King", 1986, BigDecimal.valueOf(12.99));
        TitleInfo braveNW = new TitleInfo("Brave New World", "Aldous Huxley", 1932, BigDecimal.valueOf(9.99));
        TitleInfo year1984 = new TitleInfo("Nineteen Eighty-Four", "Georg Orwell", 1949, BigDecimal.valueOf(8.99));
        TitleInfo plague = new TitleInfo("Plague", "Albert Camus", 1947, BigDecimal.valueOf(7.99));
        TitleInfo catch22 = new TitleInfo("Catch 22", "Joseph Heller", 1961, BigDecimal.valueOf(11.99));
        TitleInfo cuckoo = new TitleInfo("One Flew Over the Cuckoo’s Nest", "Ken Kesey", 1962, BigDecimal.valueOf(11.49));
        titleInfoList = Arrays.asList(it, braveNW, year1984, plague, catch22, cuckoo);
        titleInfoRepository.saveAll(titleInfoList);

        int numOfBooksOfEachTitle = 10;

        for (TitleInfo t : titleInfoList) {
            List<Book> temp = new ArrayList<>();
            for (int i = 0; i < numOfBooksOfEachTitle; i++) {
                Book b = new Book(t, BookStatus.AVAILABLE);
                switch (i) {
                    case 0:
                        b.setBookStatus(BookStatus.RENTED);
                        break;
                    case 1:
                        b.setBookStatus(BookStatus.LOST_OR_DESTROYED);
                        break;
                }

                bookList.add(b);
                temp.add(b);
                bookRepository.save(b);
            }
            t.getBookList().addAll(temp);
            titleInfoRepository.save(t);
        }


        User user1 = new User("John", "Smith", LocalDate.of(2001, 1, 1));
        User user2 = new User("Atilla", "Hun", LocalDate.of(2002, 2, 2));
        User user3 = new User("Julius", "Caesar", LocalDate.of(2003, 3, 3));
        User user4 = new User("Sun", "Tzu", LocalDate.of(2004, 4, 4));
        User user5 = new User("Cornelius", "Sulla", LocalDate.of(2005, 5, 5));
        User user6 = new User("Gaius", "Marius", LocalDate.of(2006, 6, 6));


        userList = Arrays.asList(user1, user2, user3, user4, user5, user6);
        userRepository.saveAll(userList);

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            Book book = bookList.get(i * (numOfBooksOfEachTitle));

            Rent rent = new Rent(user, book);
            rentRepository.save(rent);
            rentList.add(rent);
            
            userRepository.save(user);

            book.setRent(rent);
            bookRepository.save(book);
        }
    }
    
    public List<User> getUserList() {
        return new ArrayList<>(userList);
    }

    public List<TitleInfo> getTitleInfoList() {
        return new ArrayList<>(titleInfoList);
    }

    public List<Book> getBookList() {
        return new ArrayList<>(bookList);
    }

    public  List<Rent> getRentList() {
        return new ArrayList<>(rentList);
    }
}
