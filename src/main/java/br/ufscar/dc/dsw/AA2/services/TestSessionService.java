package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.dtos.testSession.CreateTestSessionRequestDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.GetTestSessionResponseDTO;
import br.ufscar.dc.dsw.AA2.exceptions.ResourceNotFoundException;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.models.TestSession;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.TestSessionStatusEnum;
import br.ufscar.dc.dsw.AA2.repositories.ProjectRepository;
import br.ufscar.dc.dsw.AA2.repositories.StrategyRepository;
import br.ufscar.dc.dsw.AA2.repositories.TestSessionRepository;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public void updateSessionStatus(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));
        if (testSession.getStatus().equals(TestSessionStatusEnum.CREATED)) {
            testSession.setStatus(TestSessionStatusEnum.IN_PROGRESS);
            testSession.setStartDateTime(LocalDateTime.now());
        } else if (testSession.getStatus().equals(TestSessionStatusEnum.IN_PROGRESS)) {
            testSession.setFinishDateTime(LocalDateTime.now());
            testSession.setStatus(TestSessionStatusEnum.FINISHED);
        }

        testSessionRepository.save(testSession);
        new GetTestSessionResponseDTO(testSession);
    }

    public void updateTestSessionDescription(UUID sessionId, String description) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("TestSession", "id", sessionId.toString()));

        testSession.setDescription(description);
        testSessionRepository.save(testSession);
    }

}
