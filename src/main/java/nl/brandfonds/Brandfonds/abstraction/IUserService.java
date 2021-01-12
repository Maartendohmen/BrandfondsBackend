package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.User;

import java.util.List;

public interface IUserService{

    public abstract List<User> GetAll();

    public abstract User GetByID(Integer id);

    public abstract User GetOne(Integer id);

    public abstract void Save(User user);

    public abstract void Delete(User user);

    public  abstract User Login(String username, String password);

    public  abstract User GetByMail(String mail);

    public abstract User GetByName(String name);

    public  abstract long GetUserSaldo(Integer id);

    public  abstract void SetUserSaldo(Long amount,Integer id);


    public  abstract void UpdatePassword(String newpassword, String emailadres);
}
