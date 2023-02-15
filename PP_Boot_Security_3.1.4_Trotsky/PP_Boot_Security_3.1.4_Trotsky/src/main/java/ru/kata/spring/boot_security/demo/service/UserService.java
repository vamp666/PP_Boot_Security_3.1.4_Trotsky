package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void update(Long id, User user) {
        User userToUpdate = userRepository.getById(id);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setAge(user.getAge());
        userToUpdate.setRoles(user.getRoles());
        if (user.getPassword() != null) {
            userToUpdate.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userToUpdate);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public void setInitData() {
        if (userRepository.count() > 0) {
            return;
        }
        Role userRole = new Role("ROLE_USER");
        Role adminRole = new Role("ROLE_ADMIN");
        userRepository.save(new User("user", "user", (byte) 18, "user@gmail.com", bCryptPasswordEncoder.encode("user"), new HashSet<Role>() {{
            add(userRole);
        }}));
        userRepository.save(new User("admin", "admin", (byte) 1, "admin@gmail.com", bCryptPasswordEncoder.encode("admin"), new HashSet<Role>() {{
            add(userRole);
            add(adminRole);
        }}));
    }
}