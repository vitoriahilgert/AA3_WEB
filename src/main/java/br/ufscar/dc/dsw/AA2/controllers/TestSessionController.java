package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.StrategyDTO;
import br.ufscar.dc.dsw.AA2.config.Routes;
import br.ufscar.dc.dsw.AA2.dtos.testSession.CreateTestSessionRequestDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.GetTestSessionResponseDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.UpdateSessionRequestDTO;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.models.TestSession;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.TestSessionStatusEnum;
import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;
import br.ufscar.dc.dsw.AA2.repositories.ProjectRepository;
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
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(Routes.SESSIONS)
public class TestSessionController {
    @Autowired
    private TestSessionService testSessionService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public String listSessions(ModelMap model, Authentication auth, @RequestParam("projetoId") Optional<UUID> projetoIdFiltro) {
        User user = (User) auth.getPrincipal();

        List<GetTestSessionResponseDTO> sessionList;

        List<Strategy> strategyList = strategyService.getAll();

        List<Project> projectList = (user.getRole().equals(UserRoleEnum.ADMIN)) ? projectService.getAllProjects() :
                projectService.getAllowedProjects(user.getId());

        if (projetoIdFiltro.isPresent()) {
            sessionList = testSessionService.findAllByProjectId(projetoIdFiltro.get(), user);
        } else {
            sessionList = (user.getRole().equals(UserRoleEnum.ADMIN)) ? testSessionService.getAllTestSessions() :
                    testSessionService.getAllowedTestSessions(user);
        }

        model.addAttribute("sessionList", sessionList);
        model.addAttribute("allProjects", projectList);
        model.addAttribute("allEstrategias", strategyList);
        model.addAttribute("filtroAplicado", projetoIdFiltro.orElse(null));

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
        redirectAttributes.addFlashAttribute("message", "Sessão de teste criada com sucesso!");

        return "redirect:/sessions";
    }

    @PostMapping("/atualizar")
    public String updateSession(@ModelAttribute UpdateSessionRequestDTO dto,
                                RedirectAttributes redirectAttributes) {


        try {
            testSessionService.updateSession(dto.getId(), dto);
            System.out.println("Atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar sessão!");
        }

        return "redirect:/sessions";
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
            GetTestSessionResponseDTO testSession = testSessionService.getTestSessionById(id);
            if (testSession.getStatus().equals(TestSessionStatusEnum.IN_PROGRESS)) {
                redirectAttributes.addFlashAttribute("success", "Sessão iniciada com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Sessão finalizada com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao iniciar a sessão: " + e.getMessage());
        }

        return "redirect:/sessions/detalhes?id=" + id;
    }

    @PostMapping("/atualizar-bugs")
    public String updateBugs(@RequestParam("id") UUID id,
                                    @RequestParam("new_bug") String new_bug,
                                    RedirectAttributes redirectAttributes) {
        try {
            GetTestSessionResponseDTO testSession = testSessionService.getTestSessionById(id);

            String currentBugs = testSession.getBugs();
            String updatedBugs;
            if (currentBugs == null || currentBugs.trim().isEmpty()) {
                updatedBugs = new_bug;
            } else {
                updatedBugs = currentBugs + "\n" + new_bug;
            }

            testSessionService.updateTestSessionBugs(id, updatedBugs);
            redirectAttributes.addFlashAttribute("success", "Bug adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar bugs!");
        }

        return "redirect:/sessions/detalhes?id=" + id;
    }

    @PostMapping("/deletar")
    public String deleteSession(@RequestParam("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            testSessionService.deleteTestSession(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar sessão de teste.");
        }
        return "redirect:/sessions";
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
