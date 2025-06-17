package br.ufscar.dc.dsw.AA2.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ProjectRecordDto(
        UUID id,
        String name,
        String description,
        LocalDateTime creationDateTime,
        Set<UUID> allowedMemberIds
) {
}
