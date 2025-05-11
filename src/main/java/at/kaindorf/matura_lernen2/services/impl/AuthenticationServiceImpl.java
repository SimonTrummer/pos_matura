package at.kaindorf.matura_lernen2.services.impl;

import at.kaindorf.matura_lernen2.exceptions.UserAlreadyExistsException;
import at.kaindorf.matura_lernen2.pojos.*;
import at.kaindorf.matura_lernen2.repositories.UserRepository;
import at.kaindorf.matura_lernen2.services.AuthenticationService;
import at.kaindorf.matura_lernen2.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse signup(SignupRequest signupRequest) {
        User user = User.builder()
                .email(signupRequest.getEmail())
                .firstname(signupRequest.getFirstname())
                .lastname(signupRequest.getLastname())
                .enabled(true)
                .role(Role.USER)
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        try {
            userRepository.save(user);
            String token = jwtService.generateToken(user);
            return new AuthenticationResponse(token);
        } catch (Exception e) {
            throw new UserAlreadyExistsException("There already is a user with the email " + signupRequest.getEmail() +" in our System!");
        }
    }

    @Override
    public AuthenticationResponse signin(SigninRequest signinRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        return new AuthenticationResponse(token);
    }
}
