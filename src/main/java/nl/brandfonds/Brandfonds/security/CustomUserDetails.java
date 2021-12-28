package nl.brandfonds.Brandfonds.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetails extends User {

    private int id;

    public CustomUserDetails(String mailadres, String password, Collection<? extends GrantedAuthority> authorities) {
        super(mailadres, password, authorities);
    }

    public CustomUserDetails(String mailadres, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(mailadres, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
