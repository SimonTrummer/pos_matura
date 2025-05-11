package at.kaindorf.matura_lernen2.controller;

import at.kaindorf.matura_lernen2.pojos.Account;
import at.kaindorf.matura_lernen2.pojos.Customer;
import at.kaindorf.matura_lernen2.repositories.AccountRepository;
import at.kaindorf.matura_lernen2.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(("/api/user"))
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final AccountRepository accountRepository;

    @GetMapping("/hello")
    public ResponseEntity<String> hello () {
        return ResponseEntity.ok("hello user");
    }

    @GetMapping("/accountsPerCity")
    public ResponseEntity<Set<Account>> allAccountsWithHigherBalance (@RequestParam Double balance) {
        Set<Account> accountsWithHigherBalance = accountRepository.getAccountByBalanceGreaterThan(balance);
        accountsWithHigherBalance.forEach(account -> account.setCustomers(null));
        return ResponseEntity.ok(accountsWithHigherBalance);
    }

    @GetMapping("/accountsPaged")
    public ResponseEntity<List<Account>> allAccountsSortedByBalanceAndPaged(@RequestParam Integer page) {
        Pageable curPage = PageRequest.of(page,10);

        List<Account> accounts = accountRepository.findAllByOrderByBalanceAsc(curPage);

        return ResponseEntity.ok(accounts);
    }
}
