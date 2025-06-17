package br.ufscar.dc.dsw.AA2.repositories;

import br.ufscar.dc.dsw.AA2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
