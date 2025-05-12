package at.kaindorf.matura_lernen2.services.impl;

import at.kaindorf.matura_lernen2.pojos.OTPToken;
import at.kaindorf.matura_lernen2.pojos.User;
import at.kaindorf.matura_lernen2.repositories.OTPRepository;
import at.kaindorf.matura_lernen2.services.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final OTPRepository otpRepository;

    @Override
    public OTPToken generateToken(User user) {
        Random random = new Random();
        OTPToken token = OTPToken.builder()
                .user(user)
                .token(random.nextInt(9999))
                .build();

        otpRepository.save(token);

        return token;
    }

    @Override
    public boolean isTokenValid(OTPToken token) {
        List<OTPToken> otpTokens = otpRepository.findAll();
        otpRepository.delete(token);
        if (otpTokens.contains(token)) {
            return true;
        }
        return false;
    }
}
