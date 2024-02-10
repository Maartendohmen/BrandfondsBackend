package nl.brandfonds.Brandfonds.resource;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.brandfonds.Brandfonds.model.security.SignInResponse;
import nl.brandfonds.Brandfonds.model.security.SignUpRequest;

@Tag(name = "Authentication", description = "Authentication operations")
public interface AuthenticationController {

    @Operation(summary = "Login", operationId = "Login", description = "Logs in user using forename and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was successfully logged in", content = {@Content(schema = @Schema(implementation = SignInResponse.class, description = "Jwt token + user details"))}),
            @ApiResponse(responseCode = "403", description = "The user is set to inactive"),
            @ApiResponse(responseCode = "404", description = "The user could not be found with the supplied credentials")
    })
    SignInResponse login(String email, String password);

    @Operation(summary = "Register", operationId = "Register", description = "Creates a register request for user and sends verification mail")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The registration request was successfully created", content = {@Content(schema = @Schema(implementation = String.class, description = "Jwt token"))}),
            @ApiResponse(responseCode = "409", description = "There is already a user registered with the same email"),
    })
    void requestRegistration(SignUpRequest signUpRequest);

    @Operation(summary = "Confirm Registration", operationId = "registrationConformation", description = "Validates registration with generated string and sends activation request to brandmaster")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The register request was successfully validated"),
            @ApiResponse(responseCode = "401", description = "The token provided is expired/not found")
    })
    String confirmRegistration(String registrationCode);

    @Operation(summary = "Forgot password", operationId = "forgotPassword", description = "Checks if user exist and sends password change mail to corresponding email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mail was successfully send to corresponding address"),
            @ApiResponse(responseCode = "404", description = "The provided email is not found")
    })
    void forgotPassword(String email);

    @Operation(summary = "Forgot password validation", operationId = "forgotPasswordValidation", description = "Validated if given string is linked to password change request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Change password string is validated"),
            @ApiResponse(responseCode = "401", description = "The token provided is expired/not found")
    })
    void checkPasswordResetCode(String passwordResetCode);

    @Operation(summary = "Change password", operationId = "changePassword", description = "Changes password to new value")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password was successfully changed"),
            @ApiResponse(responseCode = "401", description = "The token provided is expired/not found")
    })
    void changePassword(String passwordResetCode, String newPassword);

    @Operation(summary = "Set user activation", operationId = "setUserActivation", description = "Set user active/inactive")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User status successfully updated"),
            @ApiResponse(responseCode = "404", description = "User could not be found")
    })
    void setUserActivation(Integer userId);
}
