package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class TestDataInit {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public TestDataInit(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        final Role ROLE_USER = new Role("ROLE_USER");
        final Role ROLE_ADMIN = new Role("ROLE_ADMIN");
        userService.saveRole(new Role("ROLE_GUEST"));

        userService.saveRole(ROLE_USER);
        userService.saveRole(ROLE_ADMIN);


        userService.saveUser(new User("Жмышенко", "Валерий", "admin", passwordEncoder.encode("admin"),
                new HashSet<>(Set.of(ROLE_ADMIN))));

        userService.saveUser(new User("Аюбджони", "Рабджазода", "user",  passwordEncoder.encode("user"),
                new HashSet<>(Set.of(ROLE_USER))));
    }
}