package cinema.management.app.webclient.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SeatAlreadyBookedException extends RuntimeException {

    private final List<String> errors;

    public SeatAlreadyBookedException(List<String> errors) {
        this.errors = errors;
    }

    public SeatAlreadyBookedException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public SeatAlreadyBookedException(String message, Throwable cause, List<String> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public SeatAlreadyBookedException(Throwable cause, List<String> errors) {
        super(cause);
        this.errors = errors;
    }

    public SeatAlreadyBookedException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace, List<String> errors
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errors = errors;
    }

}
