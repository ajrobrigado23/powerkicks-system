package com.powerkickstkd.my_application_backend.transaction.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Show the login paage
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(HttpServletRequest request,
                                  Model model) {

        // Check if there's an error message in session
        HttpSession session = request.getSession();

        String loginErrorMessage = (String) session.getAttribute("loginErrorMessage");
        String logoutSuccessMessage = (String) session.getAttribute("logoutSuccessMessage");

        // If there is an error message, add it to the model and clear it from session
        if (loginErrorMessage != null) {

            model.addAttribute("errorMessage", loginErrorMessage);
            // Clear after use
            session.removeAttribute("loginErrorMessage");
        }

        // If there is a logout success message, add it to the model and clear it from session
        if (logoutSuccessMessage != null) {
            model.addAttribute("successMessage", logoutSuccessMessage);
            // Clear after use
            session.removeAttribute("logoutSuccessMessage");
        }

        return "html/index";
    }
}
