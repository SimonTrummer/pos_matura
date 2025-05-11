package at.kaindorf.matura_lernen2.services.impl;

import at.kaindorf.matura_lernen2.config.UserAuthenticationProvider;
import at.kaindorf.matura_lernen2.controller.AuthenticationController;
import at.kaindorf.matura_lernen2.exceptions.UserAlreadyExistsException;
import at.kaindorf.matura_lernen2.pojos.*;
import at.kaindorf.matura_lernen2.repositories.UserRepository;
import at.kaindorf.matura_lernen2.services.AuthenticationService;
import at.kaindorf.matura_lernen2.services.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse signin(SigninRequest signinRequest) {
        String email = signinRequest.getEmail();
        String password = signinRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password));

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse signup(SignupRequest signupRequest) {
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .lastname(signupRequest.getLastname())
                .firstname(signupRequest.getFirstname())
                .role(Role.USER)
                .isEnabled(false)
                .build();
        try {
            userRepository.save(user);
            String token = jwtService.generateToken(user);
            log.info(token);
            return new AuthenticationResponse(token);
        }catch (Exception e) {
            throw new UserAlreadyExistsException("There already is an user with the mail "+signupRequest.getEmail());
        }

    }
}
