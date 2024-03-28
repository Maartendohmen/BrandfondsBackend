package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.UserRole;
import nl.brandfonds.Brandfonds.services.DepositRequestService;
import nl.brandfonds.Brandfonds.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/user")
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @Autowired
    DepositRequestService depositRequestService;

    @GetMapping
    public List<User> getAll(@RequestParam(defaultValue = "true") Boolean excludeDeactivated) {
        var users = userService.getAll().stream().filter(user -> user.getRole() == UserRole.NORMAL).sorted(User::compareTo);

        if (excludeDeactivated) {
            users = users.filter(User::isActivated);
        }
        return users.toList();
    }

    @GetMapping(path = "/saldo/{userId}")
    public Float getUserSaldo(@PathVariable(value = "userId") Integer userId) {
        return userService.getById(userId).getSaldo();
    }

    @PutMapping(path = "/saldo/{userId}")
    public void setUserSaldo(@PathVariable("userId") Integer userId,
                             @RequestBody Float amount) {
        var changingUser = userService.getById(userId);
        changingUser.setSaldo(amount);
        userService.save(changingUser);
    }

    @PostMapping(path = "/profilePicture/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void setUserProfilePicture(@PathVariable("userId") Integer userId, @RequestParam("image") MultipartFile file) throws IOException {
        userService.saveProfilePicture(userId, file);
    }

    @GetMapping(path = "/profilePicture/{userId}", produces = {
            MediaType.IMAGE_JPEG_VALUE
    })
    public byte[] getUserProfilePicture(@PathVariable("userId") Integer userId) throws IOException {
        return userService.getEncodedProfilePicture(userId);
    }

    @PostMapping(path = "/deposit/{userId}")
    public void setDepositRequest(@PathVariable("userId") Integer userId, @RequestBody Float amount) {
        depositRequestService.save(new DepositRequest(userService.getById(userId), amount));
    }

    //todo maybe add method for getting deposit requests for single user

    @GetMapping(path = "/deposit")
    public List<DepositRequest> getDepositRequests(@RequestParam(defaultValue = "false") Boolean excludeHandledRequests) {
        var allDepositRequests = depositRequestService.getAll();
        return excludeHandledRequests ? allDepositRequests.stream().filter(depositRequest -> depositRequest.getHandledDate() == null).toList() : allDepositRequests;
    }

    @RequestMapping(path = "/depositHandling/{id}", method = RequestMethod.GET)
    public void handleDepositRequest(@PathVariable("id") Integer id, @RequestParam(defaultValue = "false") Boolean approve) {

        var depositRequest = depositRequestService.getById(id);

        if (approve) {
            var user = userService.getById(depositRequest.getUser().getId());
            user.setSaldo(user.getSaldo() + depositRequest.getAmount());
            userService.save(user);

            depositRequest.ValidateRequest();
        } else {
            depositRequest.setHandledDate(LocalDateTime.now());
        }
        depositRequestService.save(depositRequest);
    }


}
