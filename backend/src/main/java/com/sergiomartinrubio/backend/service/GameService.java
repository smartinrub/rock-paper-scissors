package com.sergiomartinrubio.backend.service;

import com.sergiomartinrubio.backend.model.Choice;
import com.sergiomartinrubio.backend.model.GamesSummary;
import com.sergiomartinrubio.backend.model.Result;
import com.sergiomartinrubio.backend.model.ResultSummary;
import com.sergiomartinrubio.backend.util.RandomChoiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sergiomartinrubio.backend.model.Choice.*;
import static com.sergiomartinrubio.backend.model.Result.*;

@Service
@RequiredArgsConstructor
public class GameService {
    private final RandomChoiceUtils randomChoiceUtils;
    private final RoundsSummaryService roundsSummaryService;

    public void play(UUID gameId) {
        Choice playerOneChoice = randomChoiceUtils.getRandomChoice();
        saveResult(gameId, playerOneChoice);
    }

    public void removeResults(UUID gameId) {
        roundsSummaryService.removeResults(gameId);
    }

    private void saveResult(UUID gameId, Choice playerOneChoice) {
        switch (playerOneChoice) {
            case ROCK:
                roundsSummaryService.saveResult(gameId, buildResultSummary(ROCK, DRAW));
                break;
            case PAPER:
                roundsSummaryService.saveResult(gameId, buildResultSummary(PAPER, PLAYER_1_WINS));
                break;
            case SCISSORS:
                roundsSummaryService.saveResult(gameId, buildResultSummary(SCISSORS, PLAYER_2_WINS));
                break;
        }
    }

    public List<ResultSummary> getRoundsSummary(UUID gameId) {
        return roundsSummaryService.getResults(gameId);
    }

    private ResultSummary buildResultSummary(Choice choice, Result result) {
        return new ResultSummary(UUID.randomUUID(), choice, ROCK, result, false);
    }

    public GamesSummary getAllGamesSummary() {
        long totalWinsFirstPlayer = roundsSummaryService.getAllResultSummaries().stream()
                .filter(resultSummary -> resultSummary.getResult() == PLAYER_1_WINS)
                .count();
        long totalWinsSecondPlayer = roundsSummaryService.getAllResultSummaries().stream()
                .filter(resultSummary -> resultSummary.getResult() == PLAYER_2_WINS)
                .count();
        long totalDraws = roundsSummaryService.getAllResultSummaries().stream()
                .filter(resultSummary -> resultSummary.getResult() == DRAW)
                .count();
        return GamesSummary.builder()
                .totalRoundsPlayed((long) roundsSummaryService.getAllResultSummaries().size())
                .totalWinsFirstPlayers(totalWinsFirstPlayer)
                .totalWinsSecondPlayers(totalWinsSecondPlayer)
                .totalDraws(totalDraws)
                .build();
    }
}
