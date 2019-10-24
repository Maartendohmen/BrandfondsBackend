package nl.brandfonds.Brandfonds.repository;

import nl.brandfonds.Brandfonds.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface DayRepository extends JpaRepository<Day,Integer> {

    //Get days based on user ID
    @Query("SELECT d FROM Day d WHERE user_id = ?1")
    List<Day> GetByUserID(Integer id);

    //Get days based on date
    @Query("SELECT d FROM Day d WHERE date = ?1")
    List<Day> GetByDate(Date date);

    //gets day from user by specific day
    @Query("SELECT d FROM Day d WHERE date = ?1 and user_id = ?2")
    Day GetByUserIDAndDate(Date date,Integer id);

    //Add stripe based on user and date
    @Transactional
    @Modifying
    @Query("UPDATE Day SET stripes = stripes + 1 WHERE date= ?1 AND user_id = ?2")
    int AddStripe(Date date, Integer id);

    //Remove stripe based on user and date
    @Transactional
    @Modifying
    @Query("UPDATE Day SET stripes = stripes - 1 WHERE date= ?1 AND user_id = ?2")
    int RemoveStripe(Date date, Integer id);

    @Query("SELECT SUM(stripes) FROM Day WHERE user_id = ?1")
    int GetTotalStripesFromUser(Integer id);






}
