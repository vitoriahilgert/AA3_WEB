package br.ufscar.dc.dsw.AA2.dtos.project;

import java.util.List;
import java.util.UUID;

public class ProjectFormDTO {
    private UUID id;
    private String name;
    private String description;
    private List<UUID> allowedMembersIds;

    public ProjectFormDTO() {
    }

    public ProjectFormDTO(UUID id, String name, String description, List<UUID> allowedMembersIds) {
        this.name = name;
        this.description = description;
        this.allowedMembersIds = allowedMembersIds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UUID> getAllowedMembersIds() {
        return allowedMembersIds;
    }

    public void setAllowedMembersIds(List<UUID> allowedMembers) {
        this.allowedMembersIds = allowedMembers;
    }
}


