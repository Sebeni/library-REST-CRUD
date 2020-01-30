package pl.seb.czech.library.visualTesting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.seb.czech.library.domain.*;
import pl.seb.czech.library.repositories.BookRepository;
import pl.seb.czech.library.repositories.RentRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.repositories.UserRepository;

@Component
public class ForH2Debug implements ApplicationListener<ContextRefreshedEvent> {

    private BookRepository bookRepository;
    private RentRepository rentRepository;
    private TitleInfoRepository titleInfoRepository;
    private UserRepository userRepository;

    public ForH2Debug(BookRepository bookRepository, RentRepository rentRepository, TitleInfoRepository titleInfoRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.rentRepository = rentRepository;
        this.titleInfoRepository = titleInfoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData() {
        TitleInfo it = new TitleInfo("It", "Stephen King", 1986);
        Book itFirstBook = new Book(BookStatus.AVAILABLE);
        Book itSecondBook = new Book(BookStatus.DESTROYED);


        TitleInfo braveNW = new TitleInfo("Brave New World", "Aldous Huxley", 1932);
        Book bnwFirstBook = new Book(BookStatus.LOST);
        Book bnwSecondBook = new Book(BookStatus.RENTED);

        User user1 = new User("First", "John");
        Rent rentFirst = new Rent();

        titleInfoRepository.save(it);
        titleInfoRepository.save(braveNW);

        bookRepository.save(itFirstBook);
        bookRepository.save(itSecondBook);
        bookRepository.save(bnwFirstBook);
        bookRepository.save(bnwSecondBook);

        userRepository.save(user1);
        rentRepository.save(rentFirst);

        it.getBookList().add(itFirstBook);
        itFirstBook.setTitleInfo(it);
        it.getBookList().add(itSecondBook);
        itSecondBook.setTitleInfo(it);

        braveNW.getBookList().add(bnwFirstBook);
        bnwFirstBook.setTitleInfo(braveNW);

        braveNW.getBookList().add(bnwSecondBook);
        bnwSecondBook.setTitleInfo(braveNW);


        rentFirst.setBook(itFirstBook);
        itFirstBook.setRent(rentFirst);
        rentFirst.setUser(user1);
        user1.getRents().add(rentFirst);

        titleInfoRepository.save(it);
        titleInfoRepository.save(braveNW);

        bookRepository.save(itFirstBook);
        bookRepository.save(itSecondBook);
        bookRepository.save(bnwFirstBook);
        bookRepository.save(bnwSecondBook);

        userRepository.save(user1);
        rentRepository.save(rentFirst);
        
    }
    
    
}
