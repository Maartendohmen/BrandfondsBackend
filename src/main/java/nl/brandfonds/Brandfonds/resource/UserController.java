package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IDepositRequestService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    IDepositRequestService depositRequestService;


    @GetMapping
    @ApiOperation(value = "Get users", notes = "Get all registered users")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Users successfully retrieved")
    })
    public List<User> getAll() {
        return userService.GetAll();
    }

    @PostMapping
    @ApiOperation(value = "Save user", notes = "Saves a user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "User was successfully saved")
    })
    public void Save(@RequestBody User user) {
        userService.Save(user);
    }

    @GetMapping(path = "/{id}/saldo")
    @ApiOperation(value = "Get saldo user", notes = "Gets the saldo from a user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Saldo was successfully retrieved")
    })
    public long GetUserSaldo(@PathVariable(value = "id") Integer id) throws UserNotFoundException {

        try {
            return userService.GetUserSaldo(id);
        } catch (AopInvocationException ex) {
            throw new UserNotFoundException("De gebruiker kan niet worden opgehaald");
        }
    }

    @PutMapping(path = "/saldo/{id}")
    @ApiOperation(value = "Update user saldo", notes = "Updates the current saldo of a user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Saldo was successfully updated")
    })
    public void SetUserSaldo(@PathVariable("id") Integer id,
                             @RequestBody String amount) throws UserNotFoundException {
        try {
            userService.SetUserSaldo(Long.parseLong(amount), id);
        } catch (Exception x) {
            throw new UserNotFoundException("Het veranderen van het saldo is mislukt, de gebruiker kan niet gevonden worden");
        }
    }

    //region Deposit methods

    @PostMapping(path = "/{id/deposit")
    @ApiOperation(value = "Create deposit request", notes = "Creates a deposit request with given amount")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Depositrequest was successfully created")
    })
    public void SetDepositRequest(@PathVariable("id") Integer id, @RequestBody String amount) {
            depositRequestService.Save(new DepositRequest(userService.GetOne(id), Long.parseLong(amount)));
    }


    @GetMapping(path = "/deposit")
    @ApiOperation(value = "Get deposit requests", notes = "Get's all deposit requests")
    @ApiResponses({
            @ApiResponse(code= 200,message = "depositrequests were successfully retrieved")
    })
    public List<DepositRequest> GetDepositRequest() {
        return depositRequestService.GetAll();
    }


    //@Todo check if these methods could be merged into one & add annotations for swagger
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

    //@Todo check if these methods could be merged into one & add annotations for swagger
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
