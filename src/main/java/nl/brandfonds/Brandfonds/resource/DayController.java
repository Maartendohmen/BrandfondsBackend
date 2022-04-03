package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.*;
import nl.brandfonds.Brandfonds.model.Day;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Api(tags = "Stripes", description = "Stripes operations")
public interface DayController {

    @ApiOperation(value = "All stripes", nickname = "getAllStripes", notes = "Get all stripes for all users", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully retrieved", response = Day.class, responseContainer = "List")
    })
    List<Day> getAll();

    @ApiOperation(value = "All stripes for user", nickname = "getStripesForOneUser", notes = "Get all stripes for one user in total or single day", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripes successfully retrieved", response = Day.class, responseContainer = "List")
    })
    List<Day> getFromSingleUser(Integer id, LocalDate data);

    @ApiOperation(value = "Total stripes number", nickname = "getTotalStripesForUser", notes = "Get the total number of stripes for one person", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Total stripes successfully retrieved", response = Integer.class)
    })
    Integer getTotalStripesForUser(Integer id);

    @ApiOperation(value = "Stripes month", nickname = "getTotalStripesForUserPerMonth", notes = "Get the total number of stripes combined with the month", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Total stripes/months successfully retrieved")
    })
    Map<LocalDate, Integer> getTotalStripesPerMonth(Integer id);

    @ApiOperation(value = "Edit stripe(s) user", nickname = "editStripesForUser", notes = "Edit stripe(s) for a specific user", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stripe successfully edited", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "The requested user could not be found", response = ResponseEntity.class)
    })
    void editStripeForUser(Integer id, LocalDate date, Integer amount);
}
