package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException.UserNotFoundException;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.responses.StripesMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping(value = "/rest/day")
public class DayController {

    @Autowired
    IDayService dayService;

    @Autowired
    IUserService userService;

    @Autowired
    IStockService stockService;

    //region Get Stripes methods

    @GetMapping
    @ApiOperation(value = "All stripes", notes = "Get all stripes for all users", nickname = "getAllStripes", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully retrieved", response = Day.class, responseContainer = "List")
    })
    public List<Day> getAll() {
        return dayService.getAll();
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "All stripes for user", notes = "Get all stripes for one user in total or single day", nickname = "getStripesForOneUser", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully retrieved", response = Day.class, responseContainer = "List")
    })
    public List<Day> getFromSingleUser(@PathVariable(value = "id") Integer id,
                                       @RequestParam(value = "date", required = false) Date date) {

        if (date != null) {
            var stripesForDates = dayService.getByUserIDAndDate(date, id);

            return Collections.singletonList(stripesForDates.orElse(null));
        }

        return dayService.getByUserID(id);
    }

    @GetMapping(path = "/{id}/totalstripes")
    @ApiOperation(value = "Total stripes number", notes = "Get the total number of stripes for one person", nickname = "getTotalStripesForUser", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Total stripes successfully retrieved", response = Integer.class)
    })
    public int getTotalStripesForUser(@PathVariable("id") Integer id) {

        if (!dayService.getTotalStripesFromUser(id).isPresent()) {
            return 0;
        }

        return dayService.getTotalStripesFromUser(id).get();
    }

    @GetMapping(path = "/{id}/sortedbymonth")
    @ApiOperation(value = "Stripes month", notes = "Get the total number of stripes combined with the month", nickname = "getTotalStripesForUserPerMonth", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Total stripes/months successfully retrieved")
    })
    public List<StripesMonth> getTotalStripesPerMonth(@PathVariable("id") Integer id) {

        Map<String, Integer> sortedstripes = new HashMap<>();
        List<Day> alldays = dayService.getByUserID(id);

        //todo clean up and get rid of map, use list in method instead

        for (Day d : alldays) {

            LocalDate daydate = d.getDate().toInstant().atZone(ZoneId.of("Europe/Amsterdam")).toLocalDate();
            String key = daydate.getMonthValue() + "-" + daydate.getYear();

            if (sortedstripes.containsKey(key)) {
                int currentnumber = sortedstripes.get(key);
                int newnumber = currentnumber + d.getStripes();
                sortedstripes.replace(key, newnumber);
            } else {
                sortedstripes.put(key, d.getStripes());
            }
        }

        List<StripesMonth> stripesMonths = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedstripes.entrySet()) {
            stripesMonths.add(new StripesMonth(entry.getKey(), entry.getValue()));
        }

        return stripesMonths;
    }

    //endregion

    //region Edit Stripes methods

    @GetMapping(path = "/addstripes/{id}/{date}")
    @ApiOperation(value = "Add stripe(s) user", notes = "Add stripe(s) for a specific user", nickname = "addStripesForUser", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripe successfully added"),
            @ApiResponse(code = 404, message = "The requested user could not be found")
    })
    public int addStripeForUser(@PathVariable("id") Integer id,
                                @PathVariable("date") Date date,
                                @RequestParam(value = "amount", defaultValue = "1") Integer amount) {

        User user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));

        if (!dayService.getByUserIDAndDate(date, id).isPresent()) {
            dayService.save(new Day(user, date, 0));
        }

        user.setSaldo(user.getSaldo() - (amount * 50));
        stockService.removeFromStock(amount);

        return dayService.addStripes(amount,date, id);
    }

    @GetMapping(path = "/removestripes/{id}/{date}")
    @ApiOperation(value = "remove stripe(s) user", notes = "remove stripe(s) for a specific user", nickname = "removeStripesForUser", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripe successfully removed"),
            @ApiResponse(code = 404, message = "The requested user could not be found")
    })
    public void removeStripeForUser(@PathVariable("id") Integer id,
                                    @PathVariable("date") Date date,
                                    @RequestParam(value = "amount", defaultValue = "1") Integer amount) {

        User user = userService.getOne(id).orElseThrow(() -> new UserNotFoundException(id));

        dayService.getByUserIDAndDate(date, id).ifPresent(specific -> {
            if (specific.getStripes() >= 2) {
                dayService.removeStripes(amount,date,id);

            } else {
                dayService.delete(specific);
            }
            user.setSaldo(user.getSaldo() + (amount * 50));
            stockService.addToStock(amount);
        });
    }
    //endregion
}
