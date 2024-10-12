package com.koonetto.koonetto_resource_server.security;

import com.koonetto.koonetto_resource_server.converter.AuthServerGrantedAuthoritiesConverter;
import com.koonetto.koonetto_resource_server.entity.Authority;
import com.koonetto.koonetto_resource_server.security.handler.AccessDeniedHandler;
import com.koonetto.koonetto_resource_server.security.handler.AuthEntryPointHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AuthServerGrantedAuthoritiesConverter());
        http
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorizeHttpRequest -> {
                    authorizeHttpRequest.requestMatchers(HttpMethod.GET, "/api/v1/check")
                            .hasRole(Authority.CLIENT.name())
                            .requestMatchers("/api/v1/user").hasRole(Authority.USER.name());
                })
                .cors(corsConfiguration -> corsConfiguration.configurationSource(corsConfiguration()))
                .oauth2ResourceServer(oauth2ResourceServerConfig -> {
                    oauth2ResourceServerConfig.jwt(jwtConfigurer -> {
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                    });
                })
                .exceptionHandling(exceptionHandlingConfig -> {
                    exceptionHandlingConfig.authenticationEntryPoint(new AuthEntryPointHandler());
                    exceptionHandlingConfig.accessDeniedHandler(new AccessDeniedHandler());
                    });

        return http.build();
    }

    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(Duration.ofSeconds(3600L));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
