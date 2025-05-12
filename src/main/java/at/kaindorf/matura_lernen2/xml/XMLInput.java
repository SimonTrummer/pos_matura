package at.kaindorf.matura_lernen2.xml;

import at.kaindorf.matura_lernen2.pojos.*;
import at.kaindorf.matura_lernen2.repositories.LoanRepository;
import jakarta.xml.bind.JAXB;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class XMLInput implements ApplicationRunner {
    private final LoanRepository loanRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream is = getClass().getResourceAsStream("/loans.xml");

        assert is != null;
        List<Loan> loans = JAXB.unmarshal(is, DataHolder.class).getLoans();
        Set<Book> books = loans.stream().map(Loan::getBook).collect(Collectors.toSet());
        Set<User> users = new HashSet<>();

        loans.forEach(loan -> {
            loan.setBook(books.stream().filter(book -> book.equals(loan.getBook())).findFirst().orElse(null));
            loan.getBook().getLoans().add(loan);
            User user = User.builder()
                    .role(Role.USER)
                    .email(loan.getUser().getEmail())
                    .firstname(loan.getUser().getFirstname().split(" ")[0])
                    .lastname(loan.getUser().getFirstname().split(" ")[1])
                    .password(passwordEncoder.encode("password"))
                    .enabled(true)
                    .loans(new ArrayList<>())
                    .build();
            users.add(user);
            loan.setUser(users.stream().filter(u-> u.getEmail().equals(user.getEmail())).findFirst().orElse(null));
            loan.getUser().getLoans().add(loan);
        });

        loanRepository.saveAll(loans);
    }
}
