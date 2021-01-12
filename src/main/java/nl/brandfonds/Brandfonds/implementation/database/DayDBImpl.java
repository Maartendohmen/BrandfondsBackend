package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DayDBImpl implements IDayService {

    @Autowired
    DayRepository dayRepository;


    @Override
    public List<Day> GetAll() {
        return dayRepository.findAll();
    }

    @Override
    public Day GetOne(Integer id) {
        return dayRepository.getOne(id);
    }

    @Override
    public void Save(Day day) {
        dayRepository.save(day);
    }

    @Override
    public void Delete(Day day) {
        dayRepository.delete(day);
    }

    @Override
    public List<Day> GetByUserID(Integer id) {
        return dayRepository.GetByUserID(id);
    }

    @Override
    public List<Day> GetByDate(Date date) {
        return dayRepository.GetByDate(date);
    }

    @Override
    public Day GetByUserIDAndDate(Date date, Integer id) {
        return dayRepository.GetByUserIDAndDate(date, id);
    }

    @Override
    public int AddStripe(Date date, Integer id) {
        return dayRepository.AddStripe(date, id);
    }

    @Override
    public int RemoveStripe(Date date, Integer id) {
        return dayRepository.RemoveStripe(date, id);
    }

    @Override
    public int AddMultipleStripes(Integer amountOfStripes, Date date, Integer id) {
        return dayRepository.AddMultipleStripes(amountOfStripes, date, id);
    }

    @Override
    public int RemoveMultipleStripes(Integer amountOfStripes, Date date, Integer id) {
        return dayRepository.RemoveMultipleStripes(amountOfStripes, date, id);
    }

    @Override
    public int GetTotalStripesFromUser(Integer id) {
        return dayRepository.GetTotalStripesFromUser(id);
    }
}
