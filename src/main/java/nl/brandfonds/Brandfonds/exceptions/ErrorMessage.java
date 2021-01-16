package nl.brandfonds.Brandfonds.exceptions;

import java.util.Date;

public class ErrorMessage {

    private Date timestamp;
    private String message;
    private String type;

    public ErrorMessage() {
    }

    public ErrorMessage(Date timestamp, String message, String type) {
        this.timestamp = timestamp;
        this.message = message;
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }
}
