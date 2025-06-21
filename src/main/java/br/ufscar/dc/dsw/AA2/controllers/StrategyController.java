package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.config.Routes;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(Routes.STRATEGIES)
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @GetMapping
    public String listStrategies(Model model) {
        List<Strategy> strategies = strategyService.getAll();
        model.addAttribute("strategies", strategies);
        return "strategies/strategies_list";
    }
}