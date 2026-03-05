package com.atlassian_ratings;

import java.io.IOException;

class Main {

  public static void main(String[] args) {
    RatingSystem ratingSystem = new RatingSystem();

    // Base: Overall ratings example
    System.out.println("=== Base: Overall Ratings Example ===");
    ratingSystem.acceptRating("agent1", 5);
    ratingSystem.acceptRating("agent1", 4);

    ratingSystem.acceptRating("agent2", 5);
    ratingSystem.acceptRating("agent2", 5);

    ratingSystem.acceptRating("agent3", 5);

    System.out.println("All agents by average rating:");
    ratingSystem.displayAllAgentsByAverageRating();
    System.out.println();

    // Scale-up: Monthly ratings example
    System.out.println("=== Scale-up: Monthly Ratings Example ===");
    RatingSystem monthlyRatingSystem = new RatingSystem();

    // January 2024 ratings
    monthlyRatingSystem.acceptRating("agentA", 5, "2024-01-15");
    monthlyRatingSystem.acceptRating("agentA", 4, "2024-01-20");
    monthlyRatingSystem.acceptRating("agentA", 5, "2024-01-25");

    monthlyRatingSystem.acceptRating("agentB", 5, "2024-01-10");
    monthlyRatingSystem.acceptRating("agentB", 5, "2024-01-18");

    monthlyRatingSystem.acceptRating("agentC", 4, "2024-01-12");
    monthlyRatingSystem.acceptRating("agentC", 3, "2024-01-22");

    // February 2024 ratings
    monthlyRatingSystem.acceptRating("agentA", 4, "2024-02-05");
    monthlyRatingSystem.acceptRating("agentA", 3, "2024-02-10");

    monthlyRatingSystem.acceptRating("agentB", 5, "2024-02-08");
    monthlyRatingSystem.acceptRating("agentB", 5, "2024-02-15");
    monthlyRatingSystem.acceptRating("agentB", 4, "2024-02-20");

    // Get top agents for January 2024
    monthlyRatingSystem.displayTopAgentsByMonth("2024-01-15\n");

    // Get top agents for February 2024
    monthlyRatingSystem.displayTopAgentsByMonth("2024-02-10\n");

    // Overall ratings across all months
    System.out.println("Overall ratings (all months combined):");
    monthlyRatingSystem.displayAllAgentsByAverageRating();
    System.out.println();

    // Scale-up: Export functionality demonstration
    System.out.println("=== Scale-up: Export Functionality ===");
    try {
      // Export to CSV
      monthlyRatingSystem.exportToCSV("monthly_ratings.csv");
      System.out.println("✓ Exported to CSV: monthly_ratings.csv");

    } catch (IOException e) {
      System.err.println("Error exporting ratings: " + e.getMessage());
    }
    System.out.println();

    // Variation: Unsorted monthly averages
    System.out.println("=== Variation: Unsorted Monthly Averages ===");
    var unsortedData = monthlyRatingSystem.getUnsortedMonthlyAverages();
    for (var data : unsortedData) {
      System.out.println("Agent: " + data.agentId +
        ", Month: " + data.month +
        ", Total Ratings: " + data.totalRatings +
        ", Average: " + String.format("%.2f", data.averageRating));
    }
    System.out.println();

  }
}
