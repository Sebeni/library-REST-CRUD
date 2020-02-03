package pl.seb.czech.library.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.seb.czech.library.domain.User;
import pl.seb.czech.library.repositories.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) throws DataNotFoundException {
        return userRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public User findUserByName(String firstName, String lastName) throws DataNotFoundException {
        return userRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow(DataNotFoundException::new);
    }
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
