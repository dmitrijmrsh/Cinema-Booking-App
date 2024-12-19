package cinema.management.app.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(
            CustomException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatus(exception.getStatus());

        problemDetail.setProperty("errors", List.of(exception.getMessage()));

        return ResponseEntity.status(exception.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(UnsupportedRoleException.class)
    public ResponseEntity<ProblemDetail> handleUnsupportedRoleException(
            UnsupportedRoleException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setProperty("errors", List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }

}
