package dev.sk.service;

import dev.sk.model.OTPCache;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    Map<String, OTPCache> otpCacheMap = new HashMap<>();
    final int otpExpiryInMinutes = 5;
    public boolean isUsernamePasswordValidated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null && ! (authentication instanceof AnonymousAuthenticationToken)) {
            return true;
        }
        return false;
    }

    public void generateOTP(Principal principal){
        String name = principal.getName();
        OTPCache otpCache = new OTPCache(generateCode(), LocalDateTime.now().plusMinutes(otpExpiryInMinutes));
        if (otpCacheMap.get(name)!=null) otpCacheMap.remove(name);
        otpCacheMap.put(name,otpCache);
        System.out.printf("OTP Generated Successfully for the user '%s'. The code is %s %n",name,otpCache.otp());
    }
    public static String generateCode() {
        String code;
        try {
            SecureRandom random =
                    SecureRandom.getInstanceStrong();
            int c = random.nextInt(9000) + 1000;
            code = String.valueOf(c);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Problem while generating the random code.");
        }
        return code;
    }

    public boolean verifyOtp(String username, String otp) {
        if (otpCacheMap.containsKey(username) && otpCacheMap.get(username).otp().equals(otp)){
            boolean expired = !otpCacheMap.get(username).expiry().isBefore(LocalDateTime.now());
            if (expired) return false;
            throw new RuntimeException("OTP Expired...");
        }
        throw new RuntimeException("Invalid OTP");
    }
}
