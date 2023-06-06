package com.kodlamaio.filterservice.config;

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
    public SecurityFilterChain chain(HttpSecurity http) throws Exception{
        var converter=new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtRoleConverter());



        http.authorizeHttpRequests()
                .requestMatchers(Arrays.toString(Paths.SwaggerPaths),Paths.PrometheusMetricsPath)
                .permitAll()
                .requestMatchers("/api/filters")
                .hasAnyRole(Roles.User,Roles.Admin,Roles.Developer)
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
