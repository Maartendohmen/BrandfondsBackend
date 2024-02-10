package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

}
