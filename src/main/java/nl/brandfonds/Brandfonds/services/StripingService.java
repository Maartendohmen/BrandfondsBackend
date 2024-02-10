package nl.brandfonds.Brandfonds.services;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.repository.DayRepository;
import nl.brandfonds.Brandfonds.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class StripingService {

    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserService userService;

    public List<Day> getAll() {
        return dayRepository.findAll();
    }

    public Day getSpecificDayForUser(Integer userId, String date) {
        return dayRepository.getByUserIDAndDate(LocalDate.parse(date), userId).orElse(null);
    }

    public List<Day> getAllDaysForUser(Integer userId) {
        return dayRepository.getByUserID(userId);
    }

    public Integer getTotalStripesForUser(Integer userId) {
        return dayRepository.getTotalStripesFromUser(userId).orElse(0);
    }

    public void editStripesForUserOnDate(Integer userId, String date, Integer amount) {
        var parsedDate = LocalDate.parse(date);
        var user = userService.getById(userId);
        var day = getSpecificDayForUser(userId, date);
        var stock = stockRepository.findAll().get(0);

        if (amount < 0) {
            removeStripeOnDay(amount, day);
        } else {
            addStripeOnDay(user, parsedDate, amount, day);
        }

        stock.setCurrentBottles(stock.getCurrentBottles() + amount);
        stockRepository.save(stock);

        user.setSaldo(user.getSaldo() + (amount * 75L * -1));
        userService.save(user);
    }

    private void removeStripeOnDay(Integer amount, Day selectedDay) {
        if (selectedDay.getStripes() < 2) {
            dayRepository.delete(selectedDay);
        } else {
            selectedDay.setStripes(selectedDay.getStripes() + amount);
            dayRepository.save(selectedDay);
        }
    }

    private void addStripeOnDay(User user, LocalDate date, Integer amount, Day selectedDay) {
        if (selectedDay == null) {
            dayRepository.save(new Day(user, date, amount));
        } else {
            selectedDay.setStripes(selectedDay.getStripes() + amount);
            dayRepository.save(selectedDay);
        }
    }
}
