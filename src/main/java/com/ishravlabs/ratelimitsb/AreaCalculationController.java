package com.ishravlabs.ratelimitsb;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaCalculationController {

  private final Bucket bucket;

  private static final org.slf4j.Logger log = LoggerFactory.getLogger(AreaCalculationController.class);

  public AreaCalculationController() {

    Refill refill = Refill.intervally(5, Duration.ofSeconds(20));
    Bandwidth limit = Bandwidth.classic(100, refill);
    this.bucket = Bucket.builder().addLimit(limit).build();
  }

  @PostMapping("/api/v1/rectangle/area")
  public ResponseEntity<AreaV1> rectangleArea(@RequestBody RectangleDimensionsV1 dimensions) {
    ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
    log.info("consumptionProbe values: {}",consumptionProbe);
    if (consumptionProbe.isConsumed()) {
      long requests = bucket.getAvailableTokens();
      return ResponseEntity.ok(
          new AreaV1("rectangle", dimensions.getLength() * dimensions.getWidth(), requests));
    }
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
  }
}
