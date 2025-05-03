package org.ozturk.service;

import org.ozturk.model.Match;

import java.util.List;

/**
 * The ScoreboardService interface defines the operations supported by a live
 * football World Cup scoreboard service. It allows starting new matches, updating scores,
 * finishing matches, and retrieving a summary of ongoing matches.
 *
 * <p>Matches are uniquely identified by their home and away team names. The scoreboard
 * maintains a list of matches currently in progress. This service provides the functionality
 * to manage matches in real-time, track scores, and generate summaries of ongoing matches.</p>
 *
 * <p>Implementing classes should provide the logic for handling the operations
 * described by the methods.</p>
 *
 * @author Halil Ibrahim Ozturk
 */
public interface ScoreboardService {

    /**
     * Starts a new football match between the specified home and away teams.
     *
     * <p>This method creates a new match entry and initializes the score to 0-0.</p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws org.ozturk.exception.TeamAlreadyPlayingException if a match with the same teams is already in progress
     */
    void startMatch(String homeTeam, String awayTeam);

    /**
     * Updates the score of an ongoing match.
     *
     * <p>This method updates the score of the match between the specified home and away teams.</p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @param homeScore the new score for the home team
     * @param awayScore the new score for the away team
     * @throws org.ozturk.exception.MatchNotFoundException if no match with the given teams is found
     */
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    /**
     * Finishes an ongoing match.
     *
     * <p>This method marks the match as finished, preventing further score updates.</p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws org.ozturk.exception.MatchNotFoundException if no match with the given teams is found
     */
    void finishMatch(String homeTeam, String awayTeam);

    /**
     * Retrieves a summary of all ongoing matches.
     *
     * <p>This method returns a list of all matches currently in progress, including their
     * home and away teams and the latest scores.</p>
     *
     * @return a list of {@link Match} objects representing the ongoing matches
     */
    List<Match> getSummary();
}
