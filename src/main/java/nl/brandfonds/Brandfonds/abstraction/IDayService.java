package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Day;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDayService extends IBaseService<Day> {

    List<Day> getByUserID(Integer id);

    Optional<Day> getByUserIDAndDate(LocalDate date, Integer id);

    Optional<Integer> getTotalStripesFromUser(Integer id);

    int editStripes(Integer amountOfStripes, LocalDate date, Integer id);
}
