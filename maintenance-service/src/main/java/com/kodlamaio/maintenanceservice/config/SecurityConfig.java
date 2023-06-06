package com.kodlamaio.maintenanceservice.config;

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
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
        var converter=new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtRoleConverter());
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(Arrays.toString(Paths.SwaggerPaths),Paths.PrometheusMetricsPath)
                .permitAll()
                .requestMatchers("/api/maintenances")
                .hasAnyRole(Roles.User,Roles.Admin,Roles.Moderator)
                .anyRequest().authenticated()
                .and()
                .csrf()
                .disable()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(converter);
        return httpSecurity.build();

    }
}