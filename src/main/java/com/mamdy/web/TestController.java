package com.mamdy.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class TestController {

    @GetMapping("/test")
    public String isOk(){
        return  "isOK";
    }
}
