package com.example.authjwtdemo.controller;

import com.example.authjwtdemo.dto.LoginDto;
import com.example.authjwtdemo.dto.LoginResponse;
import com.example.authjwtdemo.dto.RegisterDto;
import com.example.authjwtdemo.entity.User;
import com.example.authjwtdemo.service.AuthenticationService;
import com.example.authjwtdemo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterDto dto){
        User registeredUser = service.register(dto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto dto){
        User loggedInUser = service.login(dto);

        //System.out.println(loggedInUser);

        String token = jwtService.generateToken(loggedInUser);
        long expirationTime = jwtService.getJwtExpirationTime();

        LoginResponse response = new LoginResponse(token, expirationTime);

        return ResponseEntity.ok(response);

    }

}
