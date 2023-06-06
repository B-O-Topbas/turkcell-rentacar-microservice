package com.kodlamaio.paymentservice.config;

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
    public SecurityFilterChain chain(HttpSecurity security)throws Exception{
        var converter=new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtRoleConverter());

        security.authorizeHttpRequests()
                .requestMatchers("/api/payments/check", Paths.PrometheusMetricsPath, Arrays.toString(Paths.SwaggerPaths))
                .permitAll()
                .requestMatchers("/api/payments")
                .hasAnyRole(Roles.Admin,Roles.User,Roles.Developer)
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(converter);
        return security.build();
    }
}
