package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    @Query("SELECT s FROM Stock s WHERE id = 1")
    Optional<Stock> getStock();

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= ?1 where id = 1")
    void updateCurrentBottles(Integer amount);

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET returnedBottles= ?1 where id = 1")
    void updateReturnedBottles(Integer amount);

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= currentBottles + ?1 where id = 1")
    void addMultipleToStock(Integer amount);

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= currentBottles - ?1 where id = 1")
    void removeMultipleFromStock(Integer amount);

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET nonStripedBottles= ?1 where id = 1")
    void updateNonStripedBottles(Integer amount);


}
