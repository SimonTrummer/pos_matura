package at.kaindorf.matura_lernen2.config;

import at.kaindorf.matura_lernen2.jwt.JwtAuthenticationFilter;
import at.kaindorf.matura_lernen2.jwt.JwtUnauthorizedEndpoint;
import at.kaindorf.matura_lernen2.jwt.MfaAuthenticationFilter;
import at.kaindorf.matura_lernen2.pojos.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MfaAuthenticationFilter mfaAuthenticationFilter;
    private final JwtUnauthorizedEndpoint jwtUnauthorizedEndpoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"api/user/**").hasAnyAuthority(Role.USER.name(),Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET,"api/admin/**").hasAuthority(Role.ADMIN.name())
                                .anyRequest().permitAll()
                ).sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(mfaAuthenticationFilter, JwtAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtUnauthorizedEndpoint))
        ;

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider () {
        return new UserAuthenticationProvider();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
