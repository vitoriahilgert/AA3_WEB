package br.ufscar.dc.dsw.AA2.dtos;

import br.ufscar.dc.dsw.AA2.models.Image;

import java.util.List;

public record StrategyDTO(
        String name,
        String description,
        String examples,
        String tips,
        List<Image> images
) {
}