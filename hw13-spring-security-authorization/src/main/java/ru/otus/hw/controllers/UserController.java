package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dtos.userdtos.UserDto;
import ru.otus.hw.dtos.userdtos.UserLstDto;
import ru.otus.hw.dtos.userdtos.UserRegistrationDto;
import ru.otus.hw.models.UserRole;
import ru.otus.hw.services.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();

        model.addAttribute("users", users);
        model.addAttribute("userListDto", new UserLstDto(users));

        return "user/list";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getUserById(@PathVariable Long id, Model model) {
        UserDto user = userService.getById(id);
        model.addAttribute("user", user);

        return "user/profile";
    }

    @GetMapping("/user/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showCreateUserForm(Model model) {
        model.addAttribute("registrationDto", new UserRegistrationDto("", "", UserRole.USER));

        return "user/create-form";
    }

    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String createUser(@ModelAttribute @Valid UserRegistrationDto registrationDto,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/create-form";
        }

        userService.createUser(registrationDto);

        return "redirect:/user?success";

    }
}
