package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DayDBImpl implements IDayService {

    @Autowired
    DayRepository dayRepository;


    @Override
    public List<Day> getAll() {
        return dayRepository.findAll();
    }

    @Override
    public Optional<Day> getOne(Integer id) {
        return dayRepository.findById(id);
    }

    @Override
    public void save(Day day) {
        dayRepository.save(day);
    }

    @Override
    public void delete(Day day) {
        dayRepository.delete(day);
    }

    @Override
    public List<Day> getByUserID(Integer id) {
        return dayRepository.getByUserID(id);
    }

    @Override
    public Optional<Day> getByUserIDAndDate(LocalDate date, Integer id) {
        return dayRepository.getByUserIDAndDate(date, id);
    }

    @Override
    public void editStripes(Integer amountOfStripes, LocalDate date, Integer id) {
        dayRepository.updateStripes(amountOfStripes, date, id);
    }

    @Override
    public Optional<Integer> getTotalStripesFromUser(Integer id) {
        return dayRepository.getTotalStripesFromUser(id);
    }
}
