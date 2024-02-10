package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayRepository extends JpaRepository<Day, Integer> {

    //Get days based on user ID
    @Query(value = "SELECT d FROM Day d WHERE d.user.id = ?1")
    List<Day> getByUserID(Integer id);

    //Get days based on date
    @Query(value = "SELECT d FROM Day d WHERE d.date = ?1")
    List<Day> getByDate(Date date);

    //gets day from user by specific day
    @Query(value = "SELECT d FROM Day d WHERE d.date = ?1 and d.user.id = ?2")
    Optional<Day> getByUserIDAndDate(LocalDate date, Integer id);

    //Add stripe based on user and date
    @Transactional
    @Modifying
    @Query(value = "UPDATE Day d SET stripes = stripes + ?1 WHERE d.date = ?2 AND d.user.id = ?3")
    void updateStripes(Integer amountOfStripes, LocalDate date, Integer id);

    @Query(value = "SELECT SUM(stripes) FROM Day d WHERE d.user.id = ?1")
    Optional<Integer> getTotalStripesFromUser(Integer id);

}
