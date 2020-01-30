package pl.seb.czech.library;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.*;
import pl.seb.czech.library.repositories.BookRepository;
import pl.seb.czech.library.repositories.RentRepository;
import pl.seb.czech.library.repositories.TitleInfoRepository;
import pl.seb.czech.library.repositories.UserRepository;

@SpringBootTest
class LibraryApplicationTests {
	@Autowired
	BookRepository bookRepository;

	@Autowired
	RentRepository rentRepository;

	@Autowired
	TitleInfoRepository titleInfoRepository;

	@Autowired
	UserRepository userRepository;
	

    @Test
    void entitiesCountTest() {
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
    	
//    	when
		long numOfBooks = bookRepository.count();
		long numOfTitleInfos = titleInfoRepository.count();
		long numOfRents = rentRepository.count();
		long numOfUsers = rentRepository.count();
		
    	Assertions.assertAll(
    			() -> Assertions.assertEquals(4, numOfBooks),
    			() -> Assertions.assertEquals(2, numOfTitleInfos),
    			() -> Assertions.assertEquals(1, numOfRents),
    			() -> Assertions.assertEquals(1, numOfUsers)
				
		);
    }

}
