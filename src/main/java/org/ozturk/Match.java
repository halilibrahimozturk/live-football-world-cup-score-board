package org.ozturk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

/**
 * Represents a football match between two teams with score tracking and start time.
 * Immutable for team names and start time; mutable only for score updates.
 */
public class Match {
    private static final Logger logger = LoggerFactory.getLogger(Match.class);

    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final LocalDateTime startTime;

    // Error messages
    public static final String ERROR_SAME_OR_NULL_TEAMS = "Teams must be different and non-null";
    public static final String ERROR_NEGATIVE_SCORE = "Scores must be non-negative";

    // Log messages
    private static final String LOG_MATCH_CREATED = "Match created between '{}' and '{}' at {}";
    private static final String LOG_INVALID_TEAM_NAMES = "Invalid team names: homeTeam='{}', awayTeam='{}'";
    private static final String LOG_SCORE_UPDATE_ATTEMPT = "Attempted to update score with negative value: homeScore={}, awayScore={}";
    private static final String LOG_SCORE_UPDATED = "Score updated: {} {} - {} {}";

    /**
     * Constructs a Match with specified teams and default scores (0-0).
     *
     * @param homeTeam home team name
     * @param awayTeam away team name
     * @throws IllegalArgumentException if names are null or equal (ignoring case)
     */
    public Match(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.equalsIgnoreCase(awayTeam)) {
            logger.error(LOG_INVALID_TEAM_NAMES, homeTeam, awayTeam);
            throw new IllegalArgumentException(ERROR_SAME_OR_NULL_TEAMS);
        }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = LocalDateTime.now();

        logger.info(LOG_MATCH_CREATED, homeTeam, awayTeam, startTime);
    }

    /**
     * Updates the score of the match.
     *
     * @param homeScore new home team score
     * @param awayScore new away team score
     * @throws IllegalArgumentException if scores are negative
     */
    public void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            logger.error(LOG_SCORE_UPDATE_ATTEMPT, homeScore, awayScore);
            throw new IllegalArgumentException(ERROR_NEGATIVE_SCORE);
        }

        this.homeScore = homeScore;
        this.awayScore = awayScore;
        logger.info(LOG_SCORE_UPDATED, homeTeam, homeScore, awayTeam, awayScore);
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
