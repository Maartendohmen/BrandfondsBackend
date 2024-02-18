package nl.brandfonds.Brandfonds.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripesPerMonth {
    String month;
    Integer stripes;
    Float costs;
}
