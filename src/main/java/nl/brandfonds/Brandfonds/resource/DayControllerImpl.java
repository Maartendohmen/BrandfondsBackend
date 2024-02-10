package nl.brandfonds.Brandfonds.resource;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.services.StripingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@Slf4j
@RequestMapping(value = "/rest/day")
public class DayControllerImpl implements DayController {

    @Autowired
    StripingService stripingService;

    @GetMapping
    public List<Day> getAll() {
        return stripingService.getAll();
    }

    @GetMapping(path = "/{userId}/{date}")
    public Day getFromSingleUser(@PathVariable(value = "userId") Integer userId,
                                 @PathVariable(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date) {
        return stripingService.getSpecificDayForUser(userId, date);
    }

    @GetMapping(path = "/{userId}/totalstripes")
    public Integer getTotalStripesForUser(@PathVariable("userId") Integer userId) {
        return stripingService.getTotalStripesForUser(userId);
    }

    @GetMapping(path = "/{userId}/sortedbymonth")
    public Map<LocalDate, Integer> getTotalStripesPerMonth(@PathVariable("userId") Integer userId) {

        // todo we-write in service
        TreeMap<LocalDate, Integer> sortedStripes = new TreeMap<>();
        List<Day> allDays = stripingService.getAllDaysForUser(userId);

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

    @GetMapping(path = "/editstripes/{userId}/{date}")
    public void editStripeForUser(@PathVariable("userId") Integer userId,
                                  @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date,
                                  @RequestParam(value = "amount") Integer amount) {
        stripingService.editStripesForUserOnDate(userId, date, amount);
    }

}
