package at.kaindorf.matura_lernen2.controller;

import at.kaindorf.matura_lernen2.pojos.AuthenticationResponse;
import at.kaindorf.matura_lernen2.pojos.SigninRequest;
import at.kaindorf.matura_lernen2.pojos.SignupRequest;
import at.kaindorf.matura_lernen2.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authenticationService.signup(signupRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @PostMapping("/otp-signin")
    public ResponseEntity<AuthenticationResponse> otpSignin(@RequestParam Integer otpToken) {
        return ResponseEntity.ok(authenticationService.otpSignin(otpToken));
    }



}
