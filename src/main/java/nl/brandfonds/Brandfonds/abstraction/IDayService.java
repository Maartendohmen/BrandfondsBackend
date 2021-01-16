package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Day;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IDayService {

    List<Day> getAll();

    Optional<Day> getOne(Integer id);

    void save(Day day);

    void delete(Day day);

    List<Day> getByUserID(Integer id);

    Optional<Day> getByUserIDAndDate(Date date, Integer id);

    Optional<Integer> getTotalStripesFromUser(Integer id);

    int addStripe(Date date, Integer id);

    int removeStripe(Date date, Integer id);

    int addMultipleStripes(Integer amountOfStripes, Date date, Integer id);

    int removeMultipleStripes(Integer amountOfStripes, Date date, Integer id);
}
