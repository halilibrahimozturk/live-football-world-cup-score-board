package org.ozturk;

import java.time.LocalDateTime;

/**
 * Represents a football match between two teams with score tracking and start time.
 * Immutable for team names and start time; mutable only for score updates.
 */
public class Match {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final LocalDateTime startTime;
    public static final String ERROR_SAME_OR_NULL_TEAMS = "Teams must be different and non-null";
    public static final String ERROR_NEGATIVE_SCORE = "Scores must be non-negative";

    /**
     * Constructs a Match with specified teams and default scores (0-0).
     *
     * @param homeTeam home team name
     * @param awayTeam away team name
     * @throws IllegalArgumentException if names are null or equal (ignoring case)
     */
    public Match(String homeTeam, String awayTeam) {
        // Validates that the teams are non-null and different
        if (homeTeam == null || awayTeam == null || homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException(ERROR_SAME_OR_NULL_TEAMS);
        }
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = LocalDateTime.now(); // Records the current time as the match start time
    }

    /**
     * Updates the score of the match.
     *
     * @param homeScore new home team score
     * @param awayScore new away team score
     * @throws IllegalArgumentException if scores are negative
     */
    public void updateScore(int homeScore, int awayScore) {
        // Validates that the scores are non-negative
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_SCORE);
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
