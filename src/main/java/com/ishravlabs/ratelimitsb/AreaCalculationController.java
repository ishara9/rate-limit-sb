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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaCalculationController {

  private final Bucket bucket;
  private final PricingPlanService pricingPlanService;

  private static final org.slf4j.Logger log =
      LoggerFactory.getLogger(AreaCalculationController.class);

  public AreaCalculationController(PricingPlanService pricingPlanService) {

    Refill refill = Refill.intervally(5, Duration.ofSeconds(20));
    Bandwidth limit = Bandwidth.classic(100, refill);
    this.bucket = Bucket.builder().addLimit(limit).build();
    this.pricingPlanService = pricingPlanService;
  }

  @PostMapping("/api/v1/rectangle/area")
  public ResponseEntity<AreaV1> rectangleArea(
      @RequestHeader(value = "X-api-key", required = false) String apiKey,
      @RequestBody RectangleDimensionsV1 dimensions) {

    Bucket bucket1 = pricingPlanService.resolverBucket(apiKey);

    ConsumptionProbe consumptionProbe = bucket1.tryConsumeAndReturnRemaining(1);
    log.info("consumptionProbe values: {}", consumptionProbe);
    if (consumptionProbe.isConsumed()) {
      long requests = bucket1.getAvailableTokens();
      return ResponseEntity.ok()
          .header("X-Rate-Limit-Remaining", Long.toString(consumptionProbe.getRemainingTokens()))
          .body(new AreaV1("rectangle", dimensions.getLength() * dimensions.getWidth(), requests));
    }
    long waitForRefill = consumptionProbe.getNanosToWaitForRefill() / 1_000_000_000;
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .header("X-Rate-Limit-Retry-In-Minutes", String.valueOf(waitForRefill))
        .build();
  }
}
