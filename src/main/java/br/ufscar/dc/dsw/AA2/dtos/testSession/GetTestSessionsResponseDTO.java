package br.ufscar.dc.dsw.AA2.dtos.testSession;

import br.ufscar.dc.dsw.AA2.models.TestSession;

import java.util.ArrayList;
import java.util.List;

public class GetTestSessionsResponseDTO {
    private List<GetTestSessionResponseDTO> testSessions = new ArrayList<>();

    public GetTestSessionsResponseDTO(List<TestSession> sessions) {
        sessions.forEach(testSession -> {
            this.testSessions.add(new GetTestSessionResponseDTO(testSession));
        });
    }

    public List<GetTestSessionResponseDTO> getTestSessions() {
        return testSessions;
    }

    public void setTestSessions(List<GetTestSessionResponseDTO> testSessions) {
        this.testSessions = testSessions;
    }
}
