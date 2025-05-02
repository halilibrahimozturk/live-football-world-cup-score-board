import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ozturk.Match;
import org.ozturk.Scoreboard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test class written following Test-Driven-Development (TDD) principles to implement
 * following basic functions and test edge cases of `Live Football World Cup Score Board`
 * Functions :
 * 1- Start a new match
 * 2- Update score
 * 3- Finish match
 * 4- Get a summary
 *
 * @author Halil Ibrahim Ozturk
 * @version 1.0
 */
public class ScoreboardTest {

    private Scoreboard scoreboard;

    /**
     * Initializes a new Scoreboard instance before each test.
     */
    @BeforeEach
    void setup() {
        scoreboard = new Scoreboard();
    }

    /**
     * Tests that a new match is started with initial score 0-0,
     * and that the teams are correctly stored in the match object.
     */
    @Test
    void shouldStartANewMatchWithZeroScores() {
        // Start match function
        scoreboard.startMatch("Turkiye", "Norway");

        // There is only 1 match ongoing
        List<Match> summary = scoreboard.getSummary();
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
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch(null, "Norway");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch("Turkiye", null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch(null, null);
        });
    }

    /**
     * Tests that creating a Match with null home or away team throws an exception.
     */
    @Test
    void shouldNotAllowNullTeamNamesInMatch() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Match(null, "Norway");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Match("Turkiye", null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Match(null, null);
        });
    }

    /**
     * Tests that starting a duplicate match throws an exception.
     */
    @Test
    void shouldNotAllowDuplicateMatch() {
        scoreboard.startMatch("Turkiye", "Norway");

        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch("Turkiye", "Norway");
        });
    }

    /**
     * Tests that starting a duplicate match with different casing
     * throws an exception (case-insensitive check).
     */
    @Test
    void shouldTreatTeamNamesCaseInsensitively() {
        scoreboard.startMatch("Turkiye", "Norway");
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch("turkiye", "norway");
        });
    }

    /**
     * Tests that starting a match with the same team as both home and away
     * throws an exception.
     */
    @Test
    void shouldNotAllowSameTeamAsHomeAndAway() {
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch("Turkiye", "Turkiye");
        });
    }

    /**
     * Tests that a team cannot participate in more than one ongoing match.
     * Starting a new match involving an already assigned team throws an exception.
     */
    @Test
    void shouldNotAllowTeamToPlayInMoreThanOneMatch() {
        scoreboard.startMatch("Turkiye", "Norway");

        // Turkiye is already playing
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch("Turkiye", "Sweden");
        });

        // Norway is already playing
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.startMatch("Spain", "Norway");
        });
    }

    /**
     * Tests that updating the score of an existing match correctly
     * updates the match's internal score values.
     */
    @Test
    void shouldUpdateMatchScore() {
        scoreboard.startMatch("Turkiye", "Norway");

        // Update score of match with scores
        scoreboard.updateScore("Turkiye", "Norway", 1, 2);

        Match match = scoreboard.getSummary().get(0);
        assertEquals(1, match.getHomeScore());
        assertEquals(2, match.getAwayScore());
    }

    /**
     * Tests that updating a match with negative scores
     * throws an IllegalArgumentException.
     */
    @Test
    void shouldNotAllowNegativeScores() {
        scoreboard.startMatch("Turkiye", "Norway");

        // Negative home score
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.updateScore("Turkiye", "Norway", -1, 0);
        });

        // Negative away score
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.updateScore("Turkiye", "Norway", 2, -3);
        });

        // Both scores negative
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.updateScore("Turkiye", "Norway", -1, -1);
        });
    }

    /**
     * Tests that attempting to update the score of a non-existent match
     * throws an exception.
     */
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMatch() {
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.updateScore("Turkiye", "Norway", 1, 1);
        });
    }

    /**
     * Tests that updating the score of a finished match
     * throws an exception.
     */
    @Test
    void shouldNotAllowScoreUpdateAfterMatchFinished() {
        scoreboard.startMatch("Turkiye", "Norway");
        scoreboard.finishMatch("Turkiye", "Norway");

        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.updateScore("Turkiye", "Norway", 1, 1);
        });
    }

    /**
     * Tests that finishing a match removes it from the scoreboard.
     */
    @Test
    void shouldFinishMatchAndRemoveItFromScoreboard() {
        scoreboard.startMatch("Turkiye", "Norway");

        // Finish the match by removing from summary list
        scoreboard.finishMatch("Turkiye", "Norway");
        assertTrue(scoreboard.getSummary().isEmpty());
    }

    /**
     * Tests that attempting to finish a non-existent match
     * throws an exception.
     */
    @Test
    void shouldThrowExceptionWhenFinishingNonExistingMatch() {
        assertThrows(IllegalArgumentException.class, () -> {
            scoreboard.finishMatch("Spain", "Brazil");
        });
    }

    /**
     * Tests that the summary of ongoing matches is sorted correctly:
     * - First by total score (descending)
     * - Then by most recently started match if scores are equal
     */
    @Test
    void shouldSortMatchesByTotalScoreThenByRecency() throws InterruptedException {
        scoreboard.startMatch("Turkiye", "Norway");
        scoreboard.updateScore("Turkiye", "Norway", 5, 0);

        Thread.sleep(1); // To make a time differance

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        Thread.sleep(1); // To make a time differance

        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        Thread.sleep(1); // To make a time differance

        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);

        Thread.sleep(1); // To make a time differance

        scoreboard.startMatch("Argentina", "Australia");
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        Thread.sleep(1); // To make a time differance

        // Get summary ordered by the most recently if the total score is equal
        List<Match> summary = scoreboard.getSummary();

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
        assertTrue(scoreboard.getSummary().isEmpty());
    }

}
