package com.metlife.core.response;

import com.metlife.core.exception.ErrorCode;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DataResponse<T> {

  private final static String SUCCESS_MESSAGE = "SUCCESS";

  private String message;
  private T response;

  public DataResponse(String message, T data) {
    this.message = message;
    this.response = data;
  }

  public static <T> DataResponse<T> response(T data) {
    return DataResponse.<T>builder()
        .message(SUCCESS_MESSAGE)
        .response(data)
        .build();
  }

  public static <T> DataResponse<T> errorResponse(ErrorCode errorCode) {
    return DataResponse.<T>builder()
        .message(errorCode.name())
        .response((T) errorCode.getDetail())
        .build();
  }

}
