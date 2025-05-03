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

    /**
     * Test case to ensure that special characters are not allowed in team names.
     * It checks that a TeamValidationException is thrown when special characters
     * like "!" and "@" or "$" and "%" are included in the team names.
     */
    @Test
    void shouldNotAllowSpecialCharactersInTeamNames() {
        assertThrows(TeamValidationException.class, () -> {
            new Match("Turkiye!@", "Norway");
        });

        assertThrows(TeamValidationException.class, () -> {
            new Match("Turkiye", "Norway$%");
        });
    }

    /**
     * Test case to ensure that team names do not exceed the maximum allowed length.
     * It checks that a TeamValidationException is thrown when the team name exceeds 50 characters.
     * In this case, the team name is set to 51 characters long.
     */
    @Test
    void shouldEnforceMaxTeamNameLength() {
        String longTeamName = "A".repeat(51); // 51 characters long
        assertThrows(TeamValidationException.class, () -> {
            new Match(longTeamName, "Norway");
        });
    }

    /**
     * Test case to ensure that team names are properly converted to camel case format.
     * It checks that team names like "turkIYE" and "nOrway" are correctly converted to "Turkiye" and "Norway"
     * when assigned to the Match object.
     */
    @Test
    void shouldConvertTeamNamesToCamelCase() {
        Match match = new Match("turkIYE", "nOrway");
        assertEquals("Turkiye", match.getHomeTeam());
        assertEquals("Norway", match.getAwayTeam());
    }
}