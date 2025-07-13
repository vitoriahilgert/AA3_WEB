package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.config.Routes;
import br.ufscar.dc.dsw.AA2.dtos.project.*;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.services.ProjectService;
import br.ufscar.dc.dsw.AA2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<GetProjectResponseDTO> createProject(@RequestBody CreateProjectRequestDTO request) {
        GetProjectResponseDTO project = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

//    @GetMapping
//    public String listProjects(Model model) {
//        model.addAttribute("projectList", projectService.getAllProjects());
//        model.addAttribute("projectForm", new ProjectFormDTO());
//        model.addAttribute("allTesters", userService.getAllTesters());
//        return "projects";
//    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProjectResponseDTO> getProjectById(@PathVariable UUID id) {
        GetProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetProjectResponseDTO> updateProject(@PathVariable UUID id, @RequestBody UpdateProjectRequestDTO request) {
        GetProjectResponseDTO project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

//    @PostMapping
//    public ResponseEntity<Project> saveProject(@RequestBody ProjectFormDTO projectDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.saveProject(projectDto));
//    }
//
    // Inside your saveOrUpdateProject method in ProjectController

//    @PostMapping
//    public String saveOrUpdateProject(@ModelAttribute ProjectFormDTO projectForm, RedirectAttributes redirectAttributes) {
//        if (projectForm.getId() == null) {
//            // --- Manual Mapping for Creation ---
//            CreateProjectRequestDTO createDto = new CreateProjectRequestDTO(
//                    projectForm.getName(),
//                    projectForm.getDescription(),
//                    projectForm.getAllowedMembersIds()
//            );
//            projectService.saveProject(createDto);
//            redirectAttributes.addFlashAttribute("message", "Project created successfully!");
//        } else {
//            // --- Manual Mapping for Update ---
//            UpdateProjectRequestDTO updateDto = new UpdateProjectRequestDTO(
//                    projectForm.getName(),
//                    projectForm.getDescription(),
//                    projectForm.getAllowedMembersIds()
//            );
//            projectService.updateProject(projectForm.getId(), updateDto);
//            redirectAttributes.addFlashAttribute("message", "Project updated successfully!");
//        }
//        return "redirect:/projetos";
