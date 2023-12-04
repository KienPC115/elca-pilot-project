package vn.elca.training.model.exceptionHandler;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.elca.training.model.exception.AddExistedProjectException;

@RestControllerAdvice
public class AppExceptionHandler {
    private static Logger logger = Logger.getLogger(AddExistedProjectException.class);
    @ExceptionHandler(AddExistedProjectException.class)
    public ResponseEntity<String> handleAddExistedProjectException(AddExistedProjectException ex) {
        logger.error(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
