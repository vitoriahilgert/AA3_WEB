package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.exceptions.ResourceNotFoundException;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import br.ufscar.dc.dsw.AA2.dtos.UserRecordDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findByRole(UserRoleEnum role) { return userRepository.findByRole(role); }

    @Transactional
    public User saveUser(UserRecordDto userDto) {
        User user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(userDto.role());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));

        for (Project project : new ArrayList<>(user.getProjects())) {
            project.getAllowedMembers().remove(user);
        }

        userRepository.deleteById(id);
    }

    public User updateUser(UUID id, UserRecordDto userDto) {
        User userToUpdate = this.getUserById(id);

        if (userToUpdate != null) {
            userToUpdate.setName(userDto.name());
            userToUpdate.setEmail(userDto.email());

            if (userDto.password() != null && !userDto.password().isEmpty()) {
                userToUpdate.setPassword(passwordEncoder.encode(userDto.password()));
            }

            return userRepository.save(userToUpdate);
        }

        return null;
    }

    @Transactional
    public List<User> getAllTesters() {
        return userRepository.findByRole(UserRoleEnum.TESTER);
    }

}
