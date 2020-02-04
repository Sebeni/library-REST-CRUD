package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.repositories.UserRepository;
import pl.seb.czech.library.service.exceptions.DataNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    public User addUser(String firstName, String lastName, String ddMMyyyy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate dob = LocalDate.parse(ddMMyyyy, formatter);
        return saveUser(new User(firstName, lastName, dob));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) throws DataNotFoundException {
        return userRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public User findUserByName(String firstName, String lastName) throws DataNotFoundException {
        return userRepository.findByFirstNameAndLastName(firstName.trim(), lastName.trim()).orElseThrow(DataNotFoundException::new);
    }
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public User addFine(User user, double howMuch) {
        user.addToFine(howMuch);
        return saveUser(user);
    }
    
    public User payFine(User user, double howMuch) {
        user.payFine(howMuch);
        return saveUser(user);
    }
    
    
}
