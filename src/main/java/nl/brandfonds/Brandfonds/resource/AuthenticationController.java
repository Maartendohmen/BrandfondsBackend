package nl.brandfonds.Brandfonds.resource;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IMailService;
import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException.UserAlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.DisabledException.UserDisabledException;
import nl.brandfonds.Brandfonds.exceptions.ExpiredException.LinkExpiredException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
    @ApiOperation(value = "Login", notes = "Logs in user using forename and password", nickname = "login")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User was successfully logged in", response = AuthenticationResponse.class),
            @ApiResponse(code = 403, message = "The user is set to inactive", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The user could not be found with the supplied credentials", response = ResponseEntity.class)
    })
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) throws UserDisabledException, NotFoundException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getMailadres(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException(authenticationRequest.getMailadres(), authenticationRequest.getPassword());
        } catch (DisabledException d) {
            throw new UserDisabledException("mailadres", authenticationRequest.getMailadres());
        }

        final CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(authenticationRequest.getMailadres());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt, userService.getByMail(authenticationRequest.getMailadres()).get());

    }

    //region Register methods

    @PostMapping(path = "/register")
    @ApiOperation(value = "Register", notes = "Creates a register request for user and sends verification mail", nickname = "register")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The registration request was successfully created", response = ResponseEntity.class),
            @ApiResponse(code = 409, message = "There is already a user registered with the same mail adres", response = ResponseEntity.class),
    })
    public void register(@RequestBody User user) {

        if (userService.getByMail(user.getMailadres()).isPresent()) {
            throw new UserAlreadyExistException(user.getMailadres());
        }

        RegisterRequest request = new RegisterRequest(user.getMailadres(), user.getForename(),
                user.getSurname(), passwordEncoder.encode(user.getPassword()));
        registerRequestService.save(request);
        mailService.sendRegisterMail(request.getMailadres(), request.getRandomString());
    }


    @GetMapping(path = "/registerconformation/{randomstring}")
    @ApiOperation(value = "Confirm Registration", notes = "Validates registration with generated string and sends activation request to brandmaster", nickname = "registerConformation")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The register request was successfully validated", response = ResponseEntity.class)
    })
    public void confirmRegistration(@PathVariable("randomstring") String randomstring) {


        registerRequestService.getByRandomString(randomstring).ifPresentOrElse(request -> {
            userService.save(new User(request.getMailadres(), request.getForename(),
                    request.getSurname(), request.getPassword()));
            registerRequestService.delete(request);

            mailService.sendUserActivationMail("brandmeester@brandfonds.nl",
                    request.getMailadres(), request.getForename() + " "
                            + request.getSurname(),
                    (userService.getByMail(request.getMailadres()).get().getId()));
        }, () -> {
            throw new LinkExpiredException(randomstring);
        });
    }

    //endregion

    //region Edit passwords methods

    @GetMapping(path = "/forgotpassword/{mailadres}")
    @ApiOperation(value = "Forgot password", notes = "Checks if user exist and sends password change mail to corresponding mail address", nickname = "resetPasswordRequest")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Mail was successfully send to corresponding address", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The provided mailadres is not found", response = ResponseEntity.class)
    })
    public void forgotPassword(@PathVariable("mailadres") String mailadres) throws NotFoundException {
        userService.getByMail(mailadres).ifPresentOrElse(user -> {
                    PasswordChangeRequest request = new PasswordChangeRequest(mailadres);
                    passwordChangeRequestService.save(request);

                    mailService.sendChangePasswordMail(request.getMailadres(), request.getRandomString());
                }, () -> {
                    throw new UserNotFoundException(mailadres);
                }
        );
    }


    @GetMapping(path = "/resetpasswordcode/{randomstring}")
    @ApiOperation(value = "Forgot password validation", notes = "Validated if given string is linked to password change request", nickname = "validateLinkPasswordRequest")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Change password string is validated", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "The token provided is expired/ not found", response = ResponseEntity.class)
            //todo add response for link expired -> add correct code to exception class
    })
    public void checkPasswordResetLink(@PathVariable("randomstring") String randomstring) throws LinkExpiredException {
        passwordChangeRequestService.getByRandomString(randomstring).orElseThrow(() -> new LinkExpiredException(randomstring));
    }


    @PostMapping(path = "/resetpassword/{randomstring}")
    @ApiOperation(value = "Change password", notes = "Changes password to new value", nickname = "confirmPasswordRequest")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Password was succesfully changed", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "The token provided is expired/ not found", response = ResponseEntity.class)
    })
    public void changePassword(@PathVariable("randomstring") String randomstring, @RequestBody String password) throws LinkExpiredException {
        passwordChangeRequestService.getByRandomString(randomstring).ifPresentOrElse(request -> {
            userService.updatePassword(passwordEncoder.encode(password), request.getMailadres());
            passwordChangeRequestService.delete(request);
        }, () -> {
            throw new LinkExpiredException(randomstring);
        });
    }
    //endregion


    @GetMapping(path = "/activate-user/{id}")
    @ApiOperation(value = "Set user activation", notes = "Set user active/inactive", nickname = "setUserActivation")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User status successfully updated", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "User could not be found", response = ResponseEntity.class)
    })
    public void setUserActivation(@PathVariable("id") Integer id, @RequestParam("is-activated") boolean isActivated) throws UserDisabledException {
        userService.getOne(id).ifPresentOrElse(user -> {
            user.setActivated(isActivated);
            userService.save(user);

            this.mailService.sendUserActivatedMail(user.getMailadres(), user.getMailadres(), user.getForename());
        }, () -> {
            throw new UserNotFoundException(id);
        });
    }

}
