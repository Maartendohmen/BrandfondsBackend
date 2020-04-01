package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Day;

import java.util.Date;
import java.util.List;

public interface IDayService {

    public abstract List<Day> GetAll();

    public abstract Day GetOne(Integer id);

    public abstract void Save(Day day);

    public abstract void Delete(Day day);








    public  abstract List<Day> GetByUserID(Integer id);

    public  abstract List<Day> GetByDate(Date date);

    public  abstract Day GetByUserIDAndDate(Date date,Integer id);

    public  abstract int AddStripe(Date date, Integer id);

    public  abstract int RemoveStripe(Date date, Integer id);

    public  abstract int AddMultipleStripes(Integer amountOfStripes, Date date, Integer id);

    public  abstract int RemoveMultipleStripes(Integer amountOfStripes, Date date, Integer id);

    public  abstract int GetTotalStripesFromUser(Integer id);
}
