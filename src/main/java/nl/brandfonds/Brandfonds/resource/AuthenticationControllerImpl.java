package nl.brandfonds.Brandfonds.resource;


import nl.brandfonds.Brandfonds.model.security.SignInResponse;
import nl.brandfonds.Brandfonds.model.security.SignUpRequest;
import nl.brandfonds.Brandfonds.services.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/auth")
public class AuthenticationControllerImpl implements AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping(path = "/login")
    public SignInResponse login(String email, String password) {
        return authenticationService.login(email, password);
    }

    @PostMapping(path = "/register")
    public void requestRegistration(SignUpRequest request) {
        authenticationService.sendRegisterMail(request);
    }

    @GetMapping(path = "/registerConformation/{registrationCode}")
    public String confirmRegistration(@PathVariable("registrationCode") String registrationCode) {
        return authenticationService.register(registrationCode);
    }

    @GetMapping(path = "/forgotPassword/{email}")
    public void forgotPassword(@PathVariable("email") String email) {
        authenticationService.sendPasswordResetMail(email);
    }

    @GetMapping(path = "/resetPasswordCode/{passwordResetCode}")
    public void checkPasswordResetCode(@PathVariable("passwordResetCode") String passwordResetCode) {
        authenticationService.validatePasswordResetCode(passwordResetCode);
    }

    @PostMapping(path = "/resetPassword/{passwordResetCode}")
    public void changePassword(@PathVariable("passwordResetCode") String passwordResetCode, String password) {
        authenticationService.resetPassword(passwordResetCode, password);
    }

    @GetMapping(path = "/activateUser/{userId}")
    public void setUserActivation(@PathVariable("userId") Integer userId) {
        authenticationService.setActivated(userId);
    }
}
