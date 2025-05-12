package at.kaindorf.matura_lernen2.pojos;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class Customer {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer customerId;

    private String firstname;
    private String lastname;
    private Integer customerNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(joinColumns = @JoinColumn(name = "customerId"),
    inverseJoinColumns = {
            @JoinColumn(name = "accountId", referencedColumnName = "accountId"),
            @JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber"),
    })
    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
    @JsonSubTypes({
            @JsonSubTypes.Type(GiroAccount.class),
            @JsonSubTypes.Type(SavingsAccount.class)
    })
    private List<Account> accounts;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;

}
