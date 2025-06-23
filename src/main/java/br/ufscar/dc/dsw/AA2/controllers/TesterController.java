package br.ufscar.dc.dsw.AA2.controllers;

import br.ufscar.dc.dsw.AA2.dtos.UserRecordDto;
import br.ufscar.dc.dsw.AA2.models.User;
import br.ufscar.dc.dsw.AA2.models.enums.UserRoleEnum;
import br.ufscar.dc.dsw.AA2.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
public class TesterController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/testers")
    public String handleTesterActions(Model model) {
        List<User> testerUsers = userService.findByRole(UserRoleEnum.TESTER);

        model.addAttribute("testersList", testerUsers);

        return "testers";
    }


    @PostMapping("/users/testers")
    public String handleTesterActions(@RequestParam String action,
                                     @RequestParam(name = "id", required = false) UUID id,
                                     @RequestParam(name = "name", required = false) String name,
                                     @RequestParam(name = "email", required = false) String email,
                                     @RequestParam(name = "password", required = false) String password,
                                     Authentication authentication,
                                     HttpServletRequest request,
                                     HttpServletResponse response ) {

        switch (action) {
            case "create":
                UserRecordDto userDto = new UserRecordDto(name, email, password, UserRoleEnum.TESTER, null, null);

                userService.saveUser(userDto);
                break;

            case "update":
                User currentUser = userService.getUserById(id);
                if (currentUser != null) {
                    UserRecordDto userUpdateDto = new UserRecordDto(
                            name,
                            email,
                            password,
                            currentUser.getRole(),
                            null,
                            null
                    );

                    userService.updateUser(id, userUpdateDto);
                }
                break;

            case "delete":
                userService.deleteUser(id);
                break;
        }


        return "redirect:/admin/users/testers";
    }
}