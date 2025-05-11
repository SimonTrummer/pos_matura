package at.kaindorf.matura_lernen2.services;

import at.kaindorf.matura_lernen2.pojos.AuthenticationResponse;
import at.kaindorf.matura_lernen2.pojos.SigninRequest;
import at.kaindorf.matura_lernen2.pojos.SignupRequest;

public interface AuthenticationService {
    AuthenticationResponse signin(SigninRequest signinRequest);
    AuthenticationResponse signup(SignupRequest signupRequest);
}
