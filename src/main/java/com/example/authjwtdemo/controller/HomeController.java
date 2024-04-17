package com.example.authjwtdemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${hi.name}")
    private String name; //this works

    //basic http authentication
    //username: user
    //password: in console
    @GetMapping("/hi")
    public String sayHi(){
        return "Hi " + name;
    }

}
