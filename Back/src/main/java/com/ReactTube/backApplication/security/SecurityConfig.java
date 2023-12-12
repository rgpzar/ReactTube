package com.ReactTube.backApplication.security;

import com.ReactTube.backApplication.security.filters.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Component
@EnableWebSecurity
@EnableMethodSecurity
@Builder
@Data
@AllArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sessionMngConfig -> sessionMngConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authConfig ->{
                    //PUBLIC
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authConfig.requestMatchers("/error").permitAll();


                    //VIDEO ENDPOINTS
                    //  - PRIVATE (Authenticated user functions)
                    authConfig.requestMatchers(HttpMethod.GET, "/video").authenticated();
                    authConfig.requestMatchers(HttpMethod.GET, "/video/*").authenticated();
                    authConfig.requestMatchers(HttpMethod.GET, "/video/watch/*").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/video/getVideoThumbnail/*").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/video/*/addComment").authenticated();
                    authConfig.requestMatchers(HttpMethod.PUT, "/video/*/like").authenticated();
                    authConfig.requestMatchers(HttpMethod.POST, "/video/upload").authenticated();
                    authConfig.requestMatchers(HttpMethod.DELETE, "/video/*").authenticated();

                    //USER SETTINGS ENDDPOINTS
                    authConfig.requestMatchers(HttpMethod.GET, "/setting/info").authenticated();
                    authConfig.requestMatchers(HttpMethod.PUT, "/setting/update").authenticated();
                    authConfig.requestMatchers(HttpMethod.DELETE, "/setting/delete").authenticated();

                    //ADMIN ENDPOINTS
                    authConfig.requestMatchers("/user/*").hasAuthority("ROLE_ ADMIN");
                    authConfig.requestMatchers(HttpMethod.POST,  "/user").permitAll();
                    authConfig.requestMatchers(HttpMethod.PUT,  "/user/*").hasAuthority("ROLE_ ADMIN");
                    authConfig.requestMatchers(HttpMethod.DELETE,  "/user/*").hasAuthority("ROLE_ ADMIN");

                    //DENY OTHER ENDPOINTS NOT CONTEMPLATED
                    authConfig.anyRequest().denyAll();
                });

        return http.build();
    }
}


