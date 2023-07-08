package dev.sk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectConfig {
    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.csrf(x->x.disable());
        httpSecurity.authorizeHttpRequests(
                auth->{
                    auth.requestMatchers("/otp-auth").permitAll()
                        .requestMatchers("/verify-otp").permitAll()
                        .anyRequest().authenticated();
                }
        );
        httpSecurity.formLogin(e->e.defaultSuccessUrl("/otp-auth",true));
        return httpSecurity.build();
    }

    /*@Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }*/

}
