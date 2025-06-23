package br.ufscar.dc.dsw.AA2.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserRoleInterceptor implements HandlerInterceptor {

    public UserRoleInterceptor() {}

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() != "anonymousUser") {
                String role = getUserRole();
                modelAndView.addObject("role", role);
                modelAndView.addObject("user", auth.getPrincipal());
            }
        }
    }

    public String getUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = "UNLOGGED";

        if (auth != null && auth.isAuthenticated() && auth.getAuthorities() != null) {
            if (auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                role = "ADMIN";
            }

            else if (auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_TESTER"))) {
                role = "TESTER";
            }
        }

        return role;
    }
}
