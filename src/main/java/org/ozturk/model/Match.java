package org.ozturk.model;

import org.ozturk.exception.InvalidScoreException;
import org.ozturk.exception.TeamValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

/**
 * Represents a football match between two teams with score tracking and start time.
 * Immutable for team names and start time; mutable only for score updates.
 * <p>
 * This class validates the team names, ensures scores are non-negative, and allows
 * score updates during the match. It logs significant events and errors for debugging.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 * Match match = new Match("Team A", "Team B");
 * match.updateScore(1, 0);
 * System.out.println(match.getHomeScore()); // 1
 * </pre>
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
     * <p>
     * If the team names are invalid (null, empty, or equal), a {@link TeamValidationException}
     * is thrown.
     * </p>
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
     * <p>
     * If either score is negative, an {@link InvalidScoreException} is thrown.
     * </p>
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

    /**
     * Gets the name of the home team.
     *
     * @return the name of the home team
     */
    public String getHomeTeam() {
        return homeTeam;
    }

    /**
     * Gets the name of the away team.
     *
     * @return the name of the away team
     */
    public String getAwayTeam() {
        return awayTeam;
    }

    /**
     * Gets the score of the home team.
     *
     * @return the home team's score
     */
    public int getHomeScore() {
        return homeScore;
    }

    /**
     * Gets the score of the away team.
     *
     * @return the away team's score
     */
    public int getAwayScore() {
        return awayScore;
    }

    /**
     * Gets the start time of the match.
     *
     * @return the start time of the match
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Creates a unique key representing the match between two teams.
     * <p>
     * The key is a lowercase string of the format "homeTeam vs awayTeam".
     * </p>
     *
     * @param homeTeam the home team name
     * @param awayTeam the away team name
     * @return a unique match key
     */
    public static String createMatchKey(String homeTeam, String awayTeam) {
        return (homeTeam + VS + awayTeam).toLowerCase();
    }

    /**
     * Validates the team name.
     * <p>
     * The team name must be alphanumeric and less than 50 characters.
     * </p>
     *
     * @param teamName the name of the team to validate
     * @throws TeamValidationException if the team name is invalid
     */
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

    /**
     * Converts a string to CamelCase format.
     * <p>
     * This method capitalizes the first letter of each word and makes all others lowercase.
     * </p>
     *
     * @param input the string to convert
     * @return the CamelCase version of the input string
     */
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
