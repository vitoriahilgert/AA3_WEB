package br.ufscar.dc.dsw.AA2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
@RequestMapping("/error")
public class ErrorViewController implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> map) {

        ModelAndView model = new ModelAndView("error");
        model.addObject("status", status.value());
        switch (status.value()) {
            case 403:
                model.addObject("error", "Acesso negado.");
                model.addObject("message", "Você não tem permissão para acessar essa página!");
                break;
            case 404:
                model.addObject("error", "Página não encontrada.");
                model.addObject("message", "A url para a página '" + map.get("path") + "' não existe.");
                break;
            case 500:
                model.addObject("error", "Ocorreu um erro interno no servidor.");
                model.addObject("message", "Ocorreu um erro inesperado, tente mais tarde.");
                break;
            default:
                model.addObject("error", map.get("error"));
                model.addObject("message", map.get("message"));
                break;
        }
        return model;
    }
}