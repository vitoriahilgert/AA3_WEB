package br.ufscar.dc.dsw.AA2.services;

import br.ufscar.dc.dsw.AA2.dtos.StrategyDTO;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.repositories.StrategyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StrategyService {

    private final StrategyRepository strategyRepository;

    public StrategyService(StrategyRepository strategyRepository) {
        this.strategyRepository = strategyRepository;
    }

    public List<Strategy> getAll() {
        return strategyRepository.findAll();
    }

    public void deleteById(UUID id) {
        strategyRepository.deleteById(id);
    }

    public Strategy insert(StrategyDTO strategyDTO) {
        Strategy strategy = new Strategy();
        strategy.setName(strategyDTO.name());
        strategy.setDescription(strategyDTO.description());
        strategy.setTips(strategyDTO.tips());
        strategy.setImages(strategyDTO.images());
        strategy.setExamples(strategyDTO.examples());

        return strategyRepository.save(strategy);
    }
}
