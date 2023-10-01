package com.ishravlabs.ratelimitsb;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

@AllArgsConstructor
@Configuration
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

  private PricingPlanService pricingPlanService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String apiKey = request.getHeader("X-api-key");
    if (apiKey == null || apiKey.isEmpty()) {
      String errorMessage = "Missing Header: X-api-key";
      response.sendError(HttpStatus.BAD_REQUEST.value(), errorMessage);
//      response.getWriter().write(errorMessage);
//      response.setStatus(HttpStatus.BAD_REQUEST.value());
//      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      return false;
    }

    Bucket bucket = pricingPlanService.resolverBucket(apiKey);

    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
    log.info("Rate Limit Probe: {}", probe);

    if (probe.isConsumed()) {
      response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
      return true;
    } else {

      long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
      response.addHeader("X-Rate-Limit-Retry-After", String.valueOf(waitForRefill));
      response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Exceeded requests");
      return false;
    }
  }
}
