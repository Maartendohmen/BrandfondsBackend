package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordChangeRequestRepository extends JpaRepository<PasswordChangeRequest, Integer> {

    //get passwordrequest based on random string
    @Query("SELECT r FROM PasswordChangeRequest r WHERE randomString = ?1")
    Optional<PasswordChangeRequest> getByRandomString(String randomString);
}
