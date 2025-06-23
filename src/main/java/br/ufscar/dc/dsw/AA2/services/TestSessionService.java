package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.dtos.testSession.CreateTestSessionRequestDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.GetTestSessionResponseDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.UpdateSessionRequestDTO;
import br.ufscar.dc.dsw.AA2.exceptions.ResourceNotFoundException;
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

import java.time.Instant;
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

    public GetTestSessionResponseDTO createTestSession(UUID projectId, CreateTestSessionRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId.toString()));

        Strategy strategy = strategyRepository.findById(dto.getStrategyId())
                .orElseThrow(() -> new ResourceNotFoundException("Strategy", "id", dto.getStrategyId().toString()));

        User tester = userRepository.findById(dto.getTesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Tester", "id", dto.getTesterId().toString()));

        TestSession testSession = new TestSession();
        testSession.setDuration(dto.getDuration());
        testSession.setProject(project);
        testSession.setTester(tester);
        testSession.setStrategy(strategy);
        testSession.setDescription(dto.getDescription());

        testSessionRepository.save(testSession);

        return new GetTestSessionResponseDTO(testSession);
    }

    public void deleteTestSession(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        testSessionRepository.delete(testSession);
    }

    public GetTestSessionResponseDTO getTestSessionById(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));
        return new GetTestSessionResponseDTO(testSession);
    }

    public List<GetTestSessionResponseDTO> getAllTestSessions() {
        List<TestSession> testSessions = testSessionRepository.findAll();
        return testSessions.stream().map(GetTestSessionResponseDTO::new).collect(Collectors.toList());
    }

    public void updateSession(UUID sessionId, UpdateSessionRequestDTO dto) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        testSession.setDuration(dto.getDuration());
        testSession.setDescription(dto.getDescription());
        testSession.setProject(testSession.getProject());
        testSession.setStrategy(testSession.getStrategy());

        testSessionRepository.save(testSession);
    }

    public void updateSessionStatus(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));
        if (testSession.getStatus().equals(TestSessionStatusEnum.CREATED)) {
            testSession.setStatus(TestSessionStatusEnum.IN_PROGRESS);
            testSession.setStartDateTime(LocalDateTime.now());

            LocalDateTime endTime = LocalDateTime.now().plusMinutes(testSession.getDuration());
            testSession.setFinishDateTime(endTime);

            taskScheduler.schedule(() -> finishTestSession(testSession.getId()), endTime.atZone(ZoneId.of("America/Sao_Paulo")).toInstant());

        } else if (testSession.getStatus().equals(TestSessionStatusEnum.IN_PROGRESS)) {
            testSession.setFinishDateTime(LocalDateTime.now());
            testSession.setStatus(TestSessionStatusEnum.FINISHED);
        }

        testSessionRepository.save(testSession);
        new GetTestSessionResponseDTO(testSession);
    }

    public void updateTestSessionBugs(UUID sessionId, String bugs) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        testSession.setBugs(bugs);
        testSessionRepository.save(testSession);
    }

    public List<GetTestSessionResponseDTO> getAllowedTestSessions(User user) {
        List<TestSession> testSessions = testSessionRepository.findByTester(user);
        return testSessions.stream().map(GetTestSessionResponseDTO::new).collect(Collectors.toList());
    }

    private void finishTestSession(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        testSession.setStatus(TestSessionStatusEnum.FINISHED);
        testSession.setFinishDateTime(LocalDateTime.now());

        testSessionRepository.save(testSession);

        System.out.println("Sessão de teste de id igual a " + sessionId + " finalizada pelo taskScheduler");
    }

    public List<GetTestSessionResponseDTO> findAllByProjectId(UUID id, User user) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id.toString()));

        List<TestSession> testSessions = (user.getRole().equals(UserRoleEnum.ADMIN)) ?
                testSessionRepository.findAllByProject(project) :
                testSessionRepository.findAllByProjectAndTester(project, user);

        return testSessions.stream().map(GetTestSessionResponseDTO::new).collect(Collectors.toList());
    }
}
