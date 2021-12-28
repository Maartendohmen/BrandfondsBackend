package nl.brandfonds.Brandfonds.security;

import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsServer implements UserDetailsService {

    @Autowired
    IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        if (userService.getByMail(s).isPresent()) {
            User retrievedUser = userService.getByMail(s).get();

            //Build user Authority. some how a convert from your custom roles which are in database to spring GrantedAuthority
            List<GrantedAuthority> authorities = buildUserAuthority(retrievedUser.getUserRole());

            //The magic is happen in this private method !
            return buildUserForAuthentication(retrievedUser, authorities);
        }

        throw new UsernameNotFoundException("The requested user could not be found");

    }

    //Fill your extended User object (CurrentUser) here and return it
    private CustomUserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {

        String mailadres = user.getMailadres();
        String password = user.getPassword();
        boolean enabled = user.isActivated();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        CustomUserDetails customuser = new CustomUserDetails(mailadres, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);

        customuser.setId(user.getId());

        return customuser;

        //If your database has more information of user for example firstname,... You can fill it here
        //CurrentUser currentUser = new CurrentUser(....)
        //currentUser.setFirstName( user.getfirstName() );
        //.....
        //return currentUser ;
    }

    private List<GrantedAuthority> buildUserAuthority(UserRole role) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        setAuths.add(new SimpleGrantedAuthority(role.toString()));

        return new ArrayList<GrantedAuthority>(setAuths);
    }
}
