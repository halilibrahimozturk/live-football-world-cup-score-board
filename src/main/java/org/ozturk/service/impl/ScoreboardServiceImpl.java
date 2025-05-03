package org.ozturk.service.impl;

import org.ozturk.exception.MatchAlreadyExistsException;
import org.ozturk.exception.MatchNotFoundException;
import org.ozturk.exception.TeamAlreadyPlayingException;
import org.ozturk.exception.TeamValidationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ozturk.model.Match;
import org.ozturk.service.ScoreboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

/**
 * Scoreboard for managing football matches: creation, scoring, and summary.
 *
 * @author Halil Ibrahim Ozturk
 */
public class ScoreboardServiceImpl implements ScoreboardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreboardServiceImpl.class);

    private final Map<String, Match> matchesInProgress;

    // Error messages
    public static final String ERROR_MATCH_EXISTS = "Match already exists";
    public static final String ERROR_TEAM_ALREADY_PLAYING = "One or both teams are already playing";
    public static final String ERROR_MATCH_NOT_FOUND = "Match not found";
    public static final String ERROR_SAME_OR_NULL_TEAMS = "Teams must be different and non-null";

    // Log message templates
    public static final String LOG_FINISHING_MATCH = "Finishing match: %s vs %s";
    public static final String LOG_SUMMARY_REQUESTED = "Generating match summary";

    public static final String LOG_VALIDATION_FAILED_SAME_OR_NULL_TEAMS = "Validation failed - %s: %s vs %s";
    public static final String LOG_VALIDATION_FAILED_MATCH_EXISTS = "Validation failed - %s: %s vs %s";
    public static final String LOG_VALIDATION_FAILED_TEAM_ALREADY_PLAYING = "Validation failed - %s: %s vs %s";
    public static final String LOG_MATCH_NOT_FOUND_SCORE_UPDATE = "Score update failed - %s: %s vs %s";
    public static final String LOG_MATCH_NOT_FOUND_FINISH = "Finish failed - %s: %s vs %s";

    public ScoreboardServiceImpl() {
        this.matchesInProgress = new LinkedHashMap<>();
    }

    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        validateNewMatch(homeTeam, awayTeam);

        String key = Match.createMatchKey(homeTeam, awayTeam);

        matchesInProgress.put(key, new Match(homeTeam, awayTeam));
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Match match = getMatch(homeTeam, awayTeam);

        match.updateScore(homeScore, awayScore);
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        String key = Match.createMatchKey(homeTeam, awayTeam);

        if (!matchesInProgress.containsKey(key)) {
            LOGGER.warn(String.format(LOG_MATCH_NOT_FOUND_FINISH, ERROR_MATCH_NOT_FOUND, homeTeam, awayTeam));
            throw new MatchNotFoundException(ERROR_MATCH_NOT_FOUND);
        }

        LOGGER.info(String.format(LOG_FINISHING_MATCH, homeTeam, awayTeam));
        matchesInProgress.remove(key);
    }

    @Override
    public List<Match> getSummary() {
        LOGGER.info(LOG_SUMMARY_REQUESTED);

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

    private Match getMatch(String homeTeam, String awayTeam) {
        Match match = matchesInProgress.get(Match.createMatchKey(homeTeam, awayTeam));
        if (match == null) {
            LOGGER.warn(String.format(LOG_MATCH_NOT_FOUND_SCORE_UPDATE, ERROR_MATCH_NOT_FOUND, homeTeam, awayTeam));
            throw new MatchNotFoundException(ERROR_MATCH_NOT_FOUND);
        }
        return match;
    }

    private void validateNewMatch(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null || homeTeam.equalsIgnoreCase(awayTeam)) {
            LOGGER.warn(String.format(LOG_VALIDATION_FAILED_SAME_OR_NULL_TEAMS, ERROR_SAME_OR_NULL_TEAMS, homeTeam, awayTeam));
            throw new TeamValidationException(ERROR_SAME_OR_NULL_TEAMS);
        }

        if (matchesInProgress.containsKey(Match.createMatchKey(homeTeam, awayTeam))) {
            LOGGER.warn(String.format(LOG_VALIDATION_FAILED_MATCH_EXISTS, ERROR_MATCH_EXISTS, homeTeam, awayTeam));
            throw new MatchAlreadyExistsException(ERROR_MATCH_EXISTS);
        }

        boolean homePlaying = matchesInProgress.values().stream()
                .anyMatch(m -> m.getHomeTeam().equalsIgnoreCase(homeTeam) || m.getAwayTeam().equalsIgnoreCase(homeTeam));
        boolean awayPlaying = matchesInProgress.values().stream()
                .anyMatch(m -> m.getHomeTeam().equalsIgnoreCase(awayTeam) || m.getAwayTeam().equalsIgnoreCase(awayTeam));

        if (homePlaying || awayPlaying) {
            LOGGER.warn(String.format(LOG_VALIDATION_FAILED_TEAM_ALREADY_PLAYING, ERROR_TEAM_ALREADY_PLAYING, homeTeam, awayTeam));
            throw new TeamAlreadyPlayingException(ERROR_TEAM_ALREADY_PLAYING);
        }
    }
}
