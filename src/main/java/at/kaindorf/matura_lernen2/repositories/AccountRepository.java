package at.kaindorf.matura_lernen2.repositories;

import at.kaindorf.matura_lernen2.pojos.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    Set<Account> getAccountByBalanceGreaterThan(Double balanceIsGreaterThan);
    List<Account> findAllByOrderByBalanceAsc(Pageable pageable);

}
