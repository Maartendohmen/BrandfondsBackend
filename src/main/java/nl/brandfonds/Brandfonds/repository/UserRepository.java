package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE forename = ?1 AND password = ?2")
    Optional<User> login(String username, String password);

    @Query("SELECT u FROM User u WHERE id = ?1")
    Optional<User> getByID(Integer id);

    @Query("SELECT u FROM User u WHERE mailadres = ?1")
    Optional<User> getByMail(String mail);

    @Query("SELECT u FROM User u WHERE forename = ?1")
    Optional<User> getByName(String name);

    @Query("SELECT saldo FROM User WHERE id = ?1")
    long getUserSaldo(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE User SET saldo= ?1 WHERE id= ?2")
    void setUserSaldo(Long amount, Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE User SET password= ?1 WHERE mailadres= ?2")
    void updatePassword(String newPassword, String mailadres);

}
