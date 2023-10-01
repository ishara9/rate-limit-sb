package com.ishravlabs.ratelimitsb.controllers;

import com.ishravlabs.ratelimitsb.dto.AreaV1;
import com.ishravlabs.ratelimitsb.dto.RectangleDimensionsV1;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaCalculationController {

  private static final org.slf4j.Logger log =
      LoggerFactory.getLogger(AreaCalculationController.class);

  @PostMapping("/api/v1/rectangle/area")
  public ResponseEntity<AreaV1> rectangleArea(
      @RequestHeader(value = "X-api-key", required = false) String apiKey,
      @RequestBody RectangleDimensionsV1 dimensions) {

    return ResponseEntity.ok()
        .body(new AreaV1("rectangle", dimensions.getLength() * dimensions.getWidth()));
  }
}
