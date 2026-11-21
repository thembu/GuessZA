package com.thembu.guessza.challenge;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyChallengeScheduler {

    private final  ChallengeService challengeService;

    public DailyChallengeScheduler(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    // midnight SAST, when the new day's daily starts
    @Scheduled(cron = "0 0 0 * * *", zone = "Africa/Johannesburg")
    public void  getOrCreateDailyChallenge() {
        challengeService.createDailyChallenge();
    }
}
