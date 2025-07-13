package br.ufscar.dc.dsw.AA2.dtos.testSession;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateTestSessionRequestDTO {
    private int duration;
    private UUID strategyId;
    private String description;

    public CreateTestSessionRequestDTO() {}

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
