package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.security.AuthenticationRequest;
import nl.brandfonds.Brandfonds.security.AuthenticationResponse;

@Api(tags = "Authentication", description = "User authorization operations")
public interface AuthenticationController {

    @ApiOperation(value = "Login", nickname = "Login", notes = "Logs in user using forename and password")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User was successfully logged in", response = AuthenticationResponse.class),
            @ApiResponse(code = 403, message = "The user is set to inactive"),
            @ApiResponse(code = 404, message = "The user could not be found with the supplied credentials")
    })
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    @ApiOperation(value = "Register", nickname = "Register", notes = "Creates a register request for user and sends verification mail")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The registration request was successfully created"),
            @ApiResponse(code = 409, message = "There is already a user registered with the same mailadres"),
    })
    void register(User user);

    @ApiOperation(value = "Confirm Registration", nickname = "registrationConformation", notes = "Validates registration with generated string and sends activation request to brandmaster")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The register request was successfully validated"),
            @ApiResponse(code = 401, message = "The token provided is expired/not found")
    })
    void checkRegistrationCode(String registrationCode);

    @ApiOperation(value = "Forgot password", nickname = "forgotPassword", notes = "Checks if user exist and sends password change mail to corresponding mailadres")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Mail was successfully send to corresponding address"),
            @ApiResponse(code = 404, message = "The provided mailadres is not found")
    })
    void forgotPassword(String mailadres);

    @ApiOperation(value = "Forgot password validation", nickname = "forgotPasswordValidation", notes = "Validated if given string is linked to password change request")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Change password string is validated"),
            @ApiResponse(code = 401, message = "The token provided is expired/not found")
    })
    void checkPasswordResetCode(String passwordResetCode);

    @ApiOperation(value = "Change password", notes = "Changes password to new value", nickname = "changePassword")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Password was successfully changed"),
            @ApiResponse(code = 401, message = "The token provided is expired/not found")
    })
    void changePassword(String passwordResetCode, String newPassword);

    @ApiOperation(value = "Set user activation", notes = "Set user active/inactive", nickname = "setUserActivation")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User status successfully updated"),
            @ApiResponse(code = 404, message = "User could not be found")
    })
    void setUserActivation(Integer userId, Boolean isActivated);
}
