package nl.brandfonds.Brandfonds.model.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.brandfonds.Brandfonds.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String jwt;
    private User loggedInUser;
}
