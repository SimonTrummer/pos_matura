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
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account{
    private double interest;

    @Builder
    public SavingsAccount(Integer accountId, Integer accountNumber, Double balance, List<Customer> customers, double interest) {
        super(accountId, accountNumber, balance, customers);
        this.interest = interest;
    }
}
