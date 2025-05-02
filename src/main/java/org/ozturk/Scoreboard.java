package org.ozturk;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Scoreboard for managing football matches: creation, scoring, and summary.
 */
public class Scoreboard {
    private final Map<String, Match> matchesInProgress;
    public static final String ERROR_MATCH_EXISTS = "Match already exists";
    public static final String ERROR_TEAM_ALREADY_PLAYING = "One or both teams are already playing";
    public static final String ERROR_MATCH_NOT_FOUND = "Match not found";
    public static final String ERROR_SAME_OR_NULL_TEAMS = "Teams must be different and non-null";
    public static final String VS = "  vs  "; // Separator for match key

    // Constructor to initialize the scoreboard with an empty map of matches
    public Scoreboard() {
        this.matchesInProgress = new LinkedHashMap<>();
    }

    /**
     * Starts a new match if teams are not already playing or duplicated.
     *
     * @param homeTeam home team name
     * @param awayTeam away team name
     * @throws IllegalArgumentException for invalid input or active teams
     */
    public void startMatch(String homeTeam, String awayTeam) {
        // Validates that the teams are not already playing or duplicated
        validateNewMatch(homeTeam, awayTeam);

        // Creates a unique key for the match and stores it in the map
        String key = createMatchKey(homeTeam, awayTeam);

        // Adds the new match to the scoreboard
        matchesInProgress.put(key, new Match(homeTeam, awayTeam));
    }

    /**
     * Updates the score of an existing match.
     *
     * @param homeTeam  home team name
     * @param awayTeam  away team name
     * @param homeScore new home score
     * @param awayScore new away score
     * @throws IllegalArgumentException if match doesn't exist
     */
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        // Retrieves the existing match and updates its score
        Match match = getMatch(homeTeam, awayTeam);
        match.updateScore(homeScore, awayScore);
    }

    /**
     * Finishes a match and removes it from the scoreboard.
     *
     * @param homeTeam home team name
     * @param awayTeam away team name
     * @throws IllegalArgumentException if match doesn't exist
     */
    public void finishMatch(String homeTeam, String awayTeam) {
        // Generates the match key and removes the match from the scoreboard if it exists
        String key = createMatchKey(homeTeam, awayTeam);

        // Throws exception if match is not found
        if (!matchesInProgress.containsKey(key)) {
            throw new IllegalArgumentException(ERROR_MATCH_NOT_FOUND);
        }

        // Removes the match from the scoreboard
        matchesInProgress.remove(key);
    }

    /**
     * Returns a summary of matches sorted by total score (desc) and then recency (desc).
     *
     * @return ordered list of matches
     */
    public List<Match> getSummary() {
        return matchesInProgress.values().stream()
                .sorted((m1, m2) -> {
                    int score1 = m1.getHomeScore() + m1.getAwayScore();
                    int score2 = m2.getHomeScore() + m2.getAwayScore();

                    // If scores are different, the match with higher score comes first
                    if (score1 != score2) {
                        return Integer.compare(score2, score1); // Highest score is first
                    } else {
                        // If scores are the same, the most recent match comes first
                        return m2.getStartTime().compareTo(m1.getStartTime()); // Most recent match is first
                    }
                })
                .collect(Collectors.toList());
    }


    // Retrieves an ongoing match based on the team names
    private Match getMatch(String homeTeam, String awayTeam) {
        Match match = matchesInProgress.get(createMatchKey(homeTeam, awayTeam));
        if (match == null) throw new IllegalArgumentException(ERROR_MATCH_NOT_FOUND);
        return match;
    }

    // Creates a unique key for the match based on the home and away team names
    private String createMatchKey(String homeTeam, String awayTeam) {
        return (homeTeam + VS + awayTeam).toLowerCase();
    }

    private void validateNewMatch(String homeTeam, String awayTeam) {
        // Validates that the teams are not null or identical
        if (homeTeam == null || awayTeam == null || homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException(ERROR_SAME_OR_NULL_TEAMS);
        }

        // Checks if the match already exists
        if (matchesInProgress.containsKey(createMatchKey(homeTeam, awayTeam))) {
            throw new IllegalArgumentException(ERROR_MATCH_EXISTS);
        }

        // Checks if either team is already playing in another match
        boolean homePlaying = matchesInProgress.values().stream()
                .anyMatch(m -> m.getHomeTeam().equalsIgnoreCase(homeTeam) || m.getAwayTeam().equalsIgnoreCase(homeTeam));
        boolean awayPlaying = matchesInProgress.values().stream()
                .anyMatch(m -> m.getHomeTeam().equalsIgnoreCase(awayTeam) || m.getAwayTeam().equalsIgnoreCase(awayTeam));
        if (homePlaying || awayPlaying) {
            throw new IllegalArgumentException(ERROR_TEAM_ALREADY_PLAYING);
        }
    }
}
