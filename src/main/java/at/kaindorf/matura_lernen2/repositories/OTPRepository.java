package at.kaindorf.matura_lernen2.repositories;

import at.kaindorf.matura_lernen2.pojos.OTPToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OTPToken,Integer> {
    OTPToken findByUser_Email(String userEmail);
}
