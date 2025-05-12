package at.kaindorf.matura_lernen2.db;

import at.kaindorf.matura_lernen2.pojos.*;
import at.kaindorf.matura_lernen2.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InnitData implements ApplicationRunner {
    private final CustomerRepository customerRepository;

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void run(ApplicationArguments args) throws Exception {
       jsonReadTree();
    }

    public void jsonObjectMapper() throws IOException {
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

    public void jsonReadTree() throws IOException {
        InputStream is = getClass().getResourceAsStream("/bankAccount_customer.json");
        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());

        JsonNode rootNode = om.readTree(is);
        if (!rootNode.isArray()) {
            return;
        }

        for (JsonNode node: rootNode) {
            String firstname = node.get("firstname").asText();
            String lastname = node.get("lastname").asText();
            Integer customerNumber = node.get("customerNumber").asInt();
            LocalDate birthdate = LocalDate.parse(node.get("birthdate").asText(),dtf);
            Gender gender = Gender.valueOf(node.get("gender").asText());
            JsonNode addressRootNode = node.get("address");
            String streetname = addressRootNode.get("streetname").asText();
            String city = addressRootNode.get("city").asText();
            Integer streetnumber = addressRootNode.get("streetNumber").asInt();
            Integer zipCode = addressRootNode.get("zipCode").asInt();

            Address address = new Address(null,streetname,streetnumber,zipCode,city,null);
            Customer customer = new Customer(null,firstname,lastname,customerNumber,birthdate, gender, new ArrayList<>(),address);
            address.setCustomer(customer);
            customerRepository.save(customer);
        }
    }
}
