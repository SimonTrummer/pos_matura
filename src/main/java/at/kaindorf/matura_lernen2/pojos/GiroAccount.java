package at.kaindorf.matura_lernen2.pojos;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("GIRO")
public class GiroAccount extends Account{
    private Double debitInterest;
    private Double creditInterest;
    private Double overdraft;

    @Builder
    public GiroAccount(Integer accountId, Integer accountNumber, Double balance, List<Customer> customers, Double debitInterest, Double creditInterest, Double overdraft) {
        super(accountId, accountNumber, balance, customers);
        this.debitInterest = debitInterest;
        this.creditInterest = creditInterest;
        this.overdraft = overdraft;
    }
}
