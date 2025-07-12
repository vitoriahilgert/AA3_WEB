package br.ufscar.dc.dsw.AA2.services;


import br.ufscar.dc.dsw.AA2.dtos.project.CreateProjectRequestDTO;
import br.ufscar.dc.dsw.AA2.dtos.project.ProjectFormDTO;
import br.ufscar.dc.dsw.AA2.dtos.project.UpdateProjectRequestDTO;
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
    public Project saveProject(CreateProjectRequestDTO projectDto) {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());

        if (projectDto.getAllowedMembersIds() != null && !projectDto.getAllowedMembersIds().isEmpty()) {
            List<User> allowedUsers = userRepository.findAllById(projectDto.getAllowedMembersIds());
            project.setAllowedMembers(allowedUsers);
        }
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(UUID id, UpdateProjectRequestDTO projectDto) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            project.setName(projectDto.getName());
            project.setDescription(projectDto.getDescription());

            if (projectDto.getAllowedMembersIds() != null) {
                List<User> allowedUsers = userRepository.findAllById(projectDto.getAllowedMembersIds());
                project.setAllowedMembers(allowedUsers);
            }
            return projectRepository.save(project);
        }
        return null;
    }

    @Transactional
    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }

    public List<Project> getAllowedProjects(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        return projectRepository.findProjectsWhereMemberIsAllowed(user);

    }

}