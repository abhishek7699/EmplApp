package com.EmplApp.EmplApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDate;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        // This returns a clean string and a 404 Status Code to React/Postman
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmployeeExistsException.class)
    public ResponseEntity<String> EmployeeExistsException(EmployeeExistsException ex) {
        // This returns a clean string and a 404 Status Code to React/Postman
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(NoResourceFoundException ex) {
        // You can make this more informative
        String message = "Resource not found: " + ex.getMessage();  // or just "404 - Not Found"
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    // Optional: Catch-all fallback (very useful for debugging)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log it in real app: logger.error("Unhandled exception", ex);
        return new ResponseEntity<>("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
//
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(err -> err.getField() + ": " + err.getDefaultMessage())
//                .toList();
//
//        ErrorResponse response = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                "Validation failed",
//                errors,
//                LocalDate.now()
//        );
//
//        return ResponseEntity.badRequest().body(response);
//    }

}
