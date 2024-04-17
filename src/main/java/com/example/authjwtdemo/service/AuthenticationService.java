package com.example.authjwtdemo.service;

import com.example.authjwtdemo.dto.LoginDto;
import com.example.authjwtdemo.dto.RegisterDto;
import com.example.authjwtdemo.entity.User;
import com.example.authjwtdemo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User register(RegisterDto dto){
        User user = new User(
                dto.getName(),
                dto.getEmail(),
                encoder.encode(dto.getPassword())
                );

        return repository.save(user);
    }

    public User login(LoginDto dto){

        //System.out.println("here");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );
        }
        catch (Exception e){
            System.out.println(e);
        }

        //System.out.println("hi " + authentication);

        //System.out.println("here1");

        //return null;

        return repository.findByEmail(dto.getEmail())
                .orElseThrow();
    }

}
