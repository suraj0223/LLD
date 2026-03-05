package com.atlassian_ratings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RatingSystem {

  // agentId -> RatingInfo
  private final Map<String, RatingInfo> ratingsDatabase = new HashMap<>();
  // Monthly ratings: year-month string (yyyy-MM) -> agentId -> RatingInfo
  private final Map<String, Map<String, RatingInfo>> monthlyRatingsDatabase = new HashMap<>();

  // 1. Base: Accept a rating (uses current date)
  public void acceptRating(String agentId, int rating) {
    String currentDate = LocalDate.now().toString(); // Returns in yyyy-MM-dd format
    acceptRating(agentId, rating, currentDate);
  }

  // Overloaded method to accept rating with specific date (yyyy-MM-dd format)
  public void acceptRating(String agentId, int rating, String dateString) {
    if (rating < 1 || rating > 5) {
      throw new IllegalArgumentException("Rating must be between 1 and 5");
    }

    // Parse the date string and extract year-month
    LocalDate date = LocalDate.parse(dateString);
    int year = date.getYear();
    int month = date.getMonthValue();
    String yearMonth = String.format("%d-%02d", year, month);

    // Update overall ratings
    ratingsDatabase.putIfAbsent(agentId, new RatingInfo());
    RatingInfo overallInfo = ratingsDatabase.get(agentId);
    overallInfo.totalRating += rating;
    overallInfo.numberOfRatings += 1;
    overallInfo.agentId = agentId;

    // Update monthly ratings: yearMonth -> agentId -> RatingInfo
    monthlyRatingsDatabase.putIfAbsent(yearMonth, new LinkedHashMap<>());
    Map<String, RatingInfo> agentRatings = monthlyRatingsDatabase.get(yearMonth);
    agentRatings.putIfAbsent(agentId, new RatingInfo());
    RatingInfo monthlyInfo = agentRatings.get(agentId);
    monthlyInfo.totalRating += rating;
    monthlyInfo.numberOfRatings += 1;
    monthlyInfo.agentId = agentId;
  }

  // 2. Base: Get all agents ordered by average rating (highest → lowest)
  public List<RatingInfo> getAllAgentsByAverageRating(List<RatingInfo> ratingInfo) {
    List<RatingInfo> ratingInfos = new ArrayList<>(ratingInfo);

    // Comparator as a variable: Sort by average rating (descending), 
    // then by number of ratings (descending) as tie-breaker,
    // then by agent ID (ascending) for consistent tie-breaking
    Comparator<RatingInfo> ratingComparator = Comparator
      .comparing((RatingInfo info) -> info.getAverageRating(), Comparator.reverseOrder())
      .thenComparing((RatingInfo info) -> info.numberOfRatings, Comparator.reverseOrder())
      .thenComparing((RatingInfo info) -> info.agentId);

    ratingInfos.sort(ratingComparator);
    return ratingInfos;
  }

  // Display version of getAllAgentsByAverageRating
  public void displayAllAgentsByAverageRating() {
    List<RatingInfo> sortedAgents = getAllAgentsByAverageRating(new ArrayList<>(ratingsDatabase.values()));
    for (RatingInfo info : sortedAgents) {
      System.out.println("Agent ID: " + info.agentId +
        ", Rating Count: " + info.numberOfRatings +
        ", Average Rating: " + String.format("%.2f", info.getAverageRating()));
    }
  }

  // 3. Scale-up: Get top agents for a specific month using date string (yyyy-MM format)
  public List<RatingInfo> getTopAgentsByMonth(String dateString) {
    LocalDate date = LocalDate.parse(dateString);
    int year = date.getYear();
    int month = date.getMonthValue();
    String yearMonth = String.format("%d-%02d", year, month);

    // Get all agents' ratings for the specified month
    Map<String, RatingInfo> agentRatings = monthlyRatingsDatabase.get(yearMonth);
    return getAllAgentsByAverageRating(new ArrayList<>(agentRatings.values()));
  }

  // Display version of getTopAgentsByMonth
  public void displayTopAgentsByMonth(String dateString) {
    LocalDate date = LocalDate.parse(dateString);
    List<RatingInfo> topAgents = getTopAgentsByMonth(dateString);

    System.out.println("Top Agents for " + date.getMonth() + " " + date.getYear() + ":");
    for (RatingInfo info : topAgents) {
      System.out.println("Agent ID: " + info.agentId +
        ", Rating Count: " + info.numberOfRatings +
        ", Average Rating: " + String.format("%.2f", info.getAverageRating()));
    }
  }

  // 4. Scale-up: Export average ratings per agent per month to CSV
  public void exportToCSV(String filePath) throws IOException {
    List<MonthlyRatingData> monthlyData = getAllMonthlyRatingData();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      // Write header
      writer.write("AgentId,Month,TotalRatings,AverageRating");
      writer.newLine();

      // Write data rows
      for (MonthlyRatingData data : monthlyData) {
        writer.write(
          data.agentId + "," + data.month + "," + data.totalRatings + "," + String.format("%.2f", data.averageRating));
        writer.newLine();
      }
    }
  }

  // Helper: Collect monthly rating data (without sorting)
  private List<MonthlyRatingData> collectMonthlyRatingData() {
    List<MonthlyRatingData> monthlyData = new ArrayList<>();

    // Iterate through yearMonth -> agentId -> RatingInfo structure
    for (var yearMonthEntry : monthlyRatingsDatabase.entrySet()) {
      String yearMonth = yearMonthEntry.getKey();
      Map<String, RatingInfo> agentRatings = yearMonthEntry.getValue();

      for (var agentEntry : agentRatings.entrySet()) {
        String agentId = agentEntry.getKey();
        RatingInfo info = agentEntry.getValue();

        if (info.numberOfRatings > 0) {
          monthlyData.add(new MonthlyRatingData(
            agentId, yearMonth, info.numberOfRatings, info.getAverageRating()
          ));
        }
      }
    }

    return monthlyData;
  }

  // Helper: Get all monthly rating data (sorted)
  private List<MonthlyRatingData> getAllMonthlyRatingData() {
    List<MonthlyRatingData> monthlyData = collectMonthlyRatingData();

    // Comparator as a variable: Sort by month, then by agent ID
    Comparator<MonthlyRatingData> dataComparator = Comparator
      .comparing((MonthlyRatingData data) -> data.month)
      .thenComparing((MonthlyRatingData data) -> data.agentId);

    monthlyData.sort(dataComparator);
    return monthlyData;
  }

  // Variation: Get unsorted monthly averages
  public List<MonthlyRatingData> getUnsortedMonthlyAverages() {
    // Return unsorted data (as-is from map iteration order)
    return collectMonthlyRatingData();
  }


  // Inner class for monthly rating data
  public static class MonthlyRatingData {

    // fields start here
    public String agentId;
    public String month;
    public int totalRatings;
    public double averageRating;

    public MonthlyRatingData(String agentId, String month, int totalRatings, double averageRating) {
      this.agentId = agentId;
      this.month = month;
      this.totalRatings = totalRatings;
      this.averageRating = averageRating;
    }
  }

}

