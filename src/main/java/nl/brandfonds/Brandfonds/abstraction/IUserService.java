package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserService{

    public abstract List<User> GetAll();

    public abstract User GetOne(Integer id);

    public abstract void Save(User user);

    public abstract void Delete(User user);


    public  abstract User Login(String username, String password);

    public  abstract User GetByMail(String mail);

    public  abstract long GetUserSaldo(Integer id);

    public  abstract void SetUserSaldo(Long amount,Integer id);

    public  abstract void UpdatePassword(String newpassword, String emailadres);
}
