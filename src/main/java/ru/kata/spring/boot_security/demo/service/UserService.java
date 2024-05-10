package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    public User findById(long id);
    public List<User> findAll();
    public User saveUser(User user);
    public void deleteByID(Long id);
    public List<Role> findAllRoles();
    public Role saveRole(Role role);
    public User findByUsername(String username);
    public Role findRoleByid(long id);
}
