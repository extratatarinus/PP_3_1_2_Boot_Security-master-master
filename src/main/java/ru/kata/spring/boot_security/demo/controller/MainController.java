package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class MainController {

    private final UserServiceImpl userDao;
    private final PasswordEncoder passwordEncoder;

    public MainController(UserServiceImpl userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String listUsers(Model model, Principal principal) {
        List<User> users = userDao.findAll();
        List<Role> roles = userDao.findAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("newUser", new User());
        model.addAttribute("principal", principal.getName());
        return "admin";
    }


    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal) {
        User currentUser = userDao.findByUsername(principal.getName());
        if (currentUser.getId().equals(id)) {
            return "redirect:/admin?error=cannotDeleteSelf";
        }
        userDao.deleteByID(id);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String addUser(Model model, @ModelAttribute("user") User user, @RequestParam(required = false) List<Long> rolesIds) {
        if ("POST".equals(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getMethod())) {
            Set<Role> roles = new HashSet<>();
            if (rolesIds != null) {
                for (Long roleId : rolesIds) {
                    Role role = new Role();
                    role.setId(roleId);
                    roles.add(role);
                }
            }
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.saveUser(user);
            return "redirect:/admin";
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("roles", userDao.findAllRoles());
            return "add-user";
        }
    }

    @RequestMapping(value = "/admin/edit/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String editUser(@PathVariable Long id, Model model, @ModelAttribute("user") User user, @RequestParam(required = false) List<Long> rolesIds) {
        if ("POST".equals(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getMethod())) {
            Set<Role> roles = new HashSet<>();
            if (rolesIds != null) {
                for (Long roleId : rolesIds) {
                    Role role = new Role();
                    role.setId(roleId);
                    roles.add(role);
                }
            }
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.saveUser(user);
            return "redirect:/admin";
        } else {
            user = userDao.findById(id);
            model.addAttribute("user", user);
            model.addAttribute("roles", userDao.findAllRoles());
            return "edit-user";
        }
    }


}
