package com.ishravlabs.ratelimitsb;

public class AreaV1 {
  private final String rectangle;
  private final long area;

  private final long requests;

  public AreaV1(String rectangle, long area, long requests) {
    this.rectangle = rectangle;
    this.area = area;
    this.requests = requests;
  }

  public String getRectangle() {
    return rectangle;
  }

  public long getArea() {
    return area;
  }

  public long getRequests() {
    return requests;
  }
}
