package br.ufscar.dc.dsw.AA2.services;


import br.ufscar.dc.dsw.AA2.dtos.ProjectRecordDto;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.repositories.ProjectRepository;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(UUID id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project saveProject(ProjectRecordDto projectDto) {
        Project project = new Project();
        project.setName(projectDto.name());
        project.setDescription(projectDto.description());
        project.setCreationDateTime(projectDto.creationDateTime());

        if (projectDto.allowedMemberIds() != null && !projectDto.allowedMemberIds().isEmpty()) {
            List<User> allowedUsers = userRepository.findAllById(projectDto.allowedMemberIds());
            project.setAllowedMembers(allowedUsers);
        } else {
            project.setAllowedMembers(List.of());
        }
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(UUID id, ProjectRecordDto projectDto) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            project.setName(projectDto.name());
            project.setDescription(projectDto.description());

            if (projectDto.allowedMemberIds() != null) {
                List<User> allowedUsers = userRepository.findAllById(projectDto.allowedMemberIds());
                project.setAllowedMembers(allowedUsers);
            }
            return projectRepository.save(project);
        }
        return null;
    }

    @Transactional
    public void deleteProject(UUID id) {projectRepository.deleteById(id);}

}