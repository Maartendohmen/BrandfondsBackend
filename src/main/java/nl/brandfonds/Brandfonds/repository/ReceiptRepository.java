package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    @Query("SELECT i FROM Receipt i WHERE file_name = ?1")
    Optional<Receipt> getByName(String name);
}
