package at.kaindorf.matura_lernen2.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class SignupRequest {

    private String email;
    private String firstname;
    private String lastname;
    private String password;
}
