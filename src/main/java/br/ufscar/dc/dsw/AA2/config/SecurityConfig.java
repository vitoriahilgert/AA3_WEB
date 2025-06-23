package br.ufscar.dc.dsw.AA2.config;

import br.ufscar.dc.dsw.AA2.services.JPAUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_TESTER");
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy JPAUserDetailsService customUserDetailsService) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(Routes.ROOT, Routes.HOME, Routes.LOGIN, Routes.CSS, Routes.JS, Routes.IMAGES, Routes.STRATEGIES, Routes.UPLOADS).permitAll()
                        .requestMatchers(Routes.STRATEGIES + Routes.CREATE).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage(Routes.LOGIN)
                        .usernameParameter("email")
                        .defaultSuccessUrl(Routes.HOME, true)
                        .failureUrl(Routes.LOGIN + "?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(Routes.LOGOUT)
                        .logoutSuccessUrl(Routes.LOGIN + "?logout")
                ).userDetailsService(customUserDetailsService);
        return http.build();
    }
}