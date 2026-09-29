package com.example.TodoProject.controller;
import com.example.TodoProject.Models.User;
import com.example.TodoProject.SecurityConfig;
import com.example.TodoProject.repository.UserRepository;
import com.example.TodoProject.service.UserService;
import com.example.TodoProject.utils.JwtUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final GenericResponseService responseBuilder;
    private UserRepository userRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String password = body.get("password");

        if(userRepository.findByEmail(email).isPresent()){
            return new ResponseEntity<>("Email allready present",HttpStatus.CONFLICT);
        }

        userService.createUser(User.builder().email(email).password(password).build());
        return new ResponseEntity<>("Successfully registered", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String password = body.get("password");

        var userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return new ResponseEntity<>("User Not Registerd",HttpStatus.UNAUTHORIZED);
        }

        User user = userOptional.get();
        if(!passwordEncoder.matches(password,user.getPassword())){
            return new ResponseEntity<>("Invalid User",HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtils.generateToken(email);
        return ResponseEntity.ok(Map.of("token",token));
    }
}
