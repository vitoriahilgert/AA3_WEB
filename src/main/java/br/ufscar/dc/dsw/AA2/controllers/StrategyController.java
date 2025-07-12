package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.config.Routes;
import br.ufscar.dc.dsw.AA2.dtos.StrategyDTO;
import br.ufscar.dc.dsw.AA2.models.Strategy;
import br.ufscar.dc.dsw.AA2.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

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

    @GetMapping(Routes.CREATE)
    public String createStrategyGet(Model model) {
        return "strategies/create_strategy";
    }

    @PostMapping(Routes.CREATE)
    public String createStrategyPost(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("examples") String examples,
            @RequestParam("tips") String tips,
            @RequestParam("images") List<MultipartFile> imagesFiles,
            RedirectAttributes redirectAttributes
    ) {
        try {
            StrategyDTO strategyDTO = new StrategyDTO(name, description, examples, tips, null);

            strategyService.insert(strategyDTO, imagesFiles);

            redirectAttributes.addFlashAttribute("successMessage", "Estratégia criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar a estratégia!" + e.getMessage());
        }
        return "redirect:" + Routes.STRATEGIES;
    }

    @PostMapping(Routes.DELETE)
    public String deleteStrategy(
            @RequestParam("id") UUID id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            strategyService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estratégia excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir a estratégia.");
        }
        return "redirect:" + Routes.STRATEGIES;
    }
}