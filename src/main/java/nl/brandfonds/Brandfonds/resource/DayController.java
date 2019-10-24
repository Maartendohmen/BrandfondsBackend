package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.User;
import nl.brandfonds.Brandfonds.repository.DayRepository;
import nl.brandfonds.Brandfonds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/day")
public class DayController {

    @Autowired
    DayRepository dayRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Get all Days from all users
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Day> getAll() {
        return dayRepository.findAll();
    }

    /**
     * Get all stripe-days from a single user
     * @param userid
     * @return
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public List<Day> GetAllFromUser(@PathVariable("id") Integer userid)
    {
        return dayRepository.GetByUserID(userid);
    }

    /**
     * Get specific day by UserID and day
     * @param userid id of user
     * @param date date to search for
     * @return
     */
    @RequestMapping(path = "/{id}/{date}", method = RequestMethod.GET)
    public Day GetFromSingleUserByDate(@PathVariable("id") Integer userid, @PathVariable("date") Date date)
    {
        return dayRepository.GetByUserIDAndDate(date,userid);
    }

    /**
     * Adds a stripe for user on specific date
     * @param userid id of user
     * @param date date of stripe
     * @return
     */
    @RequestMapping(path = "/addstripe/{id}/{date}", method = RequestMethod.GET)
    public int AddStripeForUser(@PathVariable("id") Integer userid, @PathVariable("date") Date date)
    {
        User user = userRepository.GetByID(userid);

        if (dayRepository.GetByUserIDAndDate(date,userid) == null)
        {
            dayRepository.save(new Day(user,date,0));
        }
        user.setSaldo(user.getSaldo() - 50);
        return dayRepository.AddStripe(date,userid);
    }

    @RequestMapping(path = "/removestripe/{id}/{date}", method = RequestMethod.GET)
    public int RemoveStripeForUser(@PathVariable("id") Integer userid, @PathVariable("date") Date date)
    {
        User user = userRepository.GetByID(userid);

        if (dayRepository.GetByUserIDAndDate(date,userid) != null)
        {
            user.setSaldo(user.getSaldo() + 50);
            return  dayRepository.RemoveStripe(date,userid);
        }
        return 0;
    }

    /**
     * Get Total stripe number from user
     * @param userid id of user
     * @return
     */
    @RequestMapping(path = "/{id}/totalstripes", method = RequestMethod.GET)
    public int GetTotalStripes(@PathVariable("id") Integer userid)
    {
        return dayRepository.GetTotalStripesFromUser(userid);
    }






}
