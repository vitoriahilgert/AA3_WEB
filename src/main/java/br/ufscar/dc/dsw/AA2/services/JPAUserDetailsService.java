package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JPAUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // O nome do método é 'loadUserByUsername', mas a string que chega aqui
        // é o que o usuário digitou no campo 'username' (que é o email).
        return userRepository.findByEmail(email) // ou findByUsername, dependendo do seu repositório
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}
