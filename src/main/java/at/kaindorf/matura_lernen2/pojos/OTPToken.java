package at.kaindorf.matura_lernen2.pojos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class OTPToken {
    @Id
    private Integer token;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;
}
