package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.abstraction.IDayService;
import nl.brandfonds.Brandfonds.abstraction.IUserService;
import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.repository.DayRepository;
import nl.brandfonds.Brandfonds.repository.UserRepository;
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
     * @param userid
     * @return
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<Day> GetAllFromUser(@PathVariable("id") Integer userid) {
        return dayService.GetByUserID(userid);
    }

    /**
     * Get specific day by UserID and day
     *
     * @param userid id of user
     * @param date   date to search for
     * @return
     */
    @RequestMapping(path = "/{id}/{date}", method = RequestMethod.GET)
    public Day GetFromSingleUserByDate(@PathVariable("id") Integer userid, @PathVariable("date") Date date) {
        return dayService.GetByUserIDAndDate(date, userid);
    }

    /**
     * Get Total stripe number from user
     *
     * @param userid id of user
     * @return number of total stripes
     */
    @RequestMapping(path = "/{id}/totalstripes", method = RequestMethod.GET)
    public int GetTotalStripes(@PathVariable("id") Integer userid) {
        try {
            return dayService.GetTotalStripesFromUser(userid);
        }
        //No totalstripes because there are no stripes
        catch (AopInvocationException exception) {
            return 0;
        }
    }

    /**
     * Get all stripes from user coupled per month
     *
     * @param userid The id of the user to get the stripes from
     * @return Map with <date, amount of stripes>
     */
    @RequestMapping(path = "/{id}/sortedbymonth", method = RequestMethod.GET)
    public Map<String, Integer> GetTotalStripesPerMonth(@PathVariable("id") Integer userid) {
        Map<String, Integer> sortedstripes = new HashMap<>();
        List<Day> alldays = dayService.GetByUserID(userid);

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
        return sortedstripes;
    }

    //endregion

    //region Edit Stripes methods

    /**
     * Adds a stripe for user on specific date
     *
     * @param userid id of user
     * @param date   date of stripe
     * @return
     */
    @RequestMapping(path = "/addstripe/{id}/{date}", method = RequestMethod.GET)
    public int AddStripeForUser(@PathVariable("id") Integer userid, @PathVariable("date") Date date) {
        User user = userService.GetOne(userid);

        if (dayService.GetByUserIDAndDate(date, userid) == null) {
            dayService.Save(new Day(user, date, 0));
        }
        user.setSaldo(user.getSaldo() - 50);
        return dayService.AddStripe(date, userid);
    }

    /**
     * Add multiple stripes for user on specfic date
     *
     * @param userid The id of the user which should be given stripes
     * @param date   The date of the day the stripes should be added to
     * @param amount The amount of stripes that should be added
     * @return The number of database rows that have been affected by this change
     */
    @RequestMapping(path = "/addstripes/{id}/{date}", method = RequestMethod.PUT)
    public int AddStripesForUser(@PathVariable("id") Integer userid, @PathVariable("date") Date date, @RequestBody Integer amount) {
        User user = userService.GetOne(userid);

        if (dayService.GetByUserIDAndDate(date, userid) == null) {
            dayService.Save(new Day(user, date, 0));
        }
        user.setSaldo(user.getSaldo() - (amount * 50));
        return dayService.AddMultipleStripes(amount, date, userid);
    }

    /**
     * Remove a stripe for user on specfic date
     *
     * @param userid The id of the user which should be removing stripe
     * @param date   The date of the day the stripe should be removed from
     * @return The number of database rows that have been affected by this change
     */
    @RequestMapping(path = "/removestripe/{id}/{date}", method = RequestMethod.GET)
    public int RemoveStripeForUser(@PathVariable("id") Integer userid, @PathVariable("date") Date date) {
        User user = userService.GetOne(userid);
        Day specifiday = dayService.GetByUserIDAndDate(date, userid);

        if (specifiday != null && specifiday.getStripes() >= 2) {
            user.setSaldo(user.getSaldo() + 50);
            return dayService.RemoveStripe(date, userid);
        } else if (specifiday != null) {
            user.setSaldo(user.getSaldo() + 50);
            dayService.RemoveStripe(date, userid);
            dayService.Delete(specifiday);
            return 1;
        }
        return 0;
    }

    /**
     * Remove multiple stripes for user on specfic date
     *
     * @param userid The id of the user which should be removing stripes
     * @param date   The date of the day the stripes should be removed from
     * @param amount The amount of stripes that should be removed
     * @return The number of database rows that have been affected by this change
     */
    @RequestMapping(path = "/removestripes/{id}/{date}", method = RequestMethod.PUT)
    public int RemoveStripesForUser(@PathVariable("id") Integer userid, @PathVariable("date") Date date, @RequestBody Integer amount) {
        User user = userService.GetOne(userid);
        Day specifiday = dayService.GetByUserIDAndDate(date, userid);

        if (specifiday != null && specifiday.getStripes() > amount) {
            user.setSaldo(user.getSaldo() + (amount * 50));
            return dayService.RemoveMultipleStripes(amount, date, userid);
        } else if (specifiday != null) {
            user.setSaldo(user.getSaldo() + (amount * 50));
            dayService.RemoveMultipleStripes(amount, date, userid);
            dayService.Delete(specifiday);
            return 1;
        }
        return 0;
    }

    //endregion

}
