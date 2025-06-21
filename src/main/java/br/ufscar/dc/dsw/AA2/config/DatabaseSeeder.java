package br.ufscar.dc.dsw.AA2.config;

import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;
import br.ufscar.dc.dsw.AA2.repositories.ProjectRepository;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder, ProjectRepository projectRepository) {
        return args -> {
            // Verifica se a tabela está vazia para evitar duplicação em cada reinício
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setEmail("vihilgerttomasel@gmail.com");
                admin.setPassword(passwordEncoder.encode("password")); // Senha: password
                admin.setName("Vitória Admin");
                admin.setRole(UserRoleEnum.ADMIN);
                userRepository.save(admin);
                System.out.println("Usuário Admin 'vihilgerttomasel@gmail.com' cadastrado.");

                User tester1 = new User();
                tester1.setEmail("nandaq2003@gmail.com");
                tester1.setPassword(passwordEncoder.encode("password")); // Senha: tester123
                tester1.setName("Maria Fernanda");
                tester1.setRole(UserRoleEnum.TESTER);
                userRepository.save(tester1);
                System.out.println("Usuário Tester 'nandaq2003@gmail.com' cadastrado.");

                User tester2 = new User();
                tester2.setEmail("rodrigo@estudante.ufscar.br");
                tester2.setPassword(passwordEncoder.encode("password")); // Senha: tester123
                tester2.setName("Rodrigo");
                tester2.setRole(UserRoleEnum.TESTER);
                userRepository.save(tester2);
                System.out.println("Usuário Tester 'rodrigo@estudante.ufscar.br' cadastrado.");

                User tester3 = new User();
                tester3.setEmail("sakai@gmail.com");
                tester3.setPassword(passwordEncoder.encode("password"));
                tester3.setName("Pedro Sakai");
                tester3.setRole(UserRoleEnum.TESTER);
                userRepository.save(tester3);
                System.out.println("Usuário Tester 'sakai@gmail.com' cadastrado.");

                System.out.println("Seed de usuários executada com sucesso.");
            } else {
                System.out.println("Banco de dados já contém usuários. Seed de usuários ignorada.");
            }
            if (projectRepository.count() == 0) {
                User testerMaria = userRepository.findByEmail("nandaq2003@gmail.com").orElse(null);
                User testerRodrigo = userRepository.findByEmail("rodrigo@estudante.ufscar.br").orElse(null);

                Project project1 = new Project();
                project1.setName("ETv1");
                project1.setDescription("Exploratory testing em jogos 2D com foco em bugs graves");

                if (testerMaria != null) {
                    project1.addAllowedMember(testerMaria);
                }
                if (testerRodrigo != null) {
                    project1.addAllowedMember(testerRodrigo);
                }

                projectRepository.save(project1);
                System.out.println("Projeto 'ETv1' cadastrado com testadores associados.");
            } else {
                System.out.println("Banco de dados já contém projetos. Seed de projetos ignorada.");
            }
        };
    }
}
