package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServiceImpl userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String userProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userDao.findByUsername(username);
        model.addAttribute("user", user);
        return "user-profile";
    }

    @PostMapping("/update")
    public String updateUser(User userForm, String password) {
        User user = userDao.findByUsername(userForm.getUsername());
        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setName(userForm.getName());
        user.setLastName(userForm.getLastName());
        userDao.saveUser(user);
        return "redirect:/user";
    }
}
