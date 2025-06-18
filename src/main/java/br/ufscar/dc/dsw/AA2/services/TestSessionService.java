package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.dtos.testSession.CreateTestSessionRequestDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.CreateTestSessionResponseDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.GetTestSessionResponseDTO;
import br.ufscar.dc.dsw.AA2.dtos.testSession.GetTestSessionsResponseDTO;
import br.ufscar.dc.dsw.AA2.models.Project;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.models.TestSession;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.repositories.ProjectRepository;
import br.ufscar.dc.dsw.AA2.repositories.StrategyRepository;
import br.ufscar.dc.dsw.AA2.repositories.TestSessionRepository;
import br.ufscar.dc.dsw.AA2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public TestSessionService(TestSessionRepository testSessionRepository, StrategyRepository strategyRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.testSessionRepository = testSessionRepository;
        this.strategyRepository = strategyRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // TODO: criar um ExceptionHandler
    public CreateTestSessionResponseDTO createTestSession(UUID projectId, CreateTestSessionRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Strategy strategy = strategyRepository.findById(dto.getStrategyId())
                .orElseThrow(() -> new RuntimeException("Strategy not found"));

        User tester = userRepository.findById(dto.getTesterId())
                .orElseThrow(() -> new RuntimeException("Tester not found"));

        TestSession testSession = new TestSession();
        testSession.setDescription(dto.getDescription());
        testSession.setDuration(dto.getDuration());

        testSessionRepository.save(testSession);

        return new CreateTestSessionResponseDTO(testSession);
    }

    public void deleteTestSession(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("TestSession not found"));

        testSessionRepository.delete(testSession);
    }

    public GetTestSessionResponseDTO getTestSessionById(UUID sessionId) {
        TestSession testSession = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("TestSession not found"));
        return new GetTestSessionResponseDTO(testSession);
    }

    public List<GetTestSessionResponseDTO> getAllTestSessions() {
        List<TestSession> testSessions = testSessionRepository.findAll();
        return testSessions.stream().map(GetTestSessionResponseDTO::new).collect(Collectors.toList());
    }

}
