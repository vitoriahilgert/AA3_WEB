package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import br.ufscar.dc.dsw.AA2.dtos.UserRecordDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User saveUser(UserRecordDto userDto) {
        User user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setRole(userDto.role());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(UUID id, UserRecordDto userDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDto.name());
            user.setEmail(userDto.email());
            user.setPassword(userDto.password());
            user.setRole(userDto.role());
            return userRepository.save(user);
        }
        return null;
    }
}
