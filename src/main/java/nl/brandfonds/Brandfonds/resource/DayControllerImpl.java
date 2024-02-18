package nl.brandfonds.Brandfonds.resource;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.responses.StripesPerMonth;
import nl.brandfonds.Brandfonds.services.StripingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@RestController
@Slf4j
@RequestMapping(value = "/rest/day")
public class DayControllerImpl implements DayController {

    @Autowired
    StripingService stripingService;

    @Value("${striping-cost}")
    private Float stripingCost;

    @GetMapping
    public List<Day> getAll() {
        return stripingService.getAll();
    }

    @GetMapping(path = "/{userId}/{date}")
    public Day getFromSingleUser(@PathVariable(value = "userId") Integer userId,
                                 @PathVariable(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date) {
        return stripingService.getSpecificDayForUser(userId, date);
    }

    @GetMapping(path = "/totalStripes/{userId}")
    public Integer getTotalStripesForUser(@PathVariable("userId") Integer userId) {
        return stripingService.getTotalStripesForUser(userId);
    }

    @GetMapping(path = "sortedByMonth/{userId}")
    public List<StripesPerMonth> getTotalStripesPerMonth(@PathVariable("userId") Integer userId) {

        TreeMap<LocalDate, StripesPerMonth> stripesPerMonth = new TreeMap<>();
        List<Day> allDays = stripingService.getAllDaysForUser(userId);

        allDays.forEach(day -> {
            var truncatedToMonth = day.getDate().withDayOfMonth(1);

            if (stripesPerMonth.containsKey(truncatedToMonth)) {
                var oldStripesPerMonth = stripesPerMonth.get(truncatedToMonth);
                oldStripesPerMonth.setStripes(oldStripesPerMonth.getStripes() + day.getStripes());
                oldStripesPerMonth.setCosts(stripingCost * day.getStripes());
                stripesPerMonth.put(truncatedToMonth, oldStripesPerMonth);
            } else {
                stripesPerMonth.put(truncatedToMonth, new StripesPerMonth(truncatedToMonth.toString(), day.getStripes(), stripingCost * day.getStripes()));
            }
        });
        return new ArrayList<>(stripesPerMonth.descendingMap().values());
    }

    @GetMapping(path = "/editStripes/{userId}/{date}")
    public void editStripeForUser(@PathVariable("userId") Integer userId,
                                  @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date,
                                  @RequestParam(value = "amount") Integer amount) {
        stripingService.editStripesForUserOnDate(userId, date, amount);
    }

}
