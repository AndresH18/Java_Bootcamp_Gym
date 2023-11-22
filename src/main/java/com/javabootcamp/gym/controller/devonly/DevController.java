package com.javabootcamp.gym.controller.devonly;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/dev/")
public class DevController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    @Autowired
    public DevController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("update-all-user-passwords-using-encoder")
    public void updateAllUserPasswordsUsingEncoder() {
        var users = userRepository.findAll();

        var passwordEncoder = new BCryptPasswordEncoder();

        for (User user : users) {
            var password = user.getPassword();
            var passwordHash = passwordEncoder.encode(password);
            logger.error("Password: {}; \tPasswordHash: {}", password, passwordHash);
            user.setPassword(passwordHash);
        }
        userRepository.saveAll(users);

    }
}
