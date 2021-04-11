package nl.brandfonds.Brandfonds.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private int id;

    public CustomUserDetails(String mailadres, String password, Collection<? extends GrantedAuthority> authorities) {
        super(mailadres, password, authorities);
    }

    public CustomUserDetails(String mailadres, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(mailadres, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
