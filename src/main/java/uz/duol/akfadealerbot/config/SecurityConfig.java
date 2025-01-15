package uz.duol.akfadealerbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Agar CSRF kerak bo'lmasa o'chiring
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Qolgan barcha so'rovlar autentifikatsiyani talab qiladi
                )
                .httpBasic(); // Basic Authentication

        return http.build();
    }
}
