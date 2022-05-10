package nl.brandfonds.Brandfonds.resource;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@Slf4j
@RequestMapping(value = "/rest/day")
public class DayControllerImpl implements DayController {

    @Autowired
    IDayService dayService;

    @Autowired
    IUserService userService;

    @Autowired
    IStockService stockService;

    @GetMapping
    public List<Day> getAll() {
        return dayService.getAll();
    }

    @GetMapping(path = "/{id}")
    public List<Day> getFromSingleUser(@PathVariable(value = "id") Integer id,
                                       @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date != null) {
            var stripesForDates = dayService.getByUserIDAndDate(date, id);
            return Collections.singletonList(stripesForDates.orElse(null));
        }

        return dayService.getByUserID(id);
    }

    @GetMapping(path = "/{id}/totalstripes")
    public Integer getTotalStripesForUser(@PathVariable("id") Integer id) {

        if (!dayService.getTotalStripesFromUser(id).isPresent()) {
            return 0;
        }

        return dayService.getTotalStripesFromUser(id).get();
    }

    @GetMapping(path = "/{id}/sortedbymonth")
    public Map<LocalDate, Integer> getTotalStripesPerMonth(@PathVariable("id") Integer id) {

        TreeMap<LocalDate, Integer> sortedStripes = new TreeMap<>();
        List<Day> allDays = dayService.getByUserID(id);

        allDays.forEach(day -> {
            var truncatedToMonth = day.getDate().withDayOfMonth(1);

            if (sortedStripes.containsKey(truncatedToMonth)) {
                sortedStripes.put(truncatedToMonth, sortedStripes.get(truncatedToMonth) + day.getStripes());
            } else {
                sortedStripes.put(truncatedToMonth, day.getStripes());
            }
        });

        return sortedStripes.descendingMap();
    }

    @GetMapping(path = "/editstripes/{id}/{date}")
    public void editStripeForUser(@PathVariable("id") Integer id,
                                  @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @RequestParam(value = "amount") Integer amount) {

        User user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));

        if (amount < 0) {
            dayService.getByUserIDAndDate(date, id).ifPresent(specific -> {

                if (specific.getStripes() > 1) {
                    dayService.editStripes(amount, date, id);
                } else {
                    dayService.delete(specific);
                }
                userService.setUserSaldo(user.getSaldo() - (amount * 50), user.getId());
                stockService.addToStock(amount);
            });
        } else {
            if (!dayService.getByUserIDAndDate(date, id).isPresent()) {
                dayService.save(new Day(user, date, 0));
            }

            userService.setUserSaldo(user.getSaldo() - (amount * 50), user.getId());
            stockService.removeFromStock(amount);

            dayService.editStripes(amount, date, id);
        }

    }
}
