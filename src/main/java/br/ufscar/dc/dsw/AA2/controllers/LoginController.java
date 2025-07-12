package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.config.Routes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping(Routes.ROOT)
    public String login() {
        return "login";
    }
}
