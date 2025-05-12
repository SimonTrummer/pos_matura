package at.kaindorf.matura_lernen2.pojos;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@IdClass(AccountPK.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Type",discriminatorType = DiscriminatorType.STRING)
public abstract class Account {
    @Id
    private Integer accountId;
    @Id
    private Integer accountNumber;

    private Double balance;

    @ManyToMany(mappedBy = "accounts")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Customer> customers = new ArrayList<>();
}
