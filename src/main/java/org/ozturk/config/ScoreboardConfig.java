package org.ozturk.config;

import org.ozturk.service.ScoreboardService;
import org.ozturk.service.impl.ScoreboardServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScoreboardConfig {

    @Bean
    public ScoreboardService scoreboardService() {
        return new ScoreboardServiceImpl();  // This class comes from the JAR
    }
}
