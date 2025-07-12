package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.config.JwtService;
import br.ufscar.dc.dsw.AA2.dtos.testSession.*;
import br.ufscar.dc.dsw.AA2.exceptions.BadRequestException;
import br.ufscar.dc.dsw.AA2.exceptions.ResourceNotFoundException;
import br.ufscar.dc.dsw.AA2.exceptions.UnauthorizedExeption;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.models.TestSession;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.TestSessionStatusEnum;
import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;
import br.ufscar.dc.dsw.AA2.repositories.ProjectRepository;
import br.ufscar.dc.dsw.AA2.repositories.StrategyRepository;
import br.ufscar.dc.dsw.AA2.repositories.TestSessionRepository;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TestSessionService {
    @Autowired
    private TestSessionRepository testSessionRepository;

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private JwtService jwtService;

    public GetTestSessionResponseDTO createTestSession(UUID projectId, CreateTestSessionRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId.toString()));

        Strategy strategy = strategyRepository.findById(dto.getStrategyId())
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", dto.getStrategyId().toString()));

        User tester = userRepository.findById(dto.getTesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Tester", "id", dto.getTesterId().toString()));

        checkIfUserIsAllowedOnProject(projectId, tester);

        TestSession testSession = new TestSession();
        testSession.setDuration(dto.getDuration());
        testSession.setProject(project);
        testSession.setTester(tester);
        testSession.setStrategy(strategy);
        testSession.setDescription(dto.getDescription());

        testSessionRepository.save(testSession);

        return new GetTestSessionResponseDTO(testSession);
    }

    public GetTestSessionResponseDTO getTestSessionById(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));
        return new GetTestSessionResponseDTO(testSession);
    }

    public List<GetTestSessionResponseDTO> getAllowedTestSessionsByToken(String token) {
        User user = jwtService.getUserFromToken(token);

        List<TestSession> testSessions;

        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            testSessions = testSessionRepository.findAll();
        } else {
            testSessions = testSessionRepository.findByTester(user);
        }

        return testSessions.stream().map(GetTestSessionResponseDTO::new).collect(Collectors.toList());
    }

    public void deleteTestSession(String token, UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        User user = jwtService.getUserFromToken(token);
        checkIfUserIsAllowedOnSession(sessionId, user);

        testSessionRepository.delete(testSession);
    }

    public GetTestSessionResponseDTO updateSession(String token, UUID sessionId, UpdateSessionRequestDTO dto) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        if (!testSession.getStatus().equals(TestSessionStatusEnum.CREATED)) {
            throw new BadRequestException("A sessão de teste só pode ser editada antes de ser inicializada.");
        }

        User user = jwtService.getUserFromToken(token);
        checkIfUserIsAllowedOnSession(sessionId, user);

        testSession.setDuration(dto.getDuration());
        testSession.setDescription(dto.getDescription());
        testSession.setProject(testSession.getProject());
        testSession.setStrategy(testSession.getStrategy());

        testSessionRepository.save(testSession);
        return new GetTestSessionResponseDTO(testSession);
    }

    public GetTestSessionResponseDTO updateSessionStatus(String token, UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        User user = jwtService.getUserFromToken(token);
        checkIfUserIsAllowedOnSession(sessionId, user);

        if (testSession.getStatus().equals(TestSessionStatusEnum.CREATED)) {
            testSession.setStatus(TestSessionStatusEnum.IN_PROGRESS);
            testSession.setStartDateTime(LocalDateTime.now());

            LocalDateTime endTime = LocalDateTime.now().plusMinutes(testSession.getDuration());
            testSession.setFinishDateTime(endTime);

            taskScheduler.schedule(() -> finishTestSession(testSession.getId()), endTime.atZone(ZoneId.of("America/Sao_Paulo")).toInstant());

        } else if (testSession.getStatus().equals(TestSessionStatusEnum.IN_PROGRESS)) {
            testSession.setFinishDateTime(LocalDateTime.now());
            testSession.setStatus(TestSessionStatusEnum.FINISHED);
        } else {
            throw new BadRequestException("A sessão de teste já foi finalizada.");
        }

        testSessionRepository.save(testSession);
        return new GetTestSessionResponseDTO(testSession);
    }

    public AddTestSessionBugResponseDTO addTestSessionBugs(String token, UUID sessionId, AddTestSessionBugRequestDTO dto) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        if (!testSession.getStatus().equals(TestSessionStatusEnum.CREATED)) {
            throw new BadRequestException("Os bugs só podem ser registrados enquanto a sessão de teste está em progresso.");
        }

        User user = jwtService.getUserFromToken(token);
        checkIfUserIsAllowedOnSession(sessionId, user);

        String updatedBugs = testSession.getBugs() + "/n" + dto.getBug();

        testSession.setBugs(updatedBugs);
        testSessionRepository.save(testSession);
        return new AddTestSessionBugResponseDTO(testSession);
    }

    private void finishTestSession(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        testSession.setStatus(TestSessionStatusEnum.FINISHED);
        testSession.setFinishDateTime(LocalDateTime.now());

        testSessionRepository.save(testSession);

        System.out.println("Sessão de teste de id igual a " + sessionId + " finalizada pelo taskScheduler");
    }

    private void checkIfUserIsAllowedOnProject(UUID projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId.toString()));

        if (user.getRole().equals(UserRoleEnum.TESTER)) {
            if (!project.getAllowedMembers().contains(user)) {
                throw new UnauthorizedExeption("O testador não é um membro autorizado do projeto.");
            }
        }
    }

    private void checkIfUserIsAllowedOnSession(UUID sessionId, User user) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        if (user.getRole().equals(UserRoleEnum.TESTER)) {
            if (!testSession.getTester().getId().equals(user.getId())) {
                throw new UnauthorizedExeption("O testador não é o dono da sessão de teste.");
            }
        }
    }
}
