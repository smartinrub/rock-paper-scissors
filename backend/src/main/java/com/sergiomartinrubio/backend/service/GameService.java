package com.sergiomartinrubio.backend.service;

import com.sergiomartinrubio.backend.model.Choice;
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
    private final GameSummaryService gameSummaryService;

    public void play(UUID gameId) {
        Choice playerOneChoice = randomChoiceUtils.getRandomChoice();
        saveResult(gameId, playerOneChoice);
    }

    public void removeResults(UUID gameId) {
        gameSummaryService.removeResults(gameId);
    }

    private void saveResult(UUID gameId, Choice playerOneChoice) {
        switch (playerOneChoice) {
            case ROCK:
                gameSummaryService.saveResult(gameId, buildResultSummary(ROCK, DRAW));
                break;
            case PAPER:
                gameSummaryService.saveResult(gameId, buildResultSummary(PAPER, PLAYER_1_WINS));
                break;
            case SCISSORS:
                gameSummaryService.saveResult(gameId, buildResultSummary(SCISSORS, PLAYER_2_WINS));
                break;
        }
    }

    public List<ResultSummary> getRoundsSummary(UUID gameId) {
        return gameSummaryService.getResults(gameId);
    }

    private ResultSummary buildResultSummary(Choice choice, Result result) {
        return new ResultSummary(UUID.randomUUID(), choice, ROCK, result, false);
    }
}
