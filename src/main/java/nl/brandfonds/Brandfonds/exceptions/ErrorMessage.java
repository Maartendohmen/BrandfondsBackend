package nl.brandfonds.Brandfonds.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private Date timestamp;
    private String message;
    private String type;

    public ErrorMessage(Date timestamp, String message, String type) {
        this.timestamp = timestamp;
        this.message = message;
        this.type = type;
    }

}
