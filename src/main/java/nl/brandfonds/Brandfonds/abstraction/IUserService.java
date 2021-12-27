package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    void saveProfilePicture(User user, MultipartFile file) throws IOException;

    String getEncodedProfilePicture(User user) throws NotFoundException, IOException;

    void updatePassword(String newpassword, String emailadres);
}
