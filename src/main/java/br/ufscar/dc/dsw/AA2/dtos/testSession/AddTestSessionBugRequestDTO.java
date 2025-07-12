package br.ufscar.dc.dsw.AA2.dtos.testSession;

public class AddTestSessionBugRequestDTO {
    private String bug;

    public AddTestSessionBugRequestDTO() {
    }

    public void setBug(String bug) {
        this.bug = bug;
    }

    public String getBug() {
        return bug;
    }
}
