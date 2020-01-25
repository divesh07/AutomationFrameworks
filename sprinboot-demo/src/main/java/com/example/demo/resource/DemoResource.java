package com.example.demo.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/spring")
public class DemoResource {

    @GetMapping
    public String hello(){
        return "Hello Spring";
    }
}
