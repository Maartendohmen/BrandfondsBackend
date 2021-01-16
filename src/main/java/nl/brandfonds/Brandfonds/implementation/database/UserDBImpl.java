package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.UserRole;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDBImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();

        users.removeIf(value -> value.getUserRole() == UserRole.BRANDMASTER);

        return users;
    }

    @Override
    public Optional<User> getByID(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getByMail(String mail) {
        return userRepository.getByMail(mail);
    }

    @Override
    public Optional<User> getByName(String name) {
        return userRepository.getByName(name);
    }

    @Override
    public long getUserSaldo(Integer id) {
        return userRepository.getUserSaldo(id);
    }

    @Override
    public void setUserSaldo(Long amount, Integer id) {
        userRepository.setUserSaldo(amount, id);
    }

    @Override
    public void updatePassword(String newpassword, String emailadres) {
        userRepository.updatePassword(newpassword, emailadres);
    }
}
