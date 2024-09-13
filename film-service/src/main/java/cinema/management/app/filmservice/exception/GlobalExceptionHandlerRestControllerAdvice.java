package cinema.management.app.filmservice.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandlerRestControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

        problemDetail.setProperty("errors",
                exception.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList());

        log.info(exception.getMessage());

        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(CustomException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(exception.getHttpStatus(), exception.getMessage());

        problemDetail.setProperty("errors", List.of(exception.getMessage()));

        log.info(exception.getMessage());

        return ResponseEntity.status(exception.getHttpStatus())
                .body(problemDetail);
    }
}
