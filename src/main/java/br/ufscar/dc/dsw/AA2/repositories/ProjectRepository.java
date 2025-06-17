package br.ufscar.dc.dsw.AA2.repositories;

import br.ufscar.dc.dsw.AA2.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
