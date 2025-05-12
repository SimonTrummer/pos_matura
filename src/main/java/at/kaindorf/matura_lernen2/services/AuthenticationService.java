package at.kaindorf.matura_lernen2.services;

import at.kaindorf.matura_lernen2.pojos.AuthenticationResponse;
import at.kaindorf.matura_lernen2.pojos.SigninRequest;
import at.kaindorf.matura_lernen2.pojos.SignupRequest;

public interface AuthenticationService {
    AuthenticationResponse signup(SignupRequest signupRequest);
    AuthenticationResponse signin(SigninRequest signinRequest);
    AuthenticationResponse otpSignin(Integer otpToken);


}
