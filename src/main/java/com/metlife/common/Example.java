package com.metlife.common;

import com.metlife.common.exception.CustomException;
import com.metlife.common.exception.ErrorCode;
import com.metlife.common.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Example {

  @GetMapping("/response-example")
  public ResponseEntity<?> get() {

    return ResponseEntity.ok().body(DataResponse.response(new ExampleResponse("1234")));

    /**
     *
     *  {
     *      "message": "SUCCESS",
     *      "response": {
     *        "id": "1234"
     *     }
     *  }
     *
     */
  }


  @GetMapping("/error-custom-example")
  public ResponseEntity<?> errorCustom() {

    throw new CustomException(ErrorCode.DUPLICATE_ID_EXCEPTION);


    /**
     *
     * {
     *  "message": "DUPLICATE_ID_EXCEPTION",
     *  "response": "중복되는 ID입니다. 다른 ID를 입력해주세요."
     * }
     *
     */
  }


  private class ExampleResponse {

    private String id;

    public ExampleResponse(String id) {
      this.id = id;
    }
  }
}
