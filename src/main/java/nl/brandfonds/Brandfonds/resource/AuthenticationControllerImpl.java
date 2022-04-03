package nl.brandfonds.Brandfonds.resource;


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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "rest/auth")
public class AuthenticationControllerImpl implements AuthenticationController {

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

    @PostMapping(path = "/register")
    public void register(@RequestBody User user) {

        if (userService.getByMail(user.getMailadres()).isPresent()) {
            throw new UserAlreadyExistException(user.getMailadres());
        }

        var registerRequest = new RegisterRequest(user.getMailadres(), user.getForename(),
                user.getSurname(), passwordEncoder.encode(user.getPassword()));
        registerRequestService.save(registerRequest);
        mailService.sendRegisterMail(registerRequest.getMailadres(), registerRequest.getRandomString());
    }


    @GetMapping(path = "/registerconformation/{registrationcode}")
    public void checkRegistrationCode(@PathVariable("registrationcode") String registrationCode) {

        registerRequestService.getByRandomString(registrationCode).ifPresentOrElse(request -> {
            userService.save(new User(request.getMailadres(), request.getForename(),
                    request.getSurname(), request.getPassword()));
            registerRequestService.delete(request);

            mailService.sendUserActivationMail("brandmeester@brandfonds.nl",
                    request.getMailadres(), request.getForename() + " "
                            + request.getSurname(),
                    userService.getByMail(request.getMailadres()).get().getId());
        }, () -> {
            throw new LinkExpiredException(registrationCode);
        });
    }

    @GetMapping(path = "/forgotpassword/{mailadres}")
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


    @GetMapping(path = "/resetpasswordcode/{passwordResetCode}")
    public void checkPasswordResetCode(@PathVariable("passwordResetCode") String passwordResetCode) throws LinkExpiredException {
        passwordChangeRequestService.getByRandomString(passwordResetCode).orElseThrow(() -> new LinkExpiredException(passwordResetCode));
    }


    @PostMapping(path = "/resetpassword/{passwordResetCode}")
    public void changePassword(@PathVariable("passwordResetCode") String passwordResetCode, @RequestBody String password) throws LinkExpiredException {
        passwordChangeRequestService.getByRandomString(passwordResetCode).ifPresentOrElse(request -> {
            userService.updatePassword(passwordEncoder.encode(password), request.getMailadres());
            passwordChangeRequestService.delete(request);
        }, () -> {
            throw new LinkExpiredException(passwordResetCode);
        });
    }

    @GetMapping(path = "/activate-user/{userId}")
    public void setUserActivation(@PathVariable("userId") Integer userId, @RequestParam("is-activated") Boolean isActivated) throws UserDisabledException {
        userService.getOne(userId).ifPresentOrElse(user -> {
            user.setActivated(isActivated);
            userService.save(user);

            this.mailService.sendUserActivatedMail(user.getMailadres(), user.getMailadres(), user.getForename());
        }, () -> {
            throw new UserNotFoundException(userId);
        });
    }

}
