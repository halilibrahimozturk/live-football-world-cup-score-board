package org.ozturk;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.printf("\nHello! This is `Live Football World Cup Score Board`!\n");

        //  Simulate use of `Live Football World Cup Score Board` Library
        Scoreboard scoreboard = new Scoreboard();

        // Start matches
        scoreboard.startMatch("Turkiye", "Norway");
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.startMatch("Germany", "France");
        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.startMatch("Argentina", "Australia");

        // Update scores
        scoreboard.updateScore("Turkiye", "Norway", 0, 5);
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        scoreboard.updateScore("Germany", "France", 2, 2);
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        // Print summary
        System.out.println("\n=== Match Summary ===");
        List<Match> summary = scoreboard.getSummary();
        for (Match match : summary) {
            System.out.println(match.getHomeTeam() + " " + match.getHomeScore() + " - " +
                    match.getAwayTeam() + " " + match.getAwayScore());
        }

        // Finish a match
        scoreboard.finishMatch("Germany", "France");

        System.out.println("\n=== After Finishing Germany vs France ===");
        summary = scoreboard.getSummary();
        for (Match match : summary) {
            System.out.println(match.getHomeTeam() + " " + match.getHomeScore() + " - " +
                    match.getAwayTeam() + " " + match.getAwayScore());
        }
    }
}