package com.ishravlabs.ratelimitsb;

public class AreaV1 {
  private final String rectangle;
  private final long area;

  public AreaV1(String rectangle, long area) {
    this.rectangle = rectangle;
    this.area = area;
  }

  public String getRectangle() {
    return rectangle;
  }

  public long getArea() {
    return area;
  }
}
