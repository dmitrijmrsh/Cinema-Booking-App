package cinema.management.app.apigateway.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(CustomException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatus(exception.getHttpStatus());

        problemDetail.setProperty("errors", List.of(exception.getMessage()));

        return ResponseEntity.status(exception.getHttpStatus())
                .body(problemDetail);
    }

}
