package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.repositories.UserRepository;
import pl.seb.czech.library.service.exceptions.DataAlreadyFoundException;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;
import pl.seb.czech.library.service.exceptions.RentException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class UserService {
    private UserRepository userRepository;
    private RentService rentService;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User addNewUser(String firstName, String lastName, String ddMMyyyy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate dob = LocalDate.parse(ddMMyyyy, formatter);
        if (userRepository.findByFirstNameAndLastNameAndBirthDate(firstName, lastName, dob).isPresent()) {
            throw new DataAlreadyFoundException("user", firstName, lastName, ddMMyyyy);
        } else {
            return saveUser(new User(firstName, lastName, dob));
        }
    }

    public User updateUserFirstName(Long id, String changedFirstName) {
        User userToChange = findUserById(id);
        userToChange.setFirstName(changedFirstName);
        return saveUser(userToChange);
    }

    public User updateUserLastName(Long id, String changedLastName) {
        User userToChange = findUserById(id);
        userToChange.setLastName(changedLastName);
        return saveUser(userToChange);
    }

    public User updateUserBirthDate(Long id, String ddMMyyyy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate dob = LocalDate.parse(ddMMyyyy, formatter);
        User userToChange = findUserById(id);
        userToChange.setBirthDate(dob);
        return saveUser(userToChange);
    }

    public void deleteUser(Long userId) {
        if (rentService.countRentedBooksByUser(userId) > 0) {
            throw new RentException("Cant delete user because he still has rented books");
        } else {
            userRepository.deleteById(userId);
        }
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("user", id.toString()));
    }

    public List<User> findUserByName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName.trim(), lastName.trim());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User addFine(Long userId, BigDecimal howMuch) {
        return addFine(findUserById(userId), howMuch);
    }

    public User addFine(User user, BigDecimal howMuch) {
        user.addToFine(howMuch.abs());
        return saveUser(user);
    }

    public User payFine(Long userId, BigDecimal howMuch) {
        return payFine(findUserById(userId), howMuch);
    }

    public User payFine(User user, BigDecimal howMuch) {
        BigDecimal fine = howMuch.abs();
        if (user.getFine().compareTo(fine) < 0) {
            String warnMessage = String.format("You want to pay: %.2f but the fine is only %.2f", fine, user.getFine());
            throw new IllegalArgumentException(warnMessage);
        }
        user.payFine(fine);
        return saveUser(user);
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRentService(RentService rentService) {
        this.rentService = rentService;
    }
}
