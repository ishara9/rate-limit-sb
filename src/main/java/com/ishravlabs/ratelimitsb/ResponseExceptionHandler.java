package com.ishravlabs.ratelimitsb;

import com.ishravlabs.ratelimitsb.error.CustomError;
import java.time.LocalDate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {CustomException.class})
  protected ResponseEntity<Object> handeConflict(RuntimeException ex, WebRequest request) {
    CustomError customError =
        CustomError.builder()
            .error(ex.getCause().getMessage())
            .message(ex.getMessage())
            .timestamp(LocalDate.now().toString())
            .path(request.getDescription(false))
            .status(HttpStatus.BAD_REQUEST.value())
            .build();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("error-header", ex.getMessage());

    return handleExceptionInternal(ex, customError, httpHeaders, HttpStatus.BAD_REQUEST, request);
  }
}
