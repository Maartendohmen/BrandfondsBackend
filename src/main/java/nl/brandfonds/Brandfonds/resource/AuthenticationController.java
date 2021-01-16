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
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.exceptions.UserDisabledException;
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
            @ApiResponse(code = 200, message = "User was successfully logged in", response = AuthenticationResponse.class),
            @ApiResponse(code = 403, message = "The user is set to inactive", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The user could not be found with the supplied credentials", response = ResponseEntity.class)
    })
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) throws UserDisabledException, NotFoundException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new NotFoundException("De ingevoerde voornaam of het wachtwoord is fout");
        }

        final CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        if (!userDetails.isEnabled()) {
            throw new UserDisabledException("Het account waarmee je probeert in te loggen is uitgeschakeld, wacht of neem contact op met de brandmeester totdat je account is geactiveerd");
        }

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt, userService.getByName(authenticationRequest.getUsername()).get());

    }

    //region Register methods

    @PostMapping(path = "/register")
    @ApiOperation(value = "Register", notes = "Creates a register request for user and sends verification mail", response = ResponseEntity.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "The registration request was successfully created", response = ResponseEntity.class),
            @ApiResponse(code = 409, message = "There is already a user registered with the same mail adres", response = ResponseEntity.class),
    })
    public void register(@RequestBody User user) throws AlreadyExistException {

        if (userService.getByMail(user.getEmailadres()).isPresent()) {
            throw new AlreadyExistException("Er is al een account met dit emailadres, het is mogelijk om een nieuw wachtwoord op te vragen.");
        }

        RegisterRequest request = new RegisterRequest(user.getEmailadres(), user.getForname(),
                user.getSurname(), passwordEncoder.encode(user.getPassword()));
        registerRequestService.save(request);
        mailService.sendRegisterMail(request.getEmailadres(), request.getRandomString());
    }


    @GetMapping(path = "/registerconformation/{randomstring}")
    @ApiOperation(value = "Confirm Registration", notes = "Validates registration with generated string and sends activation request to brandmaster")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The register request was successfully validated", response = ResponseEntity.class)
            //todo add response for link expired -> add correct code to exception class
    })
    public void confirmRegistration(@PathVariable("randomstring") String randomstring) throws LinkExpiredException {

        registerRequestService.getByrandomString(randomstring).ifPresent(request -> {

            userService.save(new User(request.getEmailadres(), request.getForname(),
                    request.getSurname(), request.getPassword()));
            registerRequestService.delete(request);

            mailService.sendUserActivationMail("brandmeester@brandfonds.nl",
                    request.getEmailadres(), request.getForname() + " "
                            + request.getSurname(),
                    (userService.getByMail(request.getEmailadres()).get().getId()));

            return;
        });

        throw new LinkExpiredException("De gebruikte link is ongeldig of verlopen.");
    }

    //endregion

    //region Edit passwords methods

    @GetMapping(path = "/forgotpassword/{mailadres}")
    @ApiOperation(value = "Forgot password", notes = "Checks if user exist and sends password change mail to corresponding mail address")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Mail was successfully send to corresponding address", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The provided emailadress is not found", response = ResponseEntity.class)
    })
    public void forgotPassword(@PathVariable("mailadres") String mailadres) throws NotFoundException {

        userService.getByMail(mailadres).ifPresent(user -> {
                    PasswordChangeRequest request = new PasswordChangeRequest(mailadres);
                    passwordChangeRequestService.save(request);

                    mailService.sendChangePasswordMail(request.getEmailadres(), request.getRandomstring());
                    return;
                }
        );

        throw new NotFoundException("Er kan geen gebruiker met dit mailadres worden gevonden");
    }


    @GetMapping(path = "/resetpasswordcode/{randomstring}")
    @ApiOperation(value = "Forgot password validation", notes = "Validated if given string is linked to password change request")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Change password string is validated", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "The token provided is expired/ not found", response = ResponseEntity.class)
            //todo add response for link expired -> add correct code to exception class
    })
    public void checkPasswordLink(@PathVariable("randomstring") String randomstring) throws LinkExpiredException {
        if (!passwordChangeRequestService.getByrandomString(randomstring).isPresent()) {
            throw new LinkExpiredException("De link die je probeert te gebruiken is verlopen");
        }
    }


    @PostMapping(path = "/resetpassword/{randomstring}")
    @ApiOperation(value = "Change password", notes = "Changes password to new value")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Password was succesfully changed", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "The token provided is expired/ not found", response = ResponseEntity.class)
    })
    public void changePassword(@PathVariable("randomstring") String randomstring, @RequestBody String password) throws LinkExpiredException {
        passwordChangeRequestService.getByrandomString(randomstring).ifPresent(request -> {
            userService.updatePassword(passwordEncoder.encode(password), request.getEmailadres());
            passwordChangeRequestService.delete(request);
        });
        throw new LinkExpiredException("De link die je probeert te gebruiken is verlopen");
    }
    //endregion


    @GetMapping(path = "/activate-user/{id}/{is-activated}")
    @ApiOperation(value = "Set user activation", notes = "Set user active/inactive")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User status successfully updated", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "User could not be found", response = ResponseEntity.class)
    })
    public void activateUser(@PathVariable("id") Integer id, @PathVariable("is-activated") boolean isActivated) throws UserDisabledException {

        userService.getByID(id).ifPresent(user -> {
            user.setActivated(isActivated);
            userService.save(user);

            this.mailService.sendUserActivatedMail(user.getEmailadres(), user.getEmailadres(), user.getForname());
            return;
        });

        throw new UserDisabledException("De gebruiker die je wilt activeren staat niet meer in het systeem");

    }

}
