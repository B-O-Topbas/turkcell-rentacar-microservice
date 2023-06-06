package com.kodlamaio.inventoryservice.config;

import com.kodlamaio.commonpackage.utils.constants.Paths;
import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.commonpackage.utils.security.KeycloakJwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        var converter=new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtRoleConverter());

        http.authorizeHttpRequests()
                .requestMatchers("/api/cars/**","/api/cars/check-car-available/**", Paths.PrometheusMetricsPath, Arrays.toString(Paths.SwaggerPaths))
                .permitAll()
                .requestMatchers("/api/cars","/api/brands","/api/models")
                .hasAnyRole(Roles.User,Roles.Admin)
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(converter);
        return http.build();
    }
}