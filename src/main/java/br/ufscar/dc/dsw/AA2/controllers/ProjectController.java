package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.project.ProjectFormDTO;
import br.ufscar.dc.dsw.AA2.dtos.project.CreateProjectRequestDTO;
import br.ufscar.dc.dsw.AA2.dtos.project.UpdateProjectRequestDTO;
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

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projectList", projectService.getAllProjects());
        model.addAttribute("projectForm", new ProjectFormDTO());
        model.addAttribute("allTesters", userService.getAllTesters());
        return "projects";
    }

    @GetMapping("/{id}")
    public String getProjectById(@PathVariable UUID id, ModelMap model) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project != null) {
            model.addAttribute("project", project);
            return "project-details";
        }
        // Handle case where project is not found, e.g., redirect to project list or an error page
        return "redirect:/projetos";
    }

//    @PostMapping
//    public ResponseEntity<Project> saveProject(@RequestBody ProjectFormDTO projectDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.saveProject(projectDto));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody ProjectRecordDto projectDto) {
//        Project updatedProject = projectService.updateProject(id, projectDto);
//        if (updatedProject != null) {
//            return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

    // Inside your saveOrUpdateProject method in ProjectController

    @PostMapping
    public String saveOrUpdateProject(@ModelAttribute ProjectFormDTO projectForm, RedirectAttributes redirectAttributes) {
        if (projectForm.getId() == null) {
            // --- Manual Mapping for Creation ---
            CreateProjectRequestDTO createDto = new CreateProjectRequestDTO(
                    projectForm.getName(),
                    projectForm.getDescription(),
                    projectForm.getAllowedMembersIds()
            );
            projectService.saveProject(createDto);
            redirectAttributes.addFlashAttribute("message", "Project created successfully!");
        } else {
            // --- Manual Mapping for Update ---
            UpdateProjectRequestDTO updateDto = new UpdateProjectRequestDTO(
                    projectForm.getName(),
                    projectForm.getDescription(),
                    projectForm.getAllowedMembersIds()
            );
            projectService.updateProject(projectForm.getId(), updateDto);
            redirectAttributes.addFlashAttribute("message", "Project updated successfully!");
        }
        return "redirect:/projetos";
    }

    @GetMapping(params = "acao=deletar")
    public String deleteProject(@RequestParam("id") UUID id,
                                @RequestParam("acao") String acao,
                                RedirectAttributes redirectAttributes) {
        if ("deletar".equals(acao)) {
            projectService.deleteProject(id);
            redirectAttributes.addFlashAttribute("message", "Project with ID " + id + " deleted successfully!");
        }
        return "redirect:/projetos";
    }
}
