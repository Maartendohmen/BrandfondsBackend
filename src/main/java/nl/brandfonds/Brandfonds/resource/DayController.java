package nl.brandfonds.Brandfonds.resource;

import io.jsonwebtoken.Claims;
import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.model.responses.StripesMonth;
import nl.brandfonds.Brandfonds.security.CustomUserDetails;
import nl.brandfonds.Brandfonds.security.JwtUtil;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/rest/day")
public class DayController {

    @Autowired
    IDayService dayService;

    @Autowired
    IUserService userService;

    @Autowired
    IStockService stockService;

    @Autowired
    JwtUtil jwtUtil;

    private int GetIDFromJWT(String token) {
        final int[] id = new int[1];
        String cleantoken = token.replace("Bearer ", "");

        jwtUtil.extractClaim(cleantoken, new Function<Claims, Object>() {
            @Override
            public Object apply(Claims claims) {
                id[0] = (int) claims.get("id");
                return id[0];
            }
        });

        return id[0];
    }


    //region Get Stripes methods

    /**
     * Get all Days from all users
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Day> getAll() {
        return dayService.GetAll();
    }

    /**
     * Get all stripe-days from a single user
     *
     * @param id
     * @return
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<Day> GetAllFromUser(@PathVariable(value = "id",required = false) Integer id,
                                    @RequestHeader(name = "Authorization") String token) {
        if (id == null){
            id = GetIDFromJWT(token);
        }

        return dayService.GetByUserID(id);
    }

    /**
     * Get specific day by UserID and day
     *
     * @param id id of user
     * @param date   date to search for
     * @return
     */
    @RequestMapping(path = "/{id}/{date}", method = RequestMethod.GET)
    public Day GetFromSingleUserByDate(@PathVariable(value = "id",required = false) Integer id,
                                       @PathVariable("date") Date date,
                                       @RequestHeader(name = "Authorization") String token) {
        if (id == null){
            id = GetIDFromJWT(token);
        }

        return dayService.GetByUserIDAndDate(date, id);
    }

    /**
     * Get Total stripe number from user
     *
     * @param id id of user
     * @return number of total stripes
     */
    @RequestMapping(path = "/totalstripes/{id}", method = RequestMethod.GET)
    public int GetTotalStripes(@PathVariable("id") Integer id,
                               @RequestHeader(name = "Authorization") String token) {

        if (id == null){
            id = GetIDFromJWT(token);
        }

        try {
            return dayService.GetTotalStripesFromUser(id);
        }
        //No totalstripes because there are no stripes
        catch (AopInvocationException exception) {
            return 0;
        }
    }

    /**
     * Get all stripes from user coupled per month
     *
     * @return Map with <date, amount of stripes>
     */
    @RequestMapping(path = "/sortedbymonth", method = RequestMethod.GET)
    public List<StripesMonth> GetTotalStripesPerMonth(@RequestHeader(name = "Authorization") String token) {

        int id = GetIDFromJWT(token);

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
            stripesMonths.add(new StripesMonth(entry.getKey(),entry.getValue()));
        }

        return stripesMonths;
    }

    //endregion

    //region Edit Stripes methods

    /**
     * Adds a stripe for user on specific date
     *
     * @param id id of user
     * @param date   date of stripe
     * @return
     */
    @RequestMapping(path = "/addstripe/{id}/{date}", method = RequestMethod.GET)
    public int AddStripeForUser(@PathVariable(value = "id", required = false) Integer id,
                                @PathVariable("date") Date date,
                                @RequestHeader(name = "Authorization") String token) {

        if (id == null){
            id = GetIDFromJWT(token);
        }

        User user = userService.GetOne(id);

        if (dayService.GetByUserIDAndDate(date, id) == null) {
            dayService.Save(new Day(user, date, 0));
        }
        user.setSaldo(user.getSaldo() - 50);
        stockService.RemoveOneFromStock();

        return dayService.AddStripe(date, id);
    }

    /**
     * Add multiple stripes for user on specfic date
     *
     * @param id The id of the user which should be given stripes
     * @param date   The date of the day the stripes should be added to
     * @param amount The amount of stripes that should be added
     * @return The number of database rows that have been affected by this change
     */
    @RequestMapping(path = "/addstripes/{id}/{date}", method = RequestMethod.PUT)
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

    /**
     * Remove a stripe for user on specfic date
     *
     * @param id The id of the user which should be removing stripe
     * @param date   The date of the day the stripe should be removed from
     * @return The number of database rows that have been affected by this change
     */
    @RequestMapping(path = "/removestripe/{id}/{date}", method = RequestMethod.GET)
    public int RemoveStripeForUser(@PathVariable("id") Integer id,
                                   @PathVariable("date") Date date,
                                   @RequestHeader(name = "Authorization") String token) {
        if (id == null){
            id = GetIDFromJWT(token);
        }

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

    /**
     * Remove multiple stripes for user on specfic date
     *
     * @param id The id of the user which should be removing stripes
     * @param date   The date of the day the stripes should be removed from
     * @param amount The amount of stripes that should be removed
     * @return The number of database rows that have been affected by this change
     */
    @RequestMapping(path = "/removestripes/{id}/{date}", method = RequestMethod.PUT)
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
