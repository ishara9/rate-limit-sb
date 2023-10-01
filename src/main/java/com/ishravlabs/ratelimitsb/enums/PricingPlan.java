package com.ishravlabs.ratelimitsb.enums;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public enum PricingPlan {
  FREE {
    public Bandwidth getLimit() {
      return Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1)));
    }
  },
  BASIC {
    public Bandwidth getLimit() {
      return Bandwidth.classic(50, Refill.intervally(50, Duration.ofMinutes(1)));
    }
  };

  public abstract Bandwidth getLimit();

  public static PricingPlan resolvePlanFromApiKey(String apiKey) {
    if (apiKey == null) {
      return FREE;
    } else if (apiKey.startsWith("BX001-")) {
      return BASIC;
    }
    return FREE;
  }
}
