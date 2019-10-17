package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public boolean Save(@RequestBody User user)
    {
        try{
            userRepository.save(user);
            return true;
        }
        catch (Exception x)
        {
            return  false;
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public User Login(@RequestBody User user){
        return userRepository.Login(user.getUsername(),user.getPassword());
    }
}
