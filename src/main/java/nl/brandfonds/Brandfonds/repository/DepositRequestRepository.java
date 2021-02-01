package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.DepositRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRequestRepository extends JpaRepository<DepositRequest, Integer> {

}
