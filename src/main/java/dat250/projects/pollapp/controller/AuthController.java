package dat250.projects.pollapp.controller;

import dat250.projects.pollapp.repository.UserRepository;
import dat250.projects.pollapp.service.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    @Transactional
    public String login(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findByUsernameAndPassword(username, password).isPresent()) {
            return jwtService.generateToken(username);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
