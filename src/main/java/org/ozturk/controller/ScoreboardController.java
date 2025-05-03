package org.ozturk.controller;

import org.ozturk.model.Match;
import org.ozturk.service.ScoreboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scoreboard")
@CrossOrigin(origins = "http://localhost:3000")  // CORS for React
public class ScoreboardController {

    @Autowired
    private ScoreboardService scoreboardService;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public void startMatch(@RequestParam String homeTeam, @RequestParam String awayTeam) {
        scoreboardService.startMatch(homeTeam, awayTeam);
    }

    @PostMapping("/update")
    public void updateScore(@RequestParam String homeTeam, @RequestParam String awayTeam,
                            @RequestParam int homeScore, @RequestParam int awayScore) {
        scoreboardService.updateScore(homeTeam, awayTeam, homeScore, awayScore);
    }

    @PostMapping("/finish")
    public void finishMatch(@RequestParam String homeTeam, @RequestParam String awayTeam) {
        scoreboardService.finishMatch(homeTeam, awayTeam);
    }

    @GetMapping("/summary")
    public List<Match> getSummary() {
        return scoreboardService.getSummary();
    }
}