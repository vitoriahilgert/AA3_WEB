package br.ufscar.dc.dsw.AA2.dtos;

import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;

import java.util.Set;
import java.util.UUID;

public record UserRecordDto(
        String name,
        String email,
        String password,
        UserRoleEnum role,
        Set<UUID> projectIds,
        Set<UUID> testSessionIds
) {
}
