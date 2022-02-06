package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Day;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IDayService extends IBaseService<Day> {

    List<Day> getByUserID(Integer id);

    Optional<Day> getByUserIDAndDate(Date date, Integer id);

    Optional<Integer> getTotalStripesFromUser(Integer id);

    int addStripes(Integer amountOfStripes, Date date, Integer id);

    int removeStripes(Integer amountOfStripes, Date date, Integer id);
}
