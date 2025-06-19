package br.ufscar.dc.dsw.AA2.config;

import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User user = new User();
                user.setEmail("vihilgerttomasel@gmail.com");
                user.setPassword(passwordEncoder.encode("password"));
                user.setName("Vitória Admin");
                user.setRole(UserRoleEnum.ADMIN);
                userRepository.save(user);
                System.out.println("Seed executada com sucesso.");
            }
        };
    }
}
