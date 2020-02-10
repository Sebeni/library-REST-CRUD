package pl.seb.czech.library.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pl.seb.czech.library.domain.Rent;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.visualTesting.DataPreparer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
class LibraryRepositoriesTests {
    @Autowired
    DataPreparer dataPreparer;
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
//    	when
        long numOfBooks = bookRepository.count();
        long numOfTitleInfos = titleInfoRepository.count();
        long numOfRents = userRepository.count();
        long numOfUsers = rentRepository.count();

//		then
        Assertions.assertAll(
                () -> Assertions.assertEquals(dataPreparer.getBookList().size(), numOfBooks),
                () -> Assertions.assertEquals(dataPreparer.getTitleInfoList().size(), numOfTitleInfos),
                () -> Assertions.assertEquals(dataPreparer.getRentList().size(), numOfRents),
                () -> Assertions.assertEquals(dataPreparer.getUserList().size(), numOfUsers)
        );
    }

    @Test
    void userNameEqualityTest() {
        List<User> userListInput = dataPreparer.getUserList();
        List<User> userListFromDB = userRepository.findAll();
        
        Assertions.assertTrue(userListInput.containsAll(userListFromDB));
    }

 

    @Test
    void titleInfoEqualityTest() {
        List<TitleInfo> titleInfosDB = titleInfoRepository.findAll();
        List<TitleInfo> titleInfosInput = dataPreparer.getTitleInfoList();

        Assertions.assertTrue(titleInfosInput.containsAll(titleInfosDB));
    }

    @Test
    void rentDateEqualityTest() {
        List<Rent> rentsInput = dataPreparer.getRentList();
        List<Rent> rentsDB = rentRepository.findAll();
        
        Assertions.assertAll(
                () -> Assertions.assertTrue(extractInitDate(rentsInput).containsAll(extractInitDate(rentsDB))),
                () -> Assertions.assertTrue(extractUser(rentsInput).containsAll(extractUser(rentsDB))),
                () -> Assertions.assertTrue(rentsInput.containsAll(rentsDB))
        );
        
        
    }

    private List<LocalDate> extractInitDate(List<Rent> rentList) {
        return rentList.stream()
                .map(rent -> rent.getRentDate())
                .collect(Collectors.toList());
    }
    
    private List<User> extractUser(List<Rent> rentList) {
        return rentList.stream()
                .map(rent -> rent.getUser())
                .collect(Collectors.toList());
    }
    
    @Test
    public void countRentsByUserId() {
        long userId = dataPreparer.getUserList().get(0).getId();
        long countRentedBooksWithUserId = dataPreparer.getRentList().stream()
                .filter(rent -> rent.getUser().getId().equals(userId))
                .count();
        
        Assertions.assertEquals(countRentedBooksWithUserId, rentRepository.countByUserId(userId));
    }
    
    


}
