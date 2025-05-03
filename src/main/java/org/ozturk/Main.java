package org.ozturk;

import org.ozturk.model.Match;
import org.ozturk.service.ScoreboardService;
import org.ozturk.service.impl.ScoreboardServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.printf("\nHello! This is `Live Football World Cup Score Board`!\n");

        //  Simulate use of `Live Football World Cup Score Board` Library
        ScoreboardService scoreboardService = new ScoreboardServiceImpl();

        // Start matches
        scoreboardService.startMatch("Turkiye", "Norway");
        scoreboardService.startMatch("Spain", "Brazil");
        scoreboardService.startMatch("Germany", "France");
        scoreboardService.startMatch("Uruguay", "Italy");
        scoreboardService.startMatch("Argentina", "Australia");

        // Update scores
        scoreboardService.updateScore("Turkiye", "Norway", 0, 5);
        scoreboardService.updateScore("Spain", "Brazil", 10, 2);
        scoreboardService.updateScore("Germany", "France", 2, 2);
        scoreboardService.updateScore("Uruguay", "Italy", 6, 6);
        scoreboardService.updateScore("Argentina", "Australia", 3, 1);

        // Print summary
        System.out.println("\n=== Match Summary ===");
        List<Match> summary = scoreboardService.getSummary();
        for (Match match : summary) {
            System.out.println(match.getHomeTeam() + " " + match.getHomeScore() + " - " +
                    match.getAwayTeam() + " " + match.getAwayScore());
        }

        // Finish a match
        scoreboardService.finishMatch("Germany", "France");

        System.out.println("\n=== After Finishing Germany vs France ===");
        summary = scoreboardService.getSummary();
        for (Match match : summary) {
            System.out.println(match.getHomeTeam() + " " + match.getHomeScore() + " - " +
                    match.getAwayTeam() + " " + match.getAwayScore());
        }
    }
}