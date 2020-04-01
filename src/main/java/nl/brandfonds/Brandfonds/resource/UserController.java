package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.abstraction.IPasswordChangeRequestService;
import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.abstraction.IMailService;
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
    IUserService userService;

    @Autowired
    IRegisterRequestService registerRequestService;

    @Autowired
    IPasswordChangeRequestService passwordChangeRequestService;

    @Autowired
    IMailService mailService;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userService.GetAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public boolean Save(@RequestBody User user) {
        try {
            userService.Save(user);
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    @RequestMapping(path = "/{id}/saldo", method = RequestMethod.GET)
    public long GetUserSaldo(@PathVariable("id") Integer id) {
        return userService.GetUserSaldo(id);
    }

    @RequestMapping(path = "/{id}/saldo", method = RequestMethod.PUT)
    public boolean SetUserSaldo(@PathVariable("id") Integer id, @RequestBody String amount) {
        try {
            userService.SetUserSaldo(Long.parseLong(amount), id);
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User Login(@RequestBody User user) {
        return userService.Login(user.getForname(), user.getPassword());
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public boolean Register(@RequestBody User user) {
        try {
            RegisterRequest request = new RegisterRequest(user.getEmailadres(), user.getForname(), user.getSurname(), user.getPassword());
            registerRequestService.Save(request);

            mailService.SendRegisterMail(request.getEmailadres(), request.getRandomString());
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    @RequestMapping(path = "/registerconformation/{randomstring}", method = RequestMethod.GET)
    public boolean ConfirmRegistration(@PathVariable("randomstring") String randomstring) {
        RegisterRequest corospondingrequest = registerRequestService.GetByrandomString(randomstring);

        if (corospondingrequest != null) {
            userService.Save(new User(corospondingrequest.getEmailadres(), corospondingrequest.getForname(), corospondingrequest.getSurname(), corospondingrequest.getPassword()));
            registerRequestService.Delete(corospondingrequest);
            return true;
        }
        return false;
    }


    //region Edit passwords methods

    /**
     * Request password change --> create passwordChangeRequest and and sends mail to user.
     *
     * @param mailadres Mailadres for sending link to.
     * @return
     */
    @RequestMapping(path = "/forgotpassword/{mailadres}", method = RequestMethod.GET)
    public boolean ForgotPassword(@PathVariable("mailadres") String mailadres) {

        if (userService.GetByMail(mailadres) != null) {
            try {
                PasswordChangeRequest request = new PasswordChangeRequest(mailadres);
                passwordChangeRequestService.Save(request);

                mailService.SendChangePasswordMail(request.getEmailadres(), request.getRandomstring());
                return true;

            } catch (Exception x) {
                return false;
            }
        }

        return false;

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
            userService.UpdatePassword(SHA256.SHA256(password), request.getEmailadres());
            passwordChangeRequestService.Delete(request);
            return true;
        } catch (Exception x) {
            return false;
        }
    }
    //endregion


}
