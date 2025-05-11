package at.kaindorf.matura_lernen2.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPK {
    private Integer accountId;
    private Integer accountNumber;
}
