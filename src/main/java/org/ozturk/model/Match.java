package org.ozturk.model;

import org.ozturk.exception.InvalidScoreException;
import org.ozturk.exception.TeamValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

/**
 * Represents a football match between two teams with score tracking and start time.
 * Immutable for team names and start time; mutable only for score updates.
 *
 * @author Halil Ibrahim Ozturk
 */
public class Match {
    private static final Logger LOGGER = LoggerFactory.getLogger(Match.class);

    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final LocalDateTime startTime;

    // Error messages
    public static final String ERROR_SAME_OR_NULL_TEAMS = "Teams must be different and non-null";
    public static final String ERROR_NEGATIVE_SCORE = "Scores must be non-negative";
    public static final String ERROR_INVALID_TEAM_NAME = "Team names must be alphanumeric and less than 50 characters";
    public static final String ERROR_LONG_TEAM_NAME = "Team name must be less than 50 characters";

    // Log messages
    public static final String LOG_INVALID_TEAM_NAME = "Team names must be alphanumeric and less than 50 characters: {}";
    public static final String LOG_LONG_TEAM_NAME = "Team name must be less than 50 characters: {}";
    private static final String LOG_MATCH_CREATED = "Match created between '{}' and '{}' at {}";
    private static final String LOG_INVALID_TEAM_NAMES = "Invalid team names: homeTeam='{}', awayTeam='{}'";
    private static final String LOG_SCORE_UPDATE_ATTEMPT = "Attempted to update score with negative value: homeScore={}, awayScore={}";
    private static final String LOG_SCORE_UPDATED = "Score updated: {} {} - {} {}";

    // Separator
    public static final String VS = "  vs  ";

    // Validation constraints
    private static final String TEAM_NAME_PATTERN = "^[a-zA-Z0-9 ]+$";  // Restricting to alphanumeric and spaces
    private static final int MAX_TEAM_NAME_LENGTH = 50;

    /**
     * Constructs a Match with specified teams and default scores (0-0).
     *
     * @param homeTeam home team name
     * @param awayTeam away team name
     * @throws TeamValidationException if names are null or equal (ignoring case)
     */
    public Match(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.equalsIgnoreCase(awayTeam)) {
            LOGGER.error(LOG_INVALID_TEAM_NAMES, homeTeam, awayTeam);
            throw new TeamValidationException(ERROR_SAME_OR_NULL_TEAMS);
        }

        validateTeamName(homeTeam);
        validateTeamName(awayTeam);

        this.homeTeam = toCamelCase(homeTeam);
        this.awayTeam = toCamelCase(awayTeam);
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = LocalDateTime.now();

        LOGGER.info(LOG_MATCH_CREATED, this.homeTeam, this.awayTeam, startTime);
    }

    /**
     * Updates the score of the match.
     *
     * @param homeScore new home team score
     * @param awayScore new away team score
     * @throws InvalidScoreException if scores are negative
     */
    public void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            LOGGER.error(LOG_SCORE_UPDATE_ATTEMPT, homeScore, awayScore);
            throw new InvalidScoreException(ERROR_NEGATIVE_SCORE);
        }

        this.homeScore = homeScore;
        this.awayScore = awayScore;
        LOGGER.info(LOG_SCORE_UPDATED, homeTeam, homeScore, awayTeam, awayScore);
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

    public static String createMatchKey(String homeTeam, String awayTeam) {
        return (homeTeam + VS + awayTeam).toLowerCase();
    }

    private void validateTeamName(String teamName) {
        if (!teamName.matches(TEAM_NAME_PATTERN)) {
            LOGGER.error(LOG_INVALID_TEAM_NAME, teamName);
            throw new TeamValidationException(ERROR_INVALID_TEAM_NAME);
        }
        if (teamName.length() > MAX_TEAM_NAME_LENGTH) {
            LOGGER.error(LOG_LONG_TEAM_NAME, teamName);
            throw new TeamValidationException(ERROR_LONG_TEAM_NAME);
        }
    }

    private String toCamelCase(String input) {
        String[] words = input.split(" ");
        StringBuilder camelCaseString = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                camelCaseString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }
        return camelCaseString.toString();
    }
}
