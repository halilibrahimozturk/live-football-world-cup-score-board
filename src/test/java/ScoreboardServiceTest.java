import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ozturk.model.Match;
import org.ozturk.service.ScoreboardService;
import org.ozturk.service.impl.ScoreboardServiceImpl;
import org.ozturk.exception.TeamValidationException;
import org.ozturk.exception.MatchAlreadyExistsException;
import org.ozturk.exception.InvalidScoreException;
import org.ozturk.exception.MatchNotFoundException;
import org.ozturk.exception.TeamAlreadyPlayingException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test class written following Test-Driven-Development (TDD) principles to implement
 * following basic functions and test edge cases of `Live Football World Cup Score Board`
 * Base Functions :
 * 1- Start a new match
 * 2- Update score
 * 3- Finish match
 * 4- Get a summary
 *
 * @author Halil Ibrahim Ozturk
 * @version 1.0
 */
public class ScoreboardServiceTest {

    private ScoreboardService scoreboardService;

    /**
     * Initializes a new Scoreboard instance before each test.
     */
    @BeforeEach
    void setup() {
        scoreboardService = new ScoreboardServiceImpl();
    }

    /**
     * Tests that a new match is started with initial score 0-0,
     * and that the teams are correctly stored in the match object.
     */
    @Test
    void shouldStartANewMatchWithZeroScores() {
        // Start match function
        scoreboardService.startMatch("Turkiye", "Norway");

        // There is only 1 match ongoing
        List<Match> summary = scoreboardService.getSummary();
        assertEquals(1, summary.size());

        // First team assumed as home team
        Match match = summary.get(0);
        assertEquals("Turkiye", match.getHomeTeam());
        assertEquals("Norway", match.getAwayTeam());

        // Initializes a match with 0 scores
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    /**
     * Tests that starting a match with a null home or away team name
     * throws an IllegalArgumentException.
     */
    @Test
    void shouldNotAllowNullTeamNamesInScoreboard() {
        assertThrows(TeamValidationException.class, () -> {
            scoreboardService.startMatch(null, "Norway");
        });

        assertThrows(TeamValidationException.class, () -> {
            scoreboardService.startMatch("Turkiye", null);
        });

        assertThrows(TeamValidationException.class, () -> {
            scoreboardService.startMatch(null, null);
        });
    }

    /**
     * Tests that starting a duplicate match throws an exception.
     */
    @Test
    void shouldNotAllowDuplicateMatch() {
        scoreboardService.startMatch("Turkiye", "Norway");

        assertThrows(MatchAlreadyExistsException.class, () -> {
            scoreboardService.startMatch("Turkiye", "Norway");
        });
    }

    /**
     * Tests that starting a duplicate match with different casing
     * throws an exception (case-insensitive check).
     */
    @Test
    void shouldTreatTeamNamesCaseInsensitively() {
        scoreboardService.startMatch("Turkiye", "Norway");
        assertThrows(MatchAlreadyExistsException.class, () -> {
            scoreboardService.startMatch("turkiye", "norway");
        });
    }

    /**
     * Tests that starting a match with the same team as both home and away
     * throws an exception.
     */
    @Test
    void shouldNotAllowSameTeamAsHomeAndAway() {
        assertThrows(TeamValidationException.class, () -> {
            scoreboardService.startMatch("Turkiye", "Turkiye");
        });
    }

    /**
     * Tests that a team cannot participate in more than one ongoing match.
     * Starting a new match involving an already assigned team throws an exception.
     */
    @Test
    void shouldNotAllowTeamToPlayInMoreThanOneMatch() {
        scoreboardService.startMatch("Turkiye", "Norway");

        // Turkiye is already playing
        assertThrows(TeamAlreadyPlayingException.class, () -> {
            scoreboardService.startMatch("Turkiye", "Sweden");
        });

        // Norway is already playing
        assertThrows(TeamAlreadyPlayingException.class, () -> {
            scoreboardService.startMatch("Spain", "Norway");
        });
    }

    /**
     * Tests that updating the score of an existing match correctly
     * updates the match's internal score values.
     */
    @Test
    void shouldUpdateMatchScore() {
        scoreboardService.startMatch("Turkiye", "Norway");

        // Update score of match with scores
        scoreboardService.updateScore("Turkiye", "Norway", 1, 2);

        Match match = scoreboardService.getSummary().get(0);
        assertEquals(1, match.getHomeScore());
        assertEquals(2, match.getAwayScore());
    }

    /**
     * Tests that updating a match with negative scores
     * throws an IllegalArgumentException.
     */
    @Test
    void shouldNotAllowNegativeScores() {
        scoreboardService.startMatch("Turkiye", "Norway");

        // Negative home score
        assertThrows(InvalidScoreException.class, () -> {
            scoreboardService.updateScore("Turkiye", "Norway", -1, 0);
        });

        // Negative away score
        assertThrows(InvalidScoreException.class, () -> {
            scoreboardService.updateScore("Turkiye", "Norway", 2, -3);
        });

        // Both scores negative
        assertThrows(InvalidScoreException.class, () -> {
            scoreboardService.updateScore("Turkiye", "Norway", -1, -1);
        });
    }

    /**
     * Tests that attempting to update the score of a non-existent match
     * throws an exception.
     */
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        assertThrows(MatchNotFoundException.class, () -> {
            scoreboardService.updateScore("Turkiye", "Norway", 1, 1);
        });
    }

    /**
     * Tests that updating the score of a finished match
     * throws an exception.
     */
    @Test
    void shouldNotAllowScoreUpdateAfterMatchFinished() {
        scoreboardService.startMatch("Turkiye", "Norway");
        scoreboardService.finishMatch("Turkiye", "Norway");

        assertThrows(MatchNotFoundException.class, () -> {
            scoreboardService.updateScore("Turkiye", "Norway", 1, 1);
        });
    }

    /**
     * Tests that finishing a match removes it from the scoreboard.
     */
    @Test
    void shouldFinishMatchAndRemoveItFromScoreboard() {
        scoreboardService.startMatch("Turkiye", "Norway");

        // Finish the match by removing from summary list
        scoreboardService.finishMatch("Turkiye", "Norway");
        assertTrue(scoreboardService.getSummary().isEmpty());
    }

    /**
     * Tests that attempting to finish a non-existent match
     * throws an exception.
     */
    @Test
    void shouldThrowExceptionWhenFinishingNonExistingMatch() {
        assertThrows(MatchNotFoundException.class, () -> {
            scoreboardService.finishMatch("Spain", "Brazil");
        });
    }

    /**
     * Tests that the summary of ongoing matches is sorted correctly:
     * - First by total score (descending)
     * - Then by most recently started match if scores are equal
     */
    @Test
    void shouldSortMatchesByTotalScoreThenByRecency() throws InterruptedException {
        scoreboardService.startMatch("Turkiye", "Norway");
        scoreboardService.updateScore("Turkiye", "Norway", 5, 0);

        Thread.sleep(1); // To make a time differance

        scoreboardService.startMatch("Spain", "Brazil");
        scoreboardService.updateScore("Spain", "Brazil", 10, 2);

        Thread.sleep(1); // To make a time differance

        scoreboardService.startMatch("Germany", "France");
        scoreboardService.updateScore("Germany", "France", 2, 2);

        Thread.sleep(1); // To make a time differance

        scoreboardService.startMatch("Uruguay", "Italy");
        scoreboardService.updateScore("Uruguay", "Italy", 6, 6);

        Thread.sleep(1); // To make a time differance

        scoreboardService.startMatch("Argentina", "Australia");
        scoreboardService.updateScore("Argentina", "Australia", 3, 1);

        Thread.sleep(1); // To make a time differance

        // Get summary ordered by the most recently if the total score is equal
        List<Match> summary = scoreboardService.getSummary();

        assertEquals("Uruguay", summary.get(0).getHomeTeam());    // 12 goals - most recent
        assertEquals("Spain", summary.get(1).getHomeTeam());      // 12 goals - earlier
        assertEquals("Turkiye", summary.get(2).getHomeTeam());     // 5 goals
        assertEquals("Argentina", summary.get(3).getHomeTeam());  // 4 goals - most recent
        assertEquals("Germany", summary.get(4).getHomeTeam());    // 4 goals - earlier
    }

    /**
     * Tests that the summary is empty when no matches have been started.
     */
    @Test
    void summaryShouldBeEmptyWhenNoMatchesInProgress() {
        assertTrue(scoreboardService.getSummary().isEmpty());
    }

}
