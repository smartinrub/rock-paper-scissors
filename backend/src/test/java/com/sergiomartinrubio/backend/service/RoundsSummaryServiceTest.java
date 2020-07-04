package com.sergiomartinrubio.backend.service;

import com.sergiomartinrubio.backend.model.Choice;
import com.sergiomartinrubio.backend.model.Result;
import com.sergiomartinrubio.backend.model.ResultSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.sergiomartinrubio.backend.model.Choice.*;
import static com.sergiomartinrubio.backend.model.Result.*;
import static org.assertj.core.api.Assertions.assertThat;

class RoundsSummaryServiceTest {

    private static final UUID GAME_ID_1 = UUID.randomUUID();
    private static final UUID GAME_ID_2 = UUID.randomUUID();
    private static final UUID ROUND_ID_1 = UUID.randomUUID();
    private static final UUID ROUND_ID_2 = UUID.randomUUID();

    private RoundsSummaryService roundsSummaryService;

    @BeforeEach
    void setup() {
        roundsSummaryService = new RoundsSummaryService();
    }

    @Test
    void givenGameIdAndResultWhenSaveResultThenResultIsSaved() {
        // GIVEN
        ResultSummary resultSummary = buildResultSummary(ROUND_ID_1, PAPER, PLAYER_1_WINS, false);

        // WHEN
        roundsSummaryService.saveResult(GAME_ID_1, resultSummary);

        // THEN
        List<ResultSummary> summariesResults = roundsSummaryService.getResults(GAME_ID_1);
        assertThat(summariesResults).containsExactly(resultSummary);
    }

    @Test
    void givenThreeResultsWithAGameIdThatContainsTwoResultsWhenGetResultsThenReturnTwoResults() {
        // GIVEN
        ResultSummary firstResultSummary = buildResultSummary(ROUND_ID_1, PAPER, PLAYER_1_WINS, false);
        ResultSummary secondResultSummary = buildResultSummary(ROUND_ID_1, ROCK, DRAW, false);
        ResultSummary thirdResultSummary = buildResultSummary(ROUND_ID_2, SCISSORS, PLAYER_2_WINS, false);
        roundsSummaryService.saveResult(GAME_ID_1, firstResultSummary);
        roundsSummaryService.saveResult(GAME_ID_2, secondResultSummary);
        roundsSummaryService.saveResult(GAME_ID_1, thirdResultSummary);

        // WHEN
        List<ResultSummary> summariesResults = roundsSummaryService.getResults(GAME_ID_1);

        // THEN
        assertThat(summariesResults).hasSize(2);
        assertThat(summariesResults).containsExactly(firstResultSummary, thirdResultSummary);
    }

    @Test
    void givenResultsWhenRemoveResultsThenReturnZeroResultsForGivenGameId() {
        // GIVEN
        ResultSummary firstResultSummary = buildResultSummary(ROUND_ID_1, PAPER, PLAYER_1_WINS, false);
        ResultSummary secondResultSummary = buildResultSummary(ROUND_ID_1, ROCK, DRAW, false);
        ResultSummary thirdResultSummary = buildResultSummary(ROUND_ID_2, SCISSORS, PLAYER_2_WINS, false);
        roundsSummaryService.saveResult(GAME_ID_1, firstResultSummary);
        roundsSummaryService.saveResult(GAME_ID_2, secondResultSummary);
        roundsSummaryService.saveResult(GAME_ID_1, thirdResultSummary);

        // WHEN
        roundsSummaryService.removeResults(GAME_ID_1);

        // THEN
        List<ResultSummary> firstSummariesResults = roundsSummaryService.getResults(GAME_ID_1);
        List<ResultSummary> secondSummariesResults = roundsSummaryService.getResults(GAME_ID_2);
        assertThat(firstSummariesResults).isEmpty();
        assertThat(secondSummariesResults).hasSize(1);
        assertThat(secondSummariesResults).containsExactly(secondResultSummary);
    }

    @Test
    public void givenGameIdNotSavedWhenGetResultsThenReturnEmptyList() {
        // WHEN
        List<ResultSummary> resultSummaries = roundsSummaryService.getResults(UUID.randomUUID());

        // THEN
        assertThat(resultSummaries).hasSize(0);
    }

    @Test
    public void givenClearedResultSummaryWhenGetAllResultSummariesThenReturnAllResultSummaries() {
        ResultSummary firstResultSummary = buildResultSummary(ROUND_ID_1, PAPER, PLAYER_1_WINS, false);
        ResultSummary secondResultSummary = buildResultSummary(ROUND_ID_1, ROCK, DRAW, true);
        ResultSummary thirdResultSummary = buildResultSummary(ROUND_ID_2, SCISSORS, PLAYER_2_WINS, false);
        roundsSummaryService.saveResult(GAME_ID_1, firstResultSummary);
        roundsSummaryService.saveResult(GAME_ID_2, secondResultSummary);
        roundsSummaryService.saveResult(GAME_ID_1, thirdResultSummary);

        // WHEN
        List<ResultSummary> allResultSummaries = roundsSummaryService.getAllResultSummaries();

        // THEN
        assertThat(allResultSummaries).contains(firstResultSummary, secondResultSummary, thirdResultSummary);
    }

    private ResultSummary buildResultSummary(UUID roundId, Choice choice, Result result, Boolean cleared) {
        return new ResultSummary(roundId, choice, ROCK, result, cleared);
    }

}
