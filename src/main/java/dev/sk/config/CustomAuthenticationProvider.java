package dev.sk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String uname = authentication.getName();
        try{
            //In case, User not found it'll raise Exception
            UserDetails userDetails = userDetailsService.loadUserByUsername(uname);
            if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())){
                throw new BadCredentialsException("");
            }
            UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(uname,authentication.getCredentials().toString(), List.of());
            return upa;

        }catch (Exception exception){
            throw new BadCredentialsException("Invalid Username/Password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}