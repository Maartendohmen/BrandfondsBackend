package nl.brandfonds.Brandfonds.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.brandfonds.Brandfonds.model.Day;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Stripes", description = "Stripes operations")
@SecurityRequirement(name = "Bearer_Authentication")
public interface DayController {

    @Operation(summary = "All stripes", operationId = "getAllStripes", description = "Get all stripes for all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stripes successfully retrieved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Day.class))))
    })
    List<Day> getAll();

    @Operation(summary = "All stripes for user for a specific day", operationId = "getStripesForOneUser", description = "Get all stripes for one user in total or single day")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stripes successfully retrieved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Day.class))))
    })
    Day getFromSingleUser(Integer id, String data);

    @Operation(summary = "Total stripes number", operationId = "getTotalStripesForUser", description = "Get the total number of stripes for one person")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total stripes successfully retrieved", content = @Content(schema = @Schema(implementation = Integer.class)))
    })
    Integer getTotalStripesForUser(Integer id);

    @Operation(summary = "Stripes month", operationId = "getTotalStripesForUserPerMonth", description = "Get the total number of stripes combined with the month")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total stripes/months successfully retrieved")
    })
    Map<LocalDate, Integer> getTotalStripesPerMonth(Integer id);

    @Operation(summary = "Edit stripe(s) user", operationId = "editStripesForUser", description = "Edit stripe(s) for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stripe successfully edited"),
            @ApiResponse(responseCode = "404", description = "The requested user could not be found")
    })
    void editStripeForUser(Integer id, String date, Integer amount);
}
