package com.project.back_end.mvc;

import com.project.back_end.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * MVC Controller for serving Thymeleaf dashboard views.
 * Handles role-based dashboard routing with JWT token validation.
 */
@Controller
public class DashboardController {

    @Autowired
    private TokenService tokenService;

    /**
     * Serve the main index/login page.
     *
     * @return index view
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Serve admin dashboard with token validation.
     *
     * @param token the JWT token
     * @return admin dashboard view or redirect to login
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        try {
            if (tokenService.validateTokenAndRole(token, "ADMIN")) {
                return "admin/adminDashboard";
            }
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    /**
     * Serve doctor dashboard with token validation.
     *
     * @param token the JWT token
     * @return doctor dashboard view or redirect to login
     */
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        try {
            if (tokenService.validateTokenAndRole(token, "DOCTOR")) {
                return "doctor/doctorDashboard";
            }
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    /**
     * Serve patient dashboard with token validation.
     *
     * @param token the JWT token
     * @return patient dashboard view or redirect to login
     */
    @GetMapping("/patientDashboard/{token}")
    public String patientDashboard(@PathVariable String token) {
        try {
            if (tokenService.validateTokenAndRole(token, "PATIENT")) {
                return "patient/patientDashboard";
            }
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
}
