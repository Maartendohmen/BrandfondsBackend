package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.*;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "User", description = "User operations")
public interface UserController {

    @ApiOperation(value = "Get users", nickname = "getAllUsers", notes = "Get all registered users", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Users successfully retrieved", response = User.class, responseContainer = "List")
    })
    List<User> getAll();

    @ApiOperation(value = "Save user", nickname = "saveUser", notes = "Saves a user", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "User was successfully saved", response = ResponseEntity.class)
    })
    void save(User user);

    @ApiOperation(value = "Get saldo user", nickname = "getUserSaldo", notes = "Gets the saldo from a user", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Saldo was successfully retrieved", response = Long.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = ResponseEntity.class),

    })
    Long getUserSaldo(Integer id);

    @ApiOperation(value = "Update user saldo", nickname = "updateUserSaldo", notes = "Updates the current saldo of a user", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Saldo was successfully updated", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = ResponseEntity.class),
    })
    void setUserSaldo(Integer id, Long amount);

    @ApiOperation(value = "Update user profile picture", nickname = "uploadUserProfilePicture", notes = "Updates the current profile picture of a user", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Profile picture was successfully updated", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = ResponseEntity.class),
    })
    void setUserProfilePicture(Integer id, MultipartFile file) throws IOException;

    @ApiOperation(value = "Get user profile picture", nickname = "getEncodedUserProfilePicture", notes = "Get the current profile picture of a user", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Profile picture was successfully retrieved", response = String.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = ResponseEntity.class),
    })
    String getEncodedUserProfilePicture(Integer id) throws IOException;

    @ApiOperation(value = "Create deposit request", nickname = "createDepositRequest", notes = "Creates a deposit request with given amount", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Depositrequest was successfully created", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = ResponseEntity.class)
    })
    void setDepositRequest(Integer id, String amount);

    @ApiOperation(value = "Get deposit requests", nickname = "getDepositRequest", notes = "Gets all deposit requests", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "depositrequests were successfully retrieved", response = DepositRequest.class, responseContainer = "List")
    })
    List<DepositRequest> getDepositRequests();

    @ApiOperation(value = "Set deposit status", nickname = "setDepositRequest", notes = "Sets the status of the deposit to approved/unapproved", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "depositrequests was successfully edited")
    })
    void handleDepositRequest(Integer id, Boolean approve);
}
