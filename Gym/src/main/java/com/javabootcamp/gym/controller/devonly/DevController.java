package com.javabootcamp.gym.controller.devonly;

import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.UserRepository;
import com.javabootcamp.gym.messaging.TrainingMessage;
import com.javabootcamp.gym.messaging.report.ReportingServiceAsync;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(DevController.class);
    private final UserRepository userRepository;

    private final ReportingServiceAsync reportingServiceAsync;

    @Autowired
    public DevController(UserRepository userRepository, ReportingServiceAsync reportingServiceAsync) {
        this.userRepository = userRepository;
        this.reportingServiceAsync = reportingServiceAsync;
    }

    @GetMapping("update-all-user-passwords-using-encoder")
    public void updateAllUserPasswordsUsingEncoder() {
        var users = userRepository.findAll();

        var passwordEncoder = new BCryptPasswordEncoder();

        for (User user : users) {
            var password = user.getPassword();
            var passwordHash = passwordEncoder.encode(password);
            LOGGER.error("Password: {}; \tPasswordHash: {}", password, passwordHash);
            user.setPassword(passwordHash);
        }
        userRepository.saveAll(users);
    }

    @GetMapping("send-message-to-aws-sqs")
    public void sendMessageToAwsSqs() {

        var messages = new TrainingMessage[]{
//                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 1, "2020", "JUN", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 2, "2020", "JUN", false),
//                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 3, "2020", "JUN", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 5, "2020", "JUL", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 5, "2020", "JUL", true),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 6, "2020", "JUL", false),
//                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 1, "2020", "OCT", false),
//                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 2, "2020", "OCT", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 2, "2020", "OCT", true),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 4, "2020", "OCT", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 4, "2020", "OCT", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 1, "2020", "DIC", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 3, "2023", "JUN", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 10, "2023", "SEP", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 5, "2023", "DIC", false),
////                new TrainingMessage("andres.hoyos", "Andres", "Hoyos", true, 5, "2023", "DIC", true),
//
                new TrainingMessage("david.velasquez", "David", "Velasquez", false, 10, "2022", "JAN", false),
                new TrainingMessage("david.velasquez", "David", "Velasquez", false, 5, "2022", "DEC", false),
                new TrainingMessage("david.velasquez", "David", "Velasquez", false, 5, "2023", "DEC", false),
//                new TrainingMessage("david.velasquez", "David", "Velasquez", false, 5, "2023", "DEC", true),
//                new TrainingMessage("david.velasquez", "David", "Velasquez", false, 1, "2023", "DEC", true)
        };

        for (TrainingMessage message : messages) {
            reportingServiceAsync.sendMessage(message);
        }
    }
}
