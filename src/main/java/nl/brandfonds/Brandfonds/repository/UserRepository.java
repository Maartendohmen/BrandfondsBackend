package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Integer> {

    //Login user with credentials
    @Query("SELECT u FROM User u WHERE username = ?1 AND password = ?2")
    User Login(String username,String password);

    @Query("SELECT u FROM User u WHERE id = ?1")
    User GetByID(Integer id);
}
