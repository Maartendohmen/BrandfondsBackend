package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User u WHERE email = ?1")
    Optional<User> getByMail(String mail);
}
