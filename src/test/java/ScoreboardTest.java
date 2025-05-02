import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        scoreboard.startMatch("Turkey", "Norway");

        // There is only 1 match ongoing
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());

        // First team assumed as home team
        Match match = summary.get(0);
        assertEquals("Turkey", match.getHomeTeam());
        assertEquals("Norway", match.getAwayTeam());

        // Initializes a match with 0 scores
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    /**
     * Tests that updating the score of an existing match correctly
     * updates the match's internal score values.
     */
    @Test
    void shouldUpdateMatchScore() {
        scoreboard.startMatch("Turkey", "Norway");

        // Update score of match with scores
        scoreboard.updateScore("Turkey", "Norway", 1, 2);

        Match match = scoreboard.getSummary().get(0);
        assertEquals(1, match.getHomeScore());
        assertEquals(2, match.getAwayScore());
    }

    /**
     * Tests that finishing a match removes it from the scoreboard.
     */
    @Test
    void shouldFinishMatchAndRemoveItFromScoreboard() {
        scoreboard.startMatch("Turkey", "Norway");

        // Finish the match by removing from summary list
        scoreboard.finishMatch("Turkey", "Norway");
        assertTrue(scoreboard.getSummary().isEmpty());
    }

    /**
     * Tests that the summary of ongoing matches is sorted correctly:
     * - First by total score (descending)
     * - Then by most recently started match if scores are equal
     */
    @Test
    void shouldSortMatchesByTotalScoreThenByRecency() {
        scoreboard.startMatch("Turkey", "Norway");
        scoreboard.updateScore("Turkey", "Norway", 5, 0);

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);

        scoreboard.startMatch("Argentina", "Australia");
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        // Get summary ordered by the most recently if the total score is equal
        List<Match> summary = scoreboard.getSummary();

        assertEquals("Uruguay", summary.get(0).getHomeTeam());    // 12 goals - most recent
        assertEquals("Spain", summary.get(1).getHomeTeam());      // 12 goals - earlier
        assertEquals("Turkey", summary.get(2).getHomeTeam());     // 5 goals
        assertEquals("Argentina", summary.get(3).getHomeTeam());  // 4 goals - most recent
        assertEquals("Germany", summary.get(4).getHomeTeam());    // 4 goals - earlier
    }

}
