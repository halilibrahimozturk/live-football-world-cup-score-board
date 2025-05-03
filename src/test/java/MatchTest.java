import org.junit.jupiter.api.Test;
import org.ozturk.exception.TeamValidationException;
import org.ozturk.model.Match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This test class written following Test-Driven-Development (TDD) principles to implement
 * Match related functions and edge cases of `Live Football World Cup Score Board`
 *
 * @author Halil Ibrahim Ozturk
 * @version 1.0
 */
public class MatchTest {

    /**
     * Tests that creating a Match with null home or away team throws an exception.
     */
    @Test
    void shouldNotAllowNullTeamNamesInMatch() {
        assertThrows(TeamValidationException.class, () -> {
            new Match(null, "Norway");
        });

        assertThrows(TeamValidationException.class, () -> {
            new Match("Turkiye", null);
        });

        assertThrows(TeamValidationException.class, () -> {
            new Match(null, null);
        });
    }

    /**
     * Tests the correct initialization of a match with valid team names.
     */
    @Test
    void shouldInitializeMatchCorrectly() {
        Match match = new Match("Turkiye", "Norway");
        assertEquals("Turkiye", match.getHomeTeam());
        assertEquals("Norway", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }
}