package org.ozturk.service;

import org.ozturk.model.Match;

import java.util.List;

/**
 * The ScoreboardService interface defines the operations supported by a live
 * football World Cup scoreboard service. It allows starting new matches, updating scores,
 * finishing matches, and retrieving a summary of ongoing matches.
 *
 * <p>Matches are uniquely identified by their home and away team names. The scoreboard
 * maintains a list of matches currently in progress.</p>
 *
 * @author Halil Ibrahim Ozturk
 */
public interface ScoreboardService {
    void startMatch(String homeTeam, String awayTeam);

    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    void finishMatch(String homeTeam, String awayTeam);

    List<Match> getSummary();
}
