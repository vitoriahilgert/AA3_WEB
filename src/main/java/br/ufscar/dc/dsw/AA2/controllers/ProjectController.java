package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.ProjectRecordDto;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID id) {
        Optional<Project> projectOptional = projectService.getProjectById(id);

        return projectOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody ProjectRecordDto projectDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.saveProject(projectDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody ProjectRecordDto projectDto) {
        Project updatedProject = projectService.updateProject(id, projectDto);
        if (updatedProject != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body("Projeto com id " + id + " foi deletado com sucesso.");
    }
}
