package at.kaindorf.matura_lernen2.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(AccountPK.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Type",discriminatorType = DiscriminatorType.STRING)
public abstract class Account {
    @Id
    @GeneratedValue
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Integer accountId;
    @Id
    private Integer accountNumber;
    private Double balance;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "accounts")
    @JsonIgnore
    private List<Customer> customers = new ArrayList<>();
}
