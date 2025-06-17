package br.ufscar.dc.dsw.AA2.repositories;

import br.ufscar.dc.dsw.AA2.models.TestSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestSessionRepository extends JpaRepository<TestSession, UUID> {
}
