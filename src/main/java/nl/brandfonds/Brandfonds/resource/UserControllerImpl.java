package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.abstraction.IDepositRequestService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException.DepositRequestNotFoundException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/user")
public class UserControllerImpl implements UserController {

    @Autowired
    IUserService userService;

    @Autowired
    IDepositRequestService depositRequestService;


    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public void save(@RequestBody User user) {
        userService.save(user);
    }

    @GetMapping(path = "/saldo/{id}")
    public Long getUserSaldo(@PathVariable(value = "id") Integer id) {
        return userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id)).getSaldo();
    }

    @PutMapping(path = "/saldo/{id}")
    public void setUserSaldo(@PathVariable("id") Integer id,
                             @RequestBody Long amount) {
        userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));
        userService.setUserSaldo(amount, id);
    }

    @PostMapping(path = "/profile_picture/{id}")
    public void setUserProfilePicture(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        var user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));
        userService.saveProfilePicture(user, file);
    }

    @GetMapping(path = "/profile_picture/{id}")
    public String getEncodedUserProfilePicture(@PathVariable("id") Integer id) throws IOException {
        var user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));
        return userService.getEncodedProfilePicture(user);
    }

    @PostMapping(path = "/{id}/deposit")
    public void setDepositRequest(@PathVariable("id") Integer id, @RequestBody String amount) {
        var user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));
        depositRequestService.save(new DepositRequest(user, Long.parseLong(amount)));
    }

    //todo maybe add method for getting deposit requests for single user

    @GetMapping(path = "/deposit")
    public List<DepositRequest> getDepositRequests() {
        return depositRequestService.getAll();
    }

    @RequestMapping(path = "/deposithandling/{id}/{approve}", method = RequestMethod.GET)
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


}
