package pl.seb.czech.library.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.seb.czech.library.domain.Book;
import pl.seb.czech.library.domain.Rent;
import pl.seb.czech.library.domain.TitleInfo;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.visualTesting.DataPreparer;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @BeforeEach
    public void populateData() {
        dataPreparer.prepareData();
    }

    @AfterEach
    public void cleanUp() {
        dataPreparer.cleanUp();
    }

    @Test
    void entitiesCountTest() {
//    	when
        long numOfBooks = bookRepository.count();
        long numOfTitleInfos = titleInfoRepository.count();
        long numOfRents = userRepository.count();
        long numOfUsers = rentRepository.count();

//		then
        Assertions.assertAll(
                () -> Assertions.assertEquals(DataPreparer.getBookList().size(), numOfBooks),
                () -> Assertions.assertEquals(DataPreparer.getTitleInfoList().size(), numOfTitleInfos),
                () -> Assertions.assertEquals(DataPreparer.getRentList().size(), numOfRents),
                () -> Assertions.assertEquals(DataPreparer.getUserList().size(), numOfUsers)

        );
    }

    @Test
    void userNameEqualityTest() {
        List<User> userListFromDB = userRepository.findAll();
        List<User> userListInput = DataPreparer.getUserList();
        Assertions.assertTrue(onlyNames(userListInput).containsAll(onlyNames(userListFromDB)));
    }

    private List<String> onlyNames(List<User> userList) {
        return userList.stream()
                .map(user -> user.getFirstName() + user.getLastName())
                .collect(Collectors.toList());
    }

    @Test
    void titleInfoEqualityTest() {
        List<TitleInfo> titleInfosDB = titleInfoRepository.findAll();
        List<TitleInfo> titleInfosInput = DataPreparer.getTitleInfoList();

        Assertions.assertTrue(titleInfosInput.containsAll(titleInfosDB));
    }

    @Test
    void rentDateEqualityTest() {
        List<Rent> rentsDB = rentRepository.findAll();
        List<Rent> rentsInput = DataPreparer.getRentList();

        Assertions.assertTrue(extractInitDate(rentsDB).containsAll(extractInitDate(rentsInput)));


    }

    private List<LocalDate> extractInitDate(List<Rent> rentList) {
        return rentList.stream()
                .map(rent -> rent.getRentDate())
                .collect(Collectors.toList());
    }


}
