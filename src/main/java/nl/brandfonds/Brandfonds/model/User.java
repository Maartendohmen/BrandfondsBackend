package nl.brandfonds.Brandfonds.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails, Comparable<User> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(unique = true)
    private String email;
    private String firstname;
    private String lastname;
    private String profilePictureFileName;
    private String password;
    @Column(columnDefinition = "double")
    private long saldo; //in cents
    private boolean activated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activated;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public int compareTo(User user) {
        return this.getFirstname().compareTo(user.getFirstname());
    }
}
