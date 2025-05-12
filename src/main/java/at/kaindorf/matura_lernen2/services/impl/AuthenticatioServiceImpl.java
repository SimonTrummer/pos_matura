package at.kaindorf.matura_lernen2.services.impl;

import at.kaindorf.matura_lernen2.exceptions.UserAlreadyExistsException;
import at.kaindorf.matura_lernen2.pojos.*;
import at.kaindorf.matura_lernen2.repositories.UserRepository;
import at.kaindorf.matura_lernen2.services.AuthenticationService;
import at.kaindorf.matura_lernen2.services.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatioServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final OTPService otpService;

    @Override
    public AuthenticationResponse signup(SignupRequest signupRequest) {
        User user = User.builder()
                .firstname(signupRequest.getFirstname())
                .lastname(signupRequest.getLastname())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        try {
            userRepository.save(user);
            OTPToken token = otpService.generateToken(user);
            log.info(token.getToken().toString());
            return new AuthenticationResponse(jwtService.generateToken(user,TokenType.MFA), "This is a mfa token, use it with the otp code");
        } catch (Exception e) {
            throw new UserAlreadyExistsException("A User with this email already exists!");
        }
    }

    @Override
    public AuthenticationResponse signin(SigninRequest signinRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        OTPToken token = otpService.generateToken((User)
                authentication.getPrincipal());
        log.info(token.getToken().toString());
        return new AuthenticationResponse(jwtService.generateToken((UserDetails)
                authentication.getPrincipal(), TokenType.MFA), "This is a mfa token, use it with the otp code");
    }

    @Override
    public AuthenticationResponse otpSignin(Integer otpToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OTPToken token = OTPToken.builder().token(otpToken)
                .user((User) authentication.getPrincipal()).build();

        if(otpService.isTokenValid(token)){
            return new AuthenticationResponse(jwtService.generateToken((UserDetails)
                    authentication.getPrincipal(),TokenType.JWT), "This is a jwt token");
        }else {
            throw new BadCredentialsException("Invalid OTP Token");
        }

    }
}
