package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import nl.brandfonds.Brandfonds.abstraction.IDepositRequestService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException.DepositRequestNotFoundException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    IDepositRequestService depositRequestService;


    @GetMapping
    @ApiOperation(value = "Get users", notes = "Get all registered users", nickname = "getAllUsers", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Users successfully retrieved", response = User.class, responseContainer = "List")
    })
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    @ApiOperation(value = "Save user", notes = "Saves a user", nickname = "saveUser", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "User was successfully saved", response = ResponseEntity.class)
    })
    public void save(@RequestBody User user) {
        userService.save(user);
    }

    @GetMapping(path = "/{id}/saldo")
    @ApiOperation(value = "Get saldo user", notes = "Gets the saldo from a user", nickname = "getSaldoFromUser", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Saldo was successfully retrieved", response = Long.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = UserNotFoundException.class),

    })
    public long getUserSaldo(@PathVariable(value = "id") Integer id) {

        userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));

        return userService.getUserSaldo(id);
    }

    @PutMapping(path = "/saldo/{id}")
    @ApiOperation(value = "Update user saldo", notes = "Updates the current saldo of a user", response = ResponseEntity.class, authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Saldo was successfully updated"),
            @ApiResponse(code = 404, message = "The requested user could not be found"),
    })
    public void setUserSaldo(@PathVariable("id") Integer id,
                             @RequestBody Long amount) {

        userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));

        userService.setUserSaldo(amount, id);
    }

    @PostMapping(path = "/profile_picture/{id}")
    @ApiOperation(value = "Update user profile picture", notes = "Updates the current profile picture of a user", nickname = "setUserProfilePicture", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Profile picture was successfully updated"),
            @ApiResponse(code = 404, message = "The requested user could not be found"),
    })
    public void setUserProfilePicture(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        var user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));
        userService.saveProfilePicture(user, file);
    }

    @GetMapping(path = "/profile_picture/{id}")
    @ApiOperation(value = "Get user profile picture", notes = "Get the current profile picture of a user", nickname = "getUserProfilePicture", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Profile picture was successfully retrieved"),
            @ApiResponse(code = 404, message = "The requested user could not be found"),
    })
    public String getEncodedUserProfilePicture(@PathVariable("id") Integer id) throws IOException {
        var user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));

        return userService.getEncodedProfilePicture(user);
    }

    //region Deposit methods

    @PostMapping(path = "/{id}/deposit")
    @ApiOperation(value = "Create deposit request", notes = "Creates a deposit request with given amount", nickname = "createDepositRequest", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Depositrequest was successfully created", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = UserNotFoundException.class)
    })
    public void setDepositRequest(@PathVariable("id") Integer id, @RequestBody String amount) {
        depositRequestService.save(new DepositRequest(userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id)), Long.parseLong(amount)));
    }


    @GetMapping(path = "/deposit")
    @ApiOperation(value = "Get deposit requests", notes = "Get's all deposit requests", nickname = "getAllDeposits", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "depositrequests were successfully retrieved", response = DepositRequest.class, responseContainer = "List")
    })
    public List<DepositRequest> getDepositRequests() {
        return depositRequestService.getAll();
    }


    @RequestMapping(path = "/deposithandling/{id}/{approve}", method = RequestMethod.GET)
    @ApiOperation(value = "Set deposit status", notes = "Sets the status of the deposit to approved/unapproved", nickname = "setDepositStatus", authorizations = @Authorization(value = "jwtToken"))
    public void handleDepositRequest(@PathVariable("id") Integer id, @PathVariable("approve") Boolean approve) {

        var depositRequest = depositRequestService.getOne(id).orElseThrow(() -> new DepositRequestNotFoundException(id));

        if (approve) {
            var user = userService.getOne(depositRequest.getUser().getId()).orElseThrow(() -> new UserNotFoundException("ID", id.toString()));
            user.setSaldo(user.getSaldo() + depositRequest.getAmount());
            userService.save(user);

            depositRequest.ValidateRequest();
        } else {
            depositRequest.setHandledDate(LocalDateTime.now());
        }
        depositRequestService.save(depositRequest);
    }

    //endregion


}
