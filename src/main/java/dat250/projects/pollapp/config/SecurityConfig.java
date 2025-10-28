package dat250.projects.pollapp.config;

import dat250.projects.pollapp.service.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final boolean jwtEnabled;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            @Value("${jwt.enabled:true}") boolean jwtEnabled)
    {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtEnabled = jwtEnabled;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        if (jwtEnabled) {
            // JWT enabled → secure all endpoints except login
            http.authorizeHttpRequests(auth -> auth
                            .requestMatchers("/login", "user").permitAll() //allow create user and login endpoint
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        } else {
            // JWT disabled → make everything public (for testing)
            http.authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
            );
        }

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
