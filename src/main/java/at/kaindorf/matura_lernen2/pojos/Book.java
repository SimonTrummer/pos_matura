package at.kaindorf.matura_lernen2.pojos;

import at.kaindorf.matura_lernen2.xml.XmlLocalDateParser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Book {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer bookId;

    private String title;
    private String author;
    @XmlJavaTypeAdapter(XmlLocalDateParser.class)
    private LocalDate published;

    @OneToMany(mappedBy = "book")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Loan> loans= new ArrayList<>();

}
