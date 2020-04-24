package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.abstraction.*;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.util.SHA256;
import nl.brandfonds.Brandfonds.repository.PasswordChangeRequestRepository;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
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
    IDepositRequestService depositRequestService;

    @Autowired
    IMailService mailService;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userService.GetAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void Save(@RequestBody User user) {
        userService.Save(user);
    }


    //#exception handling implemented
    @RequestMapping(path = "/{id}/saldo", method = RequestMethod.GET)
    public long GetUserSaldo(@PathVariable("id") Integer id) throws UserNotFoundException {
        try {
            return userService.GetUserSaldo(id);
        } catch (AopInvocationException ex) {
            throw new UserNotFoundException("De gebruiker kan niet worden opgehaald");
        }

    }

    @RequestMapping(path = "/{id}/saldo", method = RequestMethod.PUT)
    public void SetUserSaldo(@PathVariable("id") Integer id, @RequestBody String amount) throws UserNotFoundException {
        try {
            userService.SetUserSaldo(Long.parseLong(amount), id);
        } catch (Exception x) {
            throw new UserNotFoundException("Het veranderen van het saldo is mislukt, de gebruiker kan niet gevonden worden");
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User Login(@RequestBody User user) {
        return userService.Login(user.getForname(), user.getPassword());
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
            RegisterRequest request = new RegisterRequest(user.getEmailadres(), user.getForname(), user.getSurname(), user.getPassword());
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
    public boolean ConfirmRegistration(@PathVariable("randomstring") String randomstring) throws AlreadyExistException {
        RegisterRequest corospondingrequest = registerRequestService.GetByrandomString(randomstring);

        try {
            if (corospondingrequest != null) {
                userService.Save(new User(corospondingrequest.getEmailadres(), corospondingrequest.getForname(), corospondingrequest.getSurname(), corospondingrequest.getPassword()));
                registerRequestService.Delete(corospondingrequest);
            }
            return true;
        } catch (Exception ex) {
            throw new AlreadyExistException("Er bestaat al een gebruiker met dit emailadres");
        }

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
            userService.UpdatePassword(SHA256.SHA256(password), request.getEmailadres());
            passwordChangeRequestService.Delete(request);
            return true;
        } catch (Exception x) {
            return false;
        }
    }
    //endregion

    //region Deposit methods

    /**
     * Create a deposit request to be validated by the brandmaster
     *
     * @param id     The user id who creates the request
     * @param amount The amount of money that is requested
     * @return boolean if transaction was succesfull
     */
    @RequestMapping(path = "/{id}/deposit", method = RequestMethod.POST)
    public boolean SetDepositRequest(@PathVariable("id") Integer id, @RequestBody String amount) {
        try {
            depositRequestService.Save(new DepositRequest(userService.GetOne(id), Long.parseLong(amount)));
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    /**
     * Get all depositrequests
     *
     * @return List off all depositrequests
     */
    @RequestMapping(path = "/deposit", method = RequestMethod.GET)
    public List<DepositRequest> GetDepositRequest() {
        return depositRequestService.GetAll();
    }

    /**
     * Approve a depositrequest from user
     *
     * @param id the id of the Depositrequest to approve
     * @return boolean if transaction was succesfull
     */
    @RequestMapping(path = "/depositapprove/{id}", method = RequestMethod.GET)
    public boolean ApproveDepositRequest(@PathVariable("id") Integer id) {
        try {
            DepositRequest request = depositRequestService.GetOne(id);

            User user = userService.GetOne(request.getUser().getId());
            user.setSaldo(user.getSaldo() + request.getAmount());
            userService.Save(user);

            request.ValidateRequest();
            depositRequestService.Save(request);
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    /**
     * Reject a depositrequest from user
     *
     * @param id the id of the Depositrequest to reject
     * @return boolean if transaction was succesfull
     */
    @RequestMapping(path = "/depositreject/{id}", method = RequestMethod.GET)
    public boolean RejectDepositRequest(@PathVariable("id") Integer id) {
        try {
            DepositRequest request = depositRequestService.GetOne(id);
            request.setHandledDate(new Date());
            depositRequestService.Save(request);
            return true;
        } catch (Exception x) {
            return false;
        }
    }
    //endregion


}
