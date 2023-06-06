package com.kodlamaio.rentalservice.config;

import com.kodlamaio.commonpackage.utils.constants.Paths;
import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.commonpackage.utils.security.KeycloakJwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain chain(HttpSecurity security)throws Exception{
        var converter=new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtRoleConverter());

        security.authorizeHttpRequests()
                .requestMatchers("/api/rentals/**",Paths.Rental.Prefix)
                .hasAnyRole(Roles.Admin, Roles.Developer, Roles.Moderator, Roles.User)
                .requestMatchers(Paths.SwaggerPaths)
                .permitAll()
                .requestMatchers(Paths.PrometheusMetricsPath)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(converter);
        return security.build();
    }
}
