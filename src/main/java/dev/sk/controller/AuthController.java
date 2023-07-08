package dev.sk.controller;

import dev.sk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    AuthService authService;
    @RequestMapping("/otp-auth")
    public String otpLogin(Principal principal){
        if (authService.isUsernamePasswordValidated()){
            authService.generateOTP(principal);
            return "otp-validation";
        }
        return "redirect:/";
    }

    @PostMapping("/verify-otp")
    public String validateOtp(@RequestParam String otp, Principal principal){
        if (authService.isUsernamePasswordValidated()){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            try{
                if (authentication!=null && authService.verifyOtp(principal.getName() ,otp)){
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getName(),otp, List.of()));
                }else {
                    System.out.println("Auth is null...");
                }
            }catch (Exception exception){
                System.out.println("Invalid/Expired OTP...");
                return "redirect:/otp-auth";
            }
        }
        return "redirect:/";
    }
}
