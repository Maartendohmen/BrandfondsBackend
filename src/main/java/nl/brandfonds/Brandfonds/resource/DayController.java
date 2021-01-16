package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
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
    @ApiOperation(value = "All stripes", notes = "Get all stripes for all users")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully retrieved", response = Day.class, responseContainer = "List")
    })
    public List<Day> getAll() {
        return dayService.getAll();
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "All stripes for user", notes = "Get all stripes for one user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully retrieved", response = Day.class, responseContainer = "List")
    })
    public List<Day> getAllFromUser(@PathVariable(value = "id") Integer id) {
        return dayService.getByUserID(id);
    }

    @GetMapping(path = "/{id}/{date}")
    @ApiOperation(value = "All stripes for user for day", notes = "Get all stripes for one user on one day")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully retrieved", response = Day.class)
    })
    public Day getFromSingleUserByDate(@PathVariable(value = "id") Integer id,
                                       @PathVariable("date") Date date) {

        if (!dayService.getByUserIDAndDate(date,id).isPresent())
        {
            return null;
        }
        return dayService.getByUserIDAndDate(date, id).get();
    }

    @GetMapping(path = "/{id}/totalstripes")
    @ApiOperation(value = "Total stripes number", notes = "Get the total number of stripes for one person")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Total stripes successfully retrieved", response = Integer.class)
    })
    public int getTotalStripes(@PathVariable("id") Integer id) {

        if (!dayService.getTotalStripesFromUser(id).isPresent()) {
            return 0;
        }

        return dayService.getTotalStripesFromUser(id).get();
    }

    @GetMapping(path = "/{id}/sortedbymonth")
    @ApiOperation(value = "Stripes month", notes = "Get the total number of stripes combined with the month")
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

    @GetMapping(path = "/addstripe/{id}/{date}")
    @ApiOperation(value = "Add stripe user", notes = "Add a stripe for a specific user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripe successfully added"),
            @ApiResponse(code = 404, message = "The requested user could not be found")
    })
    public int addStripeForUser(@PathVariable(value = "id") Integer id,
                                @PathVariable("date") Date date) throws NotFoundException {

        if (!userService.getByID(id).isPresent()) {
            throw new NotFoundException("De gebruiker  kan niet worden opgehaald");
        }

        User user = userService.getByID(id).get();

        if (!dayService.getByUserIDAndDate(date, id).isPresent()) {
            dayService.save(new Day(user, date, 0));
        }

        user.setSaldo(user.getSaldo() - 50);
        stockService.removeOneFromStock();

        return dayService.addStripe(date, id);
    }

    @PutMapping(path = "/addstripes/{id}/{date}")
    @ApiOperation(value = "Add multiple stripes user", notes = "Add multiple stripes for one user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully added"),
            @ApiResponse(code = 404, message = "The requested user could not be found")
    })
    public int addStripesForUser(@PathVariable("id") Integer id,
                                 @PathVariable("date") Date date,
                                 @RequestBody Integer amount) throws NotFoundException {

        if (!userService.getByID(id).isPresent()) {
            throw new NotFoundException("De gebruiker kan niet worden opgehaald");
        }

        User user = userService.getByID(id).get();

        if (!dayService.getByUserIDAndDate(date, id).isPresent()) {
            dayService.save(new Day(user, date, 0));
        }

        user.setSaldo(user.getSaldo() - (amount * 50));
        stockService.removeMultipleFromStock(amount);

        return dayService.addMultipleStripes(amount, date, id);
    }

    @GetMapping(path = "/removestripe/{id}/{date}")
    @ApiOperation(value = "remove stripe user", notes = "remove a stripe for a specific user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripe successfully removed"),
            @ApiResponse(code = 404, message = "The requested user could not be found")
    })
    public void removeStripeForUser(@PathVariable("id") Integer id,
                                    @PathVariable("date") Date date) throws NotFoundException {

        if (!userService.getByID(id).isPresent()) {
            throw new NotFoundException("De gebruiker kan niet worden opgehaald");
        }

        User user = userService.getByID(id).get();

        dayService.getByUserIDAndDate(date, id).ifPresent(specifiday -> {
            if (specifiday.getStripes() >= 2) {
                user.setSaldo(user.getSaldo() + 50);
                stockService.addOneToStock();
                dayService.removeStripe(date, id);
            } else {
                user.setSaldo(user.getSaldo() + 50);
                stockService.addOneToStock();
                dayService.removeStripe(date, id);
                dayService.delete(specifiday);
            }
        });
    }

    @PutMapping(path = "/removestripes/{id}/{date}")
    @ApiOperation(value = "Remove multiple stripes user", notes = "Remove multiple stripes for one user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully added"),
            @ApiResponse(code = 404, message = "The requested user could not be found")
    })
    public void removeStripesForUser(@PathVariable("id") Integer id,
                                     @PathVariable("date") Date date,
                                     @RequestBody Integer amount) throws NotFoundException {

        if (!userService.getByID(id).isPresent()) {
            throw new NotFoundException("De gebruiker kan niet worden opgehaald");
        }

        User user = userService.getByID(id).get();

        dayService.getByUserIDAndDate(date, id).ifPresent(specifiday -> {
            if (specifiday.getStripes() > amount) {
                user.setSaldo(user.getSaldo() + (amount * 50));
                stockService.addMultipleToStock(amount);
                dayService.removeMultipleStripes(amount, date, id);
            } else {
                user.setSaldo(user.getSaldo() + (amount * 50));
                stockService.addMultipleToStock(amount);
                dayService.removeMultipleStripes(amount, date, id);
                dayService.delete(specifiday);
            }
        });


    }

    //endregion


}
