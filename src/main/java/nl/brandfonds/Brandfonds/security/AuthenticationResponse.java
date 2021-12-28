package nl.brandfonds.Brandfonds.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.brandfonds.Brandfonds.model.User;

@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {

    private final String jwt;
    private final User user;
}
