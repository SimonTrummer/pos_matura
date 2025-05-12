package at.kaindorf.matura_lernen2.pojos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class Address {
    @Id
    @GeneratedValue
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Integer addressId;

    @JsonAlias("streetname")
    private String streetName;
    private Integer streetNumber;
    private Integer zipCode;
    private String city;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Customer customer;
}
