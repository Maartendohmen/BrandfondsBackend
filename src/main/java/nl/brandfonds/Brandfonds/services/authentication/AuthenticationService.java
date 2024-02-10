package nl.brandfonds.Brandfonds.services.authentication;

import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.DisabledException;
import nl.brandfonds.Brandfonds.exceptions.ExpiredException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.UserRole;
import nl.brandfonds.Brandfonds.model.security.SignInResponse;
import nl.brandfonds.Brandfonds.model.security.SignUpRequest;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import nl.brandfonds.Brandfonds.services.UserService;
import nl.brandfonds.Brandfonds.services.mail.MailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private RegisterRequestRepository registerRequestRepository;
    @Autowired
    private PasswordChangeRequestRepository passwordChangeRequestRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void sendRegisterMail(SignUpRequest request) {
        if (!userService.exists(request.getEmail())) {
            var registerRequest = RegisterRequest.builder()
                    .randomString(RandomStringUtils.randomAlphanumeric(15))
                    .initialDate(LocalDateTime.now())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            registerRequestRepository.save(registerRequest);
            mailService.sendRegisterMail(registerRequest.getEmail(), registerRequest.getRandomString());
        } else {
            throw new AlreadyExistException.UserAlreadyExistException(request.getEmail());
        }
    }

    public String register(String registrationCode) {
        var registerRequest = registerRequestRepository.getByRandomString(registrationCode);
        if (registerRequest.isEmpty()) {
            throw new ExpiredException.LinkExpiredException(registrationCode);
        }
        var retrievedRegisterRequest = registerRequest.get();

        var user = User.builder()
                .email(retrievedRegisterRequest.getEmail())
                .firstname(retrievedRegisterRequest.getFirstName())
                .lastname(retrievedRegisterRequest.getLastName())
                .password(retrievedRegisterRequest.getPassword())
                .role(UserRole.NORMAL)
                .build();

        var savedUser = userService.save(user);
        var jwt = jwtService.generateToken(user);
        mailService.sendUserActivationMail("brandmeester@brandfonds.nl",
                savedUser.getEmail(), savedUser.getFirstname() + " "
                        + savedUser.getLastname(), savedUser.getId());

        return jwt;
    }

    public void sendPasswordResetMail(String email) {
        userService.getByMail(email);
        PasswordChangeRequest request = new PasswordChangeRequest(email);
        passwordChangeRequestRepository.save(request);
        mailService.sendChangePasswordMail(request.getEmail(), request.getRandomString());
    }

    public void validatePasswordResetCode(String passwordResetCode) {
        if (passwordChangeRequestRepository.getByRandomString(passwordResetCode).isEmpty()) {
            throw new ExpiredException.LinkExpiredException(passwordResetCode);
        }
    }

    public void resetPassword(String passwordResetCode, String newPassword) {
        var passwordChangeRequest = passwordChangeRequestRepository.getByRandomString(passwordResetCode).orElseThrow(() -> new ExpiredException.LinkExpiredException(passwordResetCode));
        var userToUpdate = userService.getByMail(passwordChangeRequest.getEmail());
        userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        userService.save(userToUpdate);
    }

    public void setActivated(Integer id) {
        var userToUpdate = userService.getById(id);
        userToUpdate.setActivated(true);
        userService.save(userToUpdate);
        mailService.sendUserActivatedMail(userToUpdate.getEmail(), userToUpdate.getEmail(), userToUpdate.getFirstname());
    }

    public SignInResponse login(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (org.springframework.security.authentication.DisabledException authenticationException) {
            throw new DisabledException.UserDisabledException("email", email);
        } catch (BadCredentialsException badCredentialsException) {
            throw new NotFoundException.UserNotFoundException(email, password);
        }

        var user = userService.getByMail(email);
        var jwtToken = jwtService.generateToken(user);
        return new SignInResponse(jwtToken, user);
    }
}
