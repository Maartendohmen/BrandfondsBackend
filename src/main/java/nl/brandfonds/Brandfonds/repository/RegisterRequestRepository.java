package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.RegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterRequestRepository extends JpaRepository<RegisterRequest, Integer> {

    //get registerrequest based on random string
    @Query("SELECT r FROM RegisterRequest r WHERE randomString = ?1")
    Optional<RegisterRequest> getByrandomString(String randomString);
}
