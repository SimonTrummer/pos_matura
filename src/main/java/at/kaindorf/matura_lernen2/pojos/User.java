package at.kaindorf.matura_lernen2.pojos;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer userId;
    @Column(unique = true,nullable = false)
    private String email;
    private String firstname;
    private String lastname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Role role;
    private Boolean enabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
