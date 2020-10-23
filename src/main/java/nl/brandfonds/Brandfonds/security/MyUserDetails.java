package nl.brandfonds.Brandfonds.security;

import nl.brandfonds.Brandfonds.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(User user){
        this.username = user.getForname();
        this.password = user.getPassword();
        this.isEnabled = user.isActivated();
        this.authorities = Arrays.asList(new SimpleGrantedAuthority(user.getUserRole().toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return isEnabled;
    }
}
