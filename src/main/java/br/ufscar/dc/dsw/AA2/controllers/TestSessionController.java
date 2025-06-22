package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.StrategyDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.CreateTestSessionRequestDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.GetTestSessionResponseDTO;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.models.TestSession;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.services.ProjectService;
import br.ufscar.dc.dsw.AA2.services.StrategyService;
import br.ufscar.dc.dsw.AA2.services.TestSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sessoes")
public class TestSessionController {
    @Autowired
    private TestSessionService testSessionService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public String listSessions(ModelMap model) {
        List<GetTestSessionResponseDTO> sessionList = testSessionService.getAllTestSessions();

        List<Strategy> strategyList = strategyService.getAll();
        List<Project> projectList = projectService.getAllProjects();

        model.addAttribute("sessionList", sessionList);
        model.addAttribute("allProjects", projectList); // Para o dropdown no formulário
        model.addAttribute("allEstrategias", strategyList); // Para o dropdown no formulário

        return "test-sessions";
    }

    @PostMapping("/salvar")
    public String createSession(@RequestParam("projectId") UUID projectId,
                                @ModelAttribute CreateTestSessionRequestDTO dto,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {

        User user = (User) auth.getPrincipal();
        dto.setTesterId(user.getId());

        testSessionService.createTestSession(projectId, dto);
        redirectAttributes.addFlashAttribute("message", "Test session created successfully!");

        return "redirect:/sessoes";
    }

    @GetMapping("/detalhes")
    public String details(@RequestParam("id") UUID id, ModelMap model) {
        GetTestSessionResponseDTO dto = testSessionService.getTestSessionById(id);
        model.addAttribute("sessao", dto);
        return "test-session-details";
    }

    @PostMapping("/atualizar-status")
    public String updateSession(@RequestParam("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            testSessionService.updateSessionStatus(id);
            redirectAttributes.addFlashAttribute("success", "Sessão iniciada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao iniciar a sessão: " + e.getMessage());
        }

        return "redirect:/sessoes/detalhes?id=" + id;
    }

    @PostMapping("/atualizar-descricao")
    public String updateDescription(@RequestParam("id") UUID id,
                                    @RequestParam("new_description") String new_description,
                                    RedirectAttributes redirectAttributes) {
        try {
            GetTestSessionResponseDTO testSession = testSessionService.getTestSessionById(id);

            String currentDescription = testSession.getDescription();
            String updatedDescription;
            if (currentDescription == null || currentDescription.trim().isEmpty()) {
                updatedDescription = new_description;
            } else {
                updatedDescription = currentDescription + "\n" + new_description;
            }

            testSessionService.updateTestSessionDescription(id, updatedDescription);
            redirectAttributes.addFlashAttribute("success", "Bug adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar descrição!");
        }

        return "redirect:/sessoes/detalhes?id=" + id;
    }


//
//    @PostMapping("/{projectId}")
//    public ResponseEntity<CreateTestSessionResponseDTO> create(@RequestBody CreateTestSessionRequestDTO dto, @PathVariable("projectId") UUID projectId) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(testSessionService.createTestSession(projectId, dto));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
//        this.testSessionService.deleteTestSession(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<GetTestSessionResponseDTO>> getAll() {
//        return ResponseEntity.status(HttpStatus.OK).body(testSessionService.getAllTestSessions());
//    }
//
//    public ResponseEntity<GetTestSessionResponseDTO> getById(@PathVariable("id") UUID id) {
//        return ResponseEntity.ok().body(testSessionService.getTestSessionById(id));
//    }


}
