package com.marcin.library_spring_boot.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

    private static final String[] SWAGGER_WHITE_LIST_URL = {"/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"};

    //INFO: Ogólnie poczytać o tym wszystkim na stronie Spring Security https://docs.spring.io/spring-security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //Disable Cross Site Request Forgery
        //INFO: nie wiem co to robi, patrz: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html
        http.csrf(AbstractHttpConfigurer::disable);

        //Protect endpoints on /secure
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/books/secure/**")
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        //Swagger
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(SWAGGER_WHITE_LIST_URL).permitAll().anyRequest().authenticated());

        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

        //Add CORS filters
        http.cors(Customizer.withDefaults());

        //Add content negation strategy
        http.setSharedObject(ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy());

        //Force a non-empty response body for 401's to make response friendly
        Okta.configureResourceServer401ResponseBody(http);

        return http.build();
    }
}
