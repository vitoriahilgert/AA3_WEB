package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.UserRecordDto;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;
import br.ufscar.dc.dsw.AA2.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/admins")
    public String showManageAdminsPage(Model model) {
        List<User> adminUsers = userService.findByRole(UserRoleEnum.ADMIN);

        model.addAttribute("adminsList", adminUsers);

        return "admins";
    }


    @PostMapping("/users/admins")
    public String handleAdminActions(@RequestParam String action,
                                     @RequestParam(name = "id", required = false) UUID id,
                                     @RequestParam(name = "name", required = false) String name,
                                     @RequestParam(name = "email", required = false) String email,
                                     @RequestParam(name = "password", required = false) String password,
                                     Authentication authentication,
                                     HttpServletRequest request,
                                     HttpServletResponse response ) {

        switch (action) {
            case "create":
                UserRecordDto userDto = new UserRecordDto(name, email, password, UserRoleEnum.ADMIN, null, null);

                userService.saveUser(userDto);
                break;

            case "update":
                User userAtual = userService.getUserById(id);
                if (userAtual != null) {
                    UserRecordDto userUpdateDto = new UserRecordDto(
                            name,
                            email,
                            password,
                            userAtual.getRole(),
                            null,
                            null
                    );

                    userService.updateUser(id, userUpdateDto);
                }
                break;

            case "delete":
                User loggedInUser = (User) authentication.getPrincipal();

                if (loggedInUser.getId().equals(id)) {

                    userService.deleteUser(id);
                    new SecurityContextLogoutHandler().logout(request, response, authentication);

                    return "redirect:/login?logout&self_deleted";
                } else {
                    userService.deleteUser(id);
                }
                break;
        }


        return "redirect:/admin/users/admins";
    }
}