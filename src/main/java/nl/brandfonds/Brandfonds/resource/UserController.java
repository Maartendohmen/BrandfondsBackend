package nl.brandfonds.Brandfonds.resource;

import io.jsonwebtoken.Claims;
import nl.brandfonds.Brandfonds.abstraction.*;
import nl.brandfonds.Brandfonds.exceptions.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.security.CustomUserDetails;
import nl.brandfonds.Brandfonds.security.JwtUtil;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    IDepositRequestService depositRequestService;


    @Autowired
    JwtUtil jwtUtil;


    private int GetIDFromJWT(String token) {
        final int[] id = new int[1];
        String cleantoken = token.replace("Bearer ", "");

        jwtUtil.extractClaim(cleantoken, new Function<Claims, Object>() {
            @Override
            public Object apply(Claims claims) {
                id[0] = (int) claims.get("id");
                return id[0];
            }
        });

        return id[0];
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userService.GetAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void Save(@RequestBody User user) {
        userService.Save(user);
    }


    @RequestMapping(path = "/saldo/{id}", method = RequestMethod.GET)
    public long GetUserSaldo(@PathVariable(value = "id", required = false) Integer id,
                             @RequestHeader(name = "Authorization") String token) throws UserNotFoundException {

        if (id == null) {
            id = GetIDFromJWT(token);
        }

        try {
            return userService.GetUserSaldo(id);
        } catch (AopInvocationException ex) {
            throw new UserNotFoundException("De gebruiker kan niet worden opgehaald");
        }
    }


    @RequestMapping(path = "/saldo/{id}", method = RequestMethod.PUT)
    public void SetUserSaldo(@PathVariable("id") Integer id,
                             @RequestBody String amount) throws UserNotFoundException {
        try {
            userService.SetUserSaldo(Long.parseLong(amount), id);
        } catch (Exception x) {
            throw new UserNotFoundException("Het veranderen van het saldo is mislukt, de gebruiker kan niet gevonden worden");
        }
    }

    //region Deposit methods

    /**
     * Create a deposit request to be validated by the brandmaster
     *
     * @param amount The amount of money that is requested
     * @return boolean if transaction was succesfull
     */
    @RequestMapping(path = "/deposit", method = RequestMethod.POST)
    public boolean SetDepositRequest(@RequestHeader(name = "Authorization") String token,
                                     @RequestBody String amount) {

        int id = GetIDFromJWT(token);

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
