package at.kaindorf.matura_lernen2.db;

import at.kaindorf.matura_lernen2.pojos.Account;
import at.kaindorf.matura_lernen2.pojos.Customer;
import at.kaindorf.matura_lernen2.pojos.SavingsAccount;
import at.kaindorf.matura_lernen2.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InnitData implements ApplicationRunner {
    private final CustomerRepository customerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream is = getClass().getResourceAsStream("/bankAccount_customer.json");
        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());

        List<Customer> customers = om.readerForListOf(Customer.class).readValue(is);
        Set<Account> accounts = new HashSet<>();
        accounts = customers.stream().flatMap(customer -> customer.getAccounts().stream()).collect(Collectors.toSet());

        Set<Account> finalAccounts = accounts;
        customers.forEach(customer -> {
            List<Account> customerAccounts = new ArrayList<>();

            customer.getAccounts().forEach(account -> {
                Account realAccount = finalAccounts.stream().filter(a -> a.equals(account)).findFirst().orElse(null);
                customerAccounts.add(realAccount);
                realAccount.getCustomers().add(customer);
            });

            customer.setAccounts(customerAccounts);

        });

        customerRepository.saveAll(customers);

    }
}
