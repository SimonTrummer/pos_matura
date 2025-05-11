package at.kaindorf.matura_lernen2.db;

import at.kaindorf.matura_lernen2.pojos.*;
import at.kaindorf.matura_lernen2.repositories.CustomerRepository;
import at.kaindorf.matura_lernen2.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InnitDB implements ApplicationRunner {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createAdmin();
        loadCustomerData();
    }

    private void createAdmin() {
        User user = User.builder()
                .role(Role.ADMIN)
                .email("admin@admin.com")
                .password("admin")
                .lastname("min")
                .firstname("ad")
                .isEnabled(true)
                .build();

        userRepository.save(user);
    }

    private void loadCustomerData() throws IOException {
        InputStream is = getClass().getResourceAsStream("/bankAccount_customer.json");
        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        Set<Account> accounts = new HashSet<>();

        List<Customer> customers = om.readerForListOf(Customer.class).readValue(is);
        accounts = customers.stream().map(a -> a.getAccounts())
                .flatMap(Collection::stream).collect(Collectors.toSet());
        Set<Account> finalAccounts = accounts;

        customers.forEach(c -> {
            List<Account> realAccounts = new ArrayList<>();
            c.getAccounts().forEach(customerAccount->{
                        realAccounts.add(
                                finalAccounts.stream()
                                        .filter(account -> account.getAccountNumber().equals(customerAccount.getAccountNumber()))
                                        .findFirst().get());
                    }
                    );
            realAccounts.forEach(r ->
                r.getCustomers().add(c)
            );

            c.setAccounts(realAccounts);
        });

        customerRepository.saveAll(customers);

    }
}
