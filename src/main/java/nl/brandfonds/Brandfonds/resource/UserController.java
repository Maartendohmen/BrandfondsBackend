package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.util.SHA256;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController {

    private String salt = "e24AzH";

    @Autowired
    UserRepository userRepository;

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public boolean Save(@RequestBody User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    @RequestMapping(path = "/{id}/saldo",method = RequestMethod.GET)
    public long GetUserSaldo(@PathVariable("id") Integer id)
    {
        return  userRepository.GetUserSaldo(id);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User Login(@RequestBody User user) {
        return userRepository.Login(user.getForname(), user.getPassword() + salt);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String Register(@RequestBody User user) {
        try {

            RegisterRequest request = new RegisterRequest(user.getEmailadres(), user.getForname(), user.getSurname(), user.getPassword() + salt);
            registerRequestRepository.save(request);
            //email user request random string
            return request.getRandomString();
        } catch (Exception x) {
            return null;
        }
    }

    @RequestMapping(path = "/registerconformation/{randomstring}", method = RequestMethod.GET)
    public boolean ConfirmRegistration(@PathVariable("randomstring") String randomstring) {
        RegisterRequest corospondingrequest = registerRequestRepository.GetByrandomString(randomstring);

        if (corospondingrequest != null) {
            userRepository.save(new User(corospondingrequest.getEmailadres(), corospondingrequest.getForname(), corospondingrequest.getSurname(), corospondingrequest.getPassword()));
            registerRequestRepository.delete(corospondingrequest);
            return true;
        }
        return false;
    }
}
