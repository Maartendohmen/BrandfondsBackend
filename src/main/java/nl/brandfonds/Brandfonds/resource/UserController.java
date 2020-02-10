package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.util.Mail;
import nl.brandfonds.Brandfonds.model.util.SHA256;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    @Autowired
    PasswordChangeRequestRepository passwordChangeRequestRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public boolean Save(@RequestBody User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    @RequestMapping(path = "/{id}/saldo", method = RequestMethod.GET)
    public long GetUserSaldo(@PathVariable("id") Integer id) {
        return userRepository.GetUserSaldo(id);
    }

    @RequestMapping(path = "/{id}/saldo", method = RequestMethod.PUT)
    public boolean SetUserSaldo(@PathVariable("id") Integer id, @RequestBody String amount)
    {
        try
        {
            userRepository.SetUserSaldo(Long.parseLong(amount),id);
            return true;
        }
        catch (Exception x)
        {
            return false;
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User Login(@RequestBody User user) {
        return userRepository.Login(user.getForname(), user.getPassword());
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public boolean Register(@RequestBody User user) {
        try {
            RegisterRequest request = new RegisterRequest(user.getEmailadres(), user.getForname(), user.getSurname(), user.getPassword());
            registerRequestRepository.save(request);

            Mail.SendRegisterMail(request.getEmailadres(), request.getRandomString());
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    @RequestMapping(path = "/registerconformation/{randomstring}", method = RequestMethod.GET)
    public boolean ConfirmRegistration(@PathVariable("randomstring") String randomstring) {
        RegisterRequest corospondingrequest = registerRequestRepository.GetByrandomString(randomstring);

        if (corospondingrequest != null) {
            userRepository.save(new User(corospondingrequest.getEmailadres(), corospondingrequest.getForname(), corospondingrequest.getSurname(), corospondingrequest.getPassword()));
            registerRequestRepository.delete(corospondingrequest);
            return true;
        }
        return false;
    }




    //region Edit passwords methods
    /**
     * Request password change --> create passwordChangeRequest and and sends mail to user.
     * @param mailadres Mailadres for sending link to.
     * @return
     */
    @RequestMapping(path = "/forgotpassword/{mailadres}", method = RequestMethod.GET)
    public boolean ForgotPassword(@PathVariable("mailadres") String mailadres) {

        if (userRepository.GetByMail(mailadres) != null)
        {
            try {
                PasswordChangeRequest request = new PasswordChangeRequest(mailadres);
                passwordChangeRequestRepository.save(request);

                Mail.SendChangePasswordMail(request.getEmailadres(), request.getRandomstring());
                return true;

            } catch (Exception x) {
                return false;
            }
        }

        return false;
    }

    /**
     * Check if string provided by user is valid.
     * @param randomstring Random string to check
     * @return
     */
    @RequestMapping(path = "/resetpasswordcode/{randomstring}", method = RequestMethod.GET)
    public boolean CheckPasswordString(@PathVariable("randomstring") String randomstring)
    {
            PasswordChangeRequest request = passwordChangeRequestRepository.GetByrandomString(randomstring);

            if (request == null)
            {
                return false;
            }

            return true;
    }

    /**
     * Change password for user
     * @param randomstring String which is used to get attached email
     * @param password New password provided for user
     * @return
     */
    @RequestMapping(path = "/resetpassword/{randomstring}", method = RequestMethod.POST)
    public  boolean ChangePassword(@PathVariable("randomstring") String randomstring, @RequestBody String password)
    {
        try
        {
            PasswordChangeRequest request = passwordChangeRequestRepository.GetByrandomString(randomstring);
            userRepository.UpdatePassword(SHA256.SHA256(password),request.getEmailadres());
            passwordChangeRequestRepository.delete(request);
            return true;
        }
        catch (Exception x)
        {
            return false;
        }
    }
    //endregion


}
