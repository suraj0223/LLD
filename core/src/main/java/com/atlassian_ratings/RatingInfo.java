package com.atlassian_ratings;

public class RatingInfo {
  public String agentId;
  public int totalRating;
  public int numberOfRatings;

  public RatingInfo() {
    this.totalRating = 0;
    this.numberOfRatings = 0;
  }

  public double getAverageRating() {
    if (numberOfRatings == 0) {
      return 0.0;
    }
    return (double) totalRating / numberOfRatings;
  }
}

