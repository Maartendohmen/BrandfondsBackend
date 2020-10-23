package nl.brandfonds.Brandfonds.security;

import nl.brandfonds.Brandfonds.model.UserRole;

public class AuthenticationResponse {

    private final String jwt;
    private final UserRole role;

    public AuthenticationResponse(String jwt, UserRole role) {
        this.jwt = jwt;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }
    public UserRole getRole() {
        return role;
    }
}
