package com.powerkickstkd.my_application_backend.transaction.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Show the login form
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {

        return "html/index";
    }
}
