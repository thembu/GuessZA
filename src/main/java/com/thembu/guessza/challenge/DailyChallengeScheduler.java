package com.thembu.guessza.challenge;

import com.thembu.guessza.challenge.dto.ChallengeResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyChallengeScheduler {

    private final  ChallengeService challengeService;

    public DailyChallengeScheduler(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Scheduled(cron = "0 10 13 * * *", zone = "Africa/Johannesburg")
    public ChallengeResponse startDailyChallenge() {
        return  challengeService.createDailyChallenge();
    }
}
