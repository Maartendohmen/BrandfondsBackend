package nl.brandfonds.Brandfonds.resource;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IMailService;
import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.LinkExpiredException;
import nl.brandfonds.Brandfonds.exceptions.UserDisabledException;
import nl.brandfonds.Brandfonds.exceptions.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("rest/auth")
public class AuthenticationController {

    @Autowired
    IUserService userService;

    @Autowired
    IMailService mailService;

    @Autowired
    IRegisterRequestService registerRequestService;

    @Autowired
    IPasswordChangeRequestService passwordChangeRequestService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsServer customUserDetailsService;

    @Autowired
    JwtUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping
    @ApiOperation(value = "Login", notes = "Logs in user using forname and password")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User was successfully logged in")
    })
    public ResponseEntity<?> Login(@RequestBody AuthenticationRequest authenticationRequest) throws UserNotFoundException, UserDisabledException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("De ingevoerde voornaam of het wachtwoord is fout");
        }

        final CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        if (!userDetails.isEnabled()) {
            throw new UserDisabledException("Het account waarmee je probeert in te loggen is uitgeschakeld, wacht of neem contact op met de brandmeester totdat je account is geactiveerd");
        }

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, userService.GetByName(authenticationRequest.getUsername())));

    }

    //region Register methods

    @PostMapping(path = "/register")
    @ApiOperation(value = "Register", notes = "Creates a register request for user and sends verification mail", response = Boolean.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "The registration request was successfully created", response = ResponseEntity.class)
    })
    public void Register(@RequestBody User user) {

        try {
            RegisterRequest request = new RegisterRequest(user.getEmailadres(), user.getForname(),
                    user.getSurname(), passwordEncoder.encode(user.getPassword()));
            registerRequestService.Save(request);
            mailService.SendRegisterMail(request.getEmailadres(), request.getRandomString());
        } catch (Exception x) {
            System.out.println(x);
        }

    }


    @GetMapping(path = "/registerconformation/{randomstring}")
    @ApiOperation(value = "Confirm Registration", notes = "Validates registration with generated string and sends activation request to brandmaster")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The register request was successfully validated", response = ResponseEntity.class)
    })
    public void ConfirmRegistration(@PathVariable("randomstring") String randomstring) throws AlreadyExistException, LinkExpiredException {
        RegisterRequest corospondingrequest = registerRequestService.GetByrandomString(randomstring);

        if (corospondingrequest == null) {
            throw new LinkExpiredException("De gebruikte link is ongeldig of verlopen.");
        } else if (userService.GetByMail(corospondingrequest.getEmailadres()) != null) {
            throw new AlreadyExistException("Er is al een account met dit emailadres, het is mogelijk om een nieuw"
                    + " wachtwoord op te vragen.");
        }

        userService.Save(new User(corospondingrequest.getEmailadres(), corospondingrequest.getForname(),
                corospondingrequest.getSurname(), corospondingrequest.getPassword()));
        registerRequestService.Delete(corospondingrequest);

        mailService.SendUserActivationMail("brandmeester@brandfonds.nl",
                corospondingrequest.getEmailadres(), corospondingrequest.getForname() + " "
                        + corospondingrequest.getSurname(),
                (userService.GetByMail(corospondingrequest.getEmailadres()).getId()));
    }

    //endregion

    //region Edit passwords methods

    @GetMapping(path = "/forgotpassword/{mailadres}")
    @ApiOperation(value = "Forgot password", notes = "Checks if user exist and sends password change mail to corresponding mail address")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Mail was successfully send to corresponding address", response = Boolean.class)
    })
    public void ForgotPassword(@PathVariable("mailadres") String mailadres) throws UserNotFoundException {

        if (userService.GetByMail(mailadres) != null) {

            PasswordChangeRequest request = new PasswordChangeRequest(mailadres);
            passwordChangeRequestService.Save(request);

            mailService.SendChangePasswordMail(request.getEmailadres(), request.getRandomstring());
            return;
        }

        throw new UserNotFoundException("Er kan geen gebruiker met dit mailadres worden gevonden");
    }


    @GetMapping(path = "/resetpasswordcode/{randomstring}")
    @ApiOperation(value = "Forgot password validation", notes = "Validated if given string is linked to password change request")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Change password string is validated", response = Boolean.class)
    })
    public void CheckPasswordLink(@PathVariable("randomstring") String randomstring) throws LinkExpiredException {
        PasswordChangeRequest request = passwordChangeRequestService.GetByrandomString(randomstring);

        if (request == null) {
            throw new LinkExpiredException("De link die je probeert te gebruiken is verlopen");
        }
    }


    @PostMapping(path = "/resetpassword/{randomstring}")
    @ApiOperation(value = "Change password", notes = "Changes password to new value")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Password was succesfully changed", response = Boolean.class)
    })
    public void ChangePassword(@PathVariable("randomstring") String randomstring, @RequestBody String password) {
            PasswordChangeRequest request = passwordChangeRequestService.GetByrandomString(randomstring);
            userService.UpdatePassword(passwordEncoder.encode(password), request.getEmailadres());
            passwordChangeRequestService.Delete(request);
    }
    //endregion

    @GetMapping(path = "/activate-user/{id}/{is-activated}")
    @ApiOperation(value = "Set user activation", notes = "Set user active/inactive")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User status successfully updated")
    })
    public void ActivateUser(@PathVariable("id") Integer id, @PathVariable("is-activated") boolean isActivated) throws UserNotFoundException {

        User DBUser = userService.GetByID(id);

        if (DBUser == null) {
            throw new UserNotFoundException("De gebruiker die je wilt activeren staat niet meer in het systeem");
        }

        if (!DBUser.isActivated()) {
            DBUser.setActivated(isActivated);
            userService.Save(DBUser);

            this.mailService.SendUserActivatedMail(DBUser.getEmailadres(), DBUser.getEmailadres(), DBUser.getForname());
        }
    }

}
