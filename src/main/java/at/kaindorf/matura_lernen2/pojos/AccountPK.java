package at.kaindorf.matura_lernen2.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class AccountPK {
    private Integer accountId;
    private Integer accountNumber;
}
