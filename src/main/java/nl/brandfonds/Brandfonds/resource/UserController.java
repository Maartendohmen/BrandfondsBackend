package nl.brandfonds.Brandfonds.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "User", description = "User operations")
@SecurityRequirement(name = "Bearer_Authentication")
public interface UserController {

    @Operation(summary = "Get users", operationId = "getAllUsers", description = "Get all registered normal users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved")
    })
    List<User> getAll();

    @Operation(summary = "Get saldo user", operationId = "getUserSaldo", description = "Gets the saldo from a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saldo was successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "The requested user could not be found"),

    })
    Long getUserSaldo(Integer id);

    @Operation(summary = "Update user saldo", operationId = "updateUserSaldo", description = "Updates the current saldo of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saldo was successfully updated"),
            @ApiResponse(responseCode = "404", description = "The requested user could not be found"),
    })
    void setUserSaldo(Integer id, Long amount);

    @Operation(summary = "Update user profile picture", operationId = "uploadUserProfilePicture", description = "Updates the current profile picture of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile picture was successfully updated"),
            @ApiResponse(responseCode = "404", description = "The requested user could not be found"),
    })
    void setUserProfilePicture(Integer id, @RequestParam("image") MultipartFile file) throws IOException;

    @Operation(summary = "Get user profile picture", operationId = "getProfilePicture", description = "Get the current profile picture of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile picture was successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "The requested user could not be found"),
    })
    byte[] getUserProfilePicture(Integer id) throws IOException;

    @Operation(summary = "Create deposit request", operationId = "createDepositRequest", description = "Creates a deposit request with given amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Depositrequest was successfully created"),
            @ApiResponse(responseCode = "404", description = "The requested user could not be found")
    })
    void setDepositRequest(Integer id, String amount);

    @Operation(summary = "Get deposit requests", operationId = "getDepositRequest", description = "Gets all deposit requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "depositrequests were successfully retrieved")
    })
    List<DepositRequest> getDepositRequests();

    @Operation(summary = "Set deposit status", operationId = "setDepositRequest", description = "Sets the status of the deposit to approved/unapproved")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "depositrequests was successfully edited")
    })
    void handleDepositRequest(Integer id, Boolean approve);
}
