package br.ufscar.dc.dsw.AA2.dtos.testSession;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateTestSessionRequestDTO {
    private int duration;
    private String description;
    private UUID testerId;
    private UUID strategyId;

    public CreateTestSessionRequestDTO() {}

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getTesterId() {
        return testerId;
    }

    public void setTesterId(UUID testerId) {
        this.testerId = testerId;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }
}
