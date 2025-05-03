package org.ozturk;

import org.ozturk.model.Match;
import org.ozturk.service.ScoreboardService;
import org.ozturk.service.impl.ScoreboardServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        ScoreboardService scoreboard = new ScoreboardServiceImpl();

        // Start matches
        scoreboard.startMatch("Turkiye", "Norway");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Spain", "Italy");

        // Update scores
        scoreboard.updateScore("Turkiye", "Norway", 2, 1);
        scoreboard.updateScore("Germany", "France", 1, 1);
        scoreboard.updateScore("Spain", "Italy", 3, 2);

        // Print current summary
        System.out.println("\n--- Current Matches ---");
        printMatches(scoreboard.getSummary());

        // Finish a match
        scoreboard.finishMatch("Germany", "France");

        // Print updated summary
        System.out.println("\n--- Matches After Finishing One ---");
        printMatches(scoreboard.getSummary());
    }

    private static void printMatches(List<Match> matches) {
        for (Match match : matches) {
            System.out.println(match.getHomeTeam() + " " + match.getHomeScore() + " - " +
                    match.getAwayScore() + " " + match.getAwayTeam());
        }
    }
}