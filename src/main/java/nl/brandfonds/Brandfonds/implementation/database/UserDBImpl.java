package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.model.UserRole;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class UserDBImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> GetAll() {
        List<User> users = userRepository.findAll();

        users.removeIf(value -> value.getUserRole() == UserRole.BRANDMASTER);

        return users;
    }

    @Override
    public User GetOne(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public void Save(User user) {
        userRepository.save(user);
    }

    @Override
    public void Delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User Login(String username, String password) {
        return userRepository.Login(username,password);
    }

    @Override
    public User GetByMail(String mail) {
        return userRepository.GetByMail(mail);
    }

    @Override
    public long GetUserSaldo(Integer id) {
        return userRepository.GetUserSaldo(id);
    }

    @Override
    public void SetUserSaldo(Long amount, Integer id) {
        userRepository.SetUserSaldo(amount,id);
    }

    @Override
    public void UpdatePassword(String newpassword, String emailadres) {
        userRepository.UpdatePassword(newpassword, emailadres);
    }
}
