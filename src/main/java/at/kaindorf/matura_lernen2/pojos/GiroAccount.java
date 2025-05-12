package at.kaindorf.matura_lernen2.pojos;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class GiroAccount extends Account{
    private Double overdraft;
    private Double debitInterest;
    private Double creditInterest;
}
