package com.ishravlabs.ratelimitsb.services;

import com.ishravlabs.ratelimitsb.enums.PricingPlan;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PricingPlanService {
  private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

  public Bucket resolverBucket(String apiKey) {
    return cache.computeIfAbsent(apiKey, this::newBucket);
  }

  private Bucket newBucket(String apiKey) {
    PricingPlan pricingPlan = PricingPlan.resolvePlanFromApiKey(apiKey);
    return Bucket.builder().addLimit(pricingPlan.getLimit()).build();
  }
}
