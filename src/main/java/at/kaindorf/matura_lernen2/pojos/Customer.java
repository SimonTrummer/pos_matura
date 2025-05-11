package at.kaindorf.matura_lernen2.pojos;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.aspectj.lang.annotation.After;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @JsonIgnore
    @GeneratedValue
    private Integer customerId;

    private String firstname;

    private String lastname;

    private Integer customerNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(joinColumns = @JoinColumn(name = "customerId"),
        inverseJoinColumns = {
            @JoinColumn(name = "accountId", referencedColumnName = "accountId"),
            @JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber")
        }
    )
    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
    @JsonSubTypes({
           @JsonSubTypes.Type(GiroAccount.class),
           @JsonSubTypes.Type(SavingsAccount.class)
    })
    @EqualsAndHashCode.Exclude
    private List<Account> accounts;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;
}
