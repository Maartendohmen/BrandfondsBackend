package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StockRepository extends JpaRepository<Stock,Integer> {

    @Query("SELECT s FROM Stock s WHERE id = 1")
    Stock GetStock();

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= ?1 where id = 1")
    int UpdateCurrentBottles(Integer amount);

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET returnedBottles= ?1 where id = 1")
    int UpdateReturnedBottles(Integer amount);

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= currentBottles + 1 where id = 1")
    int AddOneToStock();

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= currentBottles - 1 where id = 1")
    int RemoveOneFromStock();

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= currentBottles + ?1 where id = 1")
    int AddMultipleToStock(Integer amount);

    @Transactional
    @Modifying
    @Query("UPDATE Stock SET currentBottles= currentBottles - ?1 where id = 1")
    int RemoveMultipleFromStock(Integer amount);


}
