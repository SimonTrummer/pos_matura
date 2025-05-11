package at.kaindorf.matura_lernen2.pojos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Integer addressId;
    private String streetname;
    private Integer streetNumber;
    private Integer zipCode;
    private String city;

    @ToString.Exclude
    @OneToOne(mappedBy = "address")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Customer customer;

}
