package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.responses.StripesMonth;
import org.springframework.aop.AopInvocationException;
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
    @ApiOperation(value = "All stripes", notes = "Get all stripes for all users")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stripes successfully retrieved")
    })
    public List<Day> getAll() {
        return dayService.GetAll();
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "All stripes for user", notes = "Get all stripes for one user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stripes successfully retrieved")
    })
    public List<Day> GetAllFromUser(@PathVariable(value = "id") Integer id) {
        return dayService.GetByUserID(id);
    }

    @GetMapping(path = "/{id}/{date}")
    @ApiOperation(value = "All stripes for user for day", notes = "Get all stripes for one user on one day")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stripes successfully retrieved")
    })
    public Day GetFromSingleUserByDate(@PathVariable(value = "id") Integer id,
                                       @PathVariable("date") Date date) {
        return dayService.GetByUserIDAndDate(date, id);
    }

    @GetMapping(path = "/{id}/totalstripes")
    @ApiOperation(value = "Total stripes number", notes = "Get the total number of stripes for one person")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Total stripes successfully retrieved")
    })
    public int GetTotalStripes(@PathVariable("id") Integer id) {
        try {
            return dayService.GetTotalStripesFromUser(id);
        }
        //No totalstripes because there are no stripes
        catch (AopInvocationException exception) {
            return 0;
        }
    }

    @GetMapping(path = "/{id}/sortedbymonth")
    @ApiOperation(value = "Stripes month", notes = "Get the total number of stripes combined with the month")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Total stripes/months successfully retrieved")
    })
    public List<StripesMonth> GetTotalStripesPerMonth(@PathVariable("id") Integer id) {

        Map<String, Integer> sortedstripes = new HashMap<>();
        List<Day> alldays = dayService.GetByUserID(id);

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

    @GetMapping(path = "/addstripe/{id}/{date}")
    @ApiOperation(value = "Add stripe user", notes = "Add a stripe for a specific user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stripe successfully added")
    })
    public int AddStripeForUser(@PathVariable(value = "id") Integer id,
                                @PathVariable("date") Date date) {

        User user = userService.GetOne(id);

        if (dayService.GetByUserIDAndDate(date, id) == null) {
            dayService.Save(new Day(user, date, 0));
        }
        user.setSaldo(user.getSaldo() - 50);
        stockService.RemoveOneFromStock();

        return dayService.AddStripe(date, id);
    }

    @PutMapping(path = "/addstripes/{id}/{date}")
    @ApiOperation(value = "Add multiple stripes user", notes = "Add multiple stripes for one user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stripes successfully added")
    })
    public int AddStripesForUser(@PathVariable("id") Integer id,
                                 @PathVariable("date") Date date,
                                 @RequestBody Integer amount) {
        User user = userService.GetOne(id);

        if (dayService.GetByUserIDAndDate(date, id) == null) {
            dayService.Save(new Day(user, date, 0));
        }
        user.setSaldo(user.getSaldo() - (amount * 50));
        stockService.RemoveMultipleFromStock(amount);

        return dayService.AddMultipleStripes(amount, date, id);
    }

    @GetMapping(path = "/removestripe/{id}/{date}")
    @ApiOperation(value = "remove stripe user", notes = "remove a stripe for a specific user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stripe successfully removed")
    })
    public int RemoveStripeForUser(@PathVariable("id") Integer id,
                                   @PathVariable("date") Date date) {

        User user = userService.GetOne(id);
        Day specifiday = dayService.GetByUserIDAndDate(date, id);

        if (specifiday != null && specifiday.getStripes() >= 2) {
            user.setSaldo(user.getSaldo() + 50);
            stockService.AddOneToStock();
            return dayService.RemoveStripe(date, id);
        } else if (specifiday != null) {
            user.setSaldo(user.getSaldo() + 50);
            stockService.AddOneToStock();
            dayService.RemoveStripe(date, id);
            dayService.Delete(specifiday);
            return 1;
        }
        return 0;
    }

    @PutMapping(path = "/removestripes/{id}/{date}")
    @ApiOperation(value = "Remove multiple stripes user", notes = "Remove multiple stripes for one user")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stripes successfully added")
    })
    public int RemoveStripesForUser(@PathVariable("id") Integer id,
                                    @PathVariable("date") Date date,
                                    @RequestBody Integer amount) {

        User user = userService.GetOne(id);
        Day specifiday = dayService.GetByUserIDAndDate(date, id);

        if (specifiday != null && specifiday.getStripes() > amount) {
            user.setSaldo(user.getSaldo() + (amount * 50));
            stockService.AddMultipleToStock(amount);
            return dayService.RemoveMultipleStripes(amount, date, id);
        } else if (specifiday != null) {
            user.setSaldo(user.getSaldo() + (amount * 50));
            stockService.AddMultipleToStock(amount);
            dayService.RemoveMultipleStripes(amount, date, id);
            dayService.Delete(specifiday);
            return 1;
        }
        return 0;
    }

    //endregion


}
