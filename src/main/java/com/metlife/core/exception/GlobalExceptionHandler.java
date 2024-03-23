package com.metlife.core.exception;

import com.metlife.core.response.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(value = CustomException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public DataResponse<Object> handleCustomException(CustomException e) {
    log.warn("[handleCustomException] : {}", e.getErrorCode());
    return DataResponse.errorResponse(e.getErrorCode());
  }
}
