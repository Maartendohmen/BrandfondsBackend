package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAll();

    void save(User user);

    void delete(User user);

    Optional<User> getByID(Integer id);

    Optional<User> getByMail(String mail);

    Optional<User> getByName(String name);

    long getUserSaldo(Integer id);

    void setUserSaldo(Long amount, Integer id);

    void updatePassword(String newpassword, String emailadres);
}
