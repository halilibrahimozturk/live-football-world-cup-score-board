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
    public static final String VS = "  vs  ";

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
        validateNewMatch(homeTeam, awayTeam);
        String key = createMatchKey(homeTeam, awayTeam);
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
        String key = createMatchKey(homeTeam, awayTeam);
        if (!matchesInProgress.containsKey(key)) {
            throw new IllegalArgumentException(ERROR_MATCH_NOT_FOUND);
        }
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

                    if (score1 != score2) {
                        return Integer.compare(score2, score1); // Highest score is first
                    } else {
                        return m2.getStartTime().compareTo(m1.getStartTime()); // Most recent match is first
                    }
                })
                .collect(Collectors.toList());
    }

    private Match getMatch(String homeTeam, String awayTeam) {
        Match match = matchesInProgress.get(createMatchKey(homeTeam, awayTeam));
        if (match == null) throw new IllegalArgumentException(ERROR_MATCH_NOT_FOUND);
        return match;
    }

    private String createMatchKey(String homeTeam, String awayTeam) {
        return (homeTeam + VS + awayTeam).toLowerCase();
    }

    private void validateNewMatch(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException(ERROR_SAME_OR_NULL_TEAMS);
        }
        if (matchesInProgress.containsKey(createMatchKey(homeTeam, awayTeam))) {
            throw new IllegalArgumentException(ERROR_MATCH_EXISTS);
        }
        boolean homePlaying = matchesInProgress.values().stream()
                .anyMatch(m -> m.getHomeTeam().equalsIgnoreCase(homeTeam) || m.getAwayTeam().equalsIgnoreCase(homeTeam));
        boolean awayPlaying = matchesInProgress.values().stream()
                .anyMatch(m -> m.getHomeTeam().equalsIgnoreCase(awayTeam) || m.getAwayTeam().equalsIgnoreCase(awayTeam));
        if (homePlaying || awayPlaying) {
            throw new IllegalArgumentException(ERROR_TEAM_ALREADY_PLAYING);
        }
    }
}
