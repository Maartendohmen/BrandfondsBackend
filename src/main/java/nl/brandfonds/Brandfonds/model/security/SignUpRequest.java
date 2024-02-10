package nl.brandfonds.Brandfonds.model.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpRequest {
    String firstName;
    String lastName;
    String email;
    String password;
}