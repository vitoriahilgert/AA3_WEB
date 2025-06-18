package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.StrategyDTO;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/strategy")
public class StrategyController {
    @Autowired
    private StrategyService strategyService;

    @GetMapping
    public ResponseEntity<List<Strategy>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(strategyService.getAll());
    }

    @PostMapping
    public ResponseEntity<Strategy> insert(@RequestBody StrategyDTO strategyDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(strategyService.insert(strategyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        strategyService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

}