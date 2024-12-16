package cinema.management.app.ticketsservice.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandlerControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setProperty(
                "errors",
                exception.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList()
        );

        log.info("Bind exception: {}", exception.getMessage());

        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

}
