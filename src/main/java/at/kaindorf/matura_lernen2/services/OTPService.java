package at.kaindorf.matura_lernen2.services;

import at.kaindorf.matura_lernen2.pojos.OTPToken;
import at.kaindorf.matura_lernen2.pojos.User;

public interface OTPService {
    OTPToken generateToken (User user);
    boolean isTokenValid(OTPToken token);
}
