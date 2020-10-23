package nl.brandfonds.Brandfonds.resource;

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
import nl.brandfonds.Brandfonds.model.UserRole;
import nl.brandfonds.Brandfonds.security.AuthenticationRequest;
import nl.brandfonds.Brandfonds.security.AuthenticationResponse;
import nl.brandfonds.Brandfonds.security.JwtUtil;
import nl.brandfonds.Brandfonds.security.MyUserDetailsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    private MyUserDetailsServer userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> Login(@RequestBody AuthenticationRequest authenticationRequest) throws UserNotFoundException, UserDisabledException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("De ingevoerde voornaam of het wachtwoord is fout");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        //Add role for checking front-end
        for (GrantedAuthority g : userDetails.getAuthorities()){
            if (g.getAuthority() == "BRANDMASTER"){
                return ResponseEntity.ok(new AuthenticationResponse(jwt, UserRole.BRANDMASTER));
            }
        }

        return ResponseEntity.ok(new AuthenticationResponse(jwt, UserRole.NORMAL));

    }

    //region Register methods

    /**
     * Create a registerrequest for a user
     *
     * @param user The user object that is gonna be created
     * @return boolean if transaction was succesfull
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public boolean Register(@RequestBody User user) {

        try {
            RegisterRequest request = new RegisterRequest(user.getEmailadres(), user.getForname(),
                    user.getSurname(), passwordEncoder.encode(user.getPassword()));
            registerRequestService.Save(request);

            mailService.SendRegisterMail(request.getEmailadres(), request.getRandomString());
            return true;
        } catch (Exception x) {
            System.out.println(x);
        }
        return false;
    }

    /**
     * Confirm account creation and create account
     *
     * @param randomstring The string that has to match the registerrequest string
     * @return boolean if transaction was succesfull
     * @throws AlreadyExistException
     */
    @RequestMapping(path = "/registerconformation/{randomstring}", method = RequestMethod.GET)
    public boolean ConfirmRegistration(@PathVariable("randomstring") String randomstring) throws AlreadyExistException, LinkExpiredException {
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
        return true;
    }

    //endregion

    //region Edit passwords methods

    /**
     * Request password change --> create passwordChangeRequest and and sends mail to user.
     *
     * @param mailadres Mailadres for sending link to.
     * @return
     */
    @RequestMapping(path = "/forgotpassword/{mailadres}", method = RequestMethod.GET)
    public boolean ForgotPassword(@PathVariable("mailadres") String mailadres) throws UserNotFoundException {

        if (userService.GetByMail(mailadres) != null) {

            PasswordChangeRequest request = new PasswordChangeRequest(mailadres);
            passwordChangeRequestService.Save(request);

            mailService.SendChangePasswordMail(request.getEmailadres(), request.getRandomstring());
            return true;
        }

        throw new UserNotFoundException("Er kan geen gebruiker met dit mailadres worden gevonden");
    }

    /**
     * Check if string provided by user is valid.
     *
     * @param randomstring Random string to check
     * @return
     */
    @RequestMapping(path = "/resetpasswordcode/{randomstring}", method = RequestMethod.GET)
    public boolean CheckPasswordString(@PathVariable("randomstring") String randomstring) {
        PasswordChangeRequest request = passwordChangeRequestService.GetByrandomString(randomstring);

        if (request == null) {
            return false;
        }

        return true;
    }

    /**
     * Change password for user
     *
     * @param randomstring String which is used to get attached email
     * @param password     New password provided for user
     * @return
     */
    @RequestMapping(path = "/resetpassword/{randomstring}", method = RequestMethod.POST)
    public boolean ChangePassword(@PathVariable("randomstring") String randomstring, @RequestBody String password) {
        try {
            PasswordChangeRequest request = passwordChangeRequestService.GetByrandomString(randomstring);
            userService.UpdatePassword(passwordEncoder.encode(password), request.getEmailadres());
            passwordChangeRequestService.Delete(request);
            return true;
        } catch (Exception x) {
            return false;
        }
    }
    //endregion


}
