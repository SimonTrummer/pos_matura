package at.kaindorf.matura_lernen2.pojos;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "dataHolder")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class DataHolder {
    @XmlElementWrapper(name = "loans")
    @XmlElement(name = "loan")
    private List<Loan> loans = new ArrayList<>();
}
