package com.sergiomartinrubio.backend.service;

import com.sergiomartinrubio.backend.model.Choice;
import com.sergiomartinrubio.backend.model.GamesSummary;
import com.sergiomartinrubio.backend.model.Result;
import com.sergiomartinrubio.backend.model.ResultSummary;
import com.sergiomartinrubio.backend.util.RandomChoiceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.sergiomartinrubio.backend.model.Choice.*;
import static com.sergiomartinrubio.backend.model.Result.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private static final UUID GAME_ID = UUID.randomUUID();
    private static final UUID ROUND_ID = UUID.randomUUID();

    @Captor
    private ArgumentCaptor<ResultSummary> captor;

    @Mock
    private RandomChoiceUtils randomChoiceUtils;

    @Mock
    private RoundsSummaryService roundsSummaryService;

    @InjectMocks
    private GameService gameService;

    @Test
    void givenPlayerOneIsPaperWhenPlayThenPlayerOneWinsAndSaveResults() {
        // GIVEN
        ResultSummary resultSummary = buildResultSummary(PAPER, PLAYER_1_WINS);
        when(randomChoiceUtils.getRandomChoice()).thenReturn(PAPER);
        when(roundsSummaryService.getResults(GAME_ID)).thenReturn(List.of(resultSummary));

        // WHEN
        gameService.play(GAME_ID);

        // THEN
        List<ResultSummary> roundsSummary = gameService.getRoundsSummary(GAME_ID);
        assertThat(roundsSummary).hasSize(1);
        assertThat(roundsSummary.get(0).getPlayerOneChoice()).isEqualTo(PAPER);
        assertThat(roundsSummary.get(0).getPlayerTwoChoice()).isEqualTo(ROCK);
        assertThat(roundsSummary.get(0).getResult()).isEqualTo(PLAYER_1_WINS);
        verify(roundsSummaryService).saveResult(eq(GAME_ID), captor.capture());
        assertThat(captor.getValue().getResult()).isEqualTo(PLAYER_1_WINS);
        assertThat(captor.getValue().getPlayerOneChoice()).isEqualTo(PAPER);
        assertThat(captor.getValue().getPlayerTwoChoice()).isEqualTo(ROCK);
    }

    @Test
    void givenPlayerOneIsScissorsWhenPlayThenPlayerTwoWinsAndSaveResults() {
        // GIVEN
        ResultSummary resultSummary = buildResultSummary(SCISSORS, PLAYER_2_WINS);
        when(randomChoiceUtils.getRandomChoice()).thenReturn(SCISSORS);
        when(roundsSummaryService.getResults(GAME_ID)).thenReturn(List.of(resultSummary));

        // WHEN
        gameService.play(GAME_ID);

        // THEN
        List<ResultSummary> roundsSummary = gameService.getRoundsSummary(GAME_ID);
        assertThat(roundsSummary).hasSize(1);
        assertThat(roundsSummary.get(0).getPlayerOneChoice()).isEqualTo(SCISSORS);
        assertThat(roundsSummary.get(0).getPlayerTwoChoice()).isEqualTo(ROCK);
        assertThat(roundsSummary.get(0).getResult()).isEqualTo(PLAYER_2_WINS);
        verify(roundsSummaryService).saveResult(eq(GAME_ID), captor.capture());
        assertThat(captor.getValue().getResult()).isEqualTo(PLAYER_2_WINS);
        assertThat(captor.getValue().getPlayerOneChoice()).isEqualTo(SCISSORS);
        assertThat(captor.getValue().getPlayerTwoChoice()).isEqualTo(ROCK);
    }

    @Test
    void givenPlayerOneIsRockWhenPlayThenDrawAndSaveResults() {
        // GIVEN
        ResultSummary resultSummary = buildResultSummary(ROCK, DRAW);
        when(randomChoiceUtils.getRandomChoice()).thenReturn(ROCK);
        when(roundsSummaryService.getResults(GAME_ID)).thenReturn(List.of(resultSummary));

        // WHEN
        gameService.play(GAME_ID);

        // THEN
        List<ResultSummary> roundsSummary = gameService.getRoundsSummary(GAME_ID);
        assertThat(roundsSummary).hasSize(1);
        assertThat(roundsSummary.get(0).getPlayerOneChoice()).isEqualTo(ROCK);
        assertThat(roundsSummary.get(0).getPlayerTwoChoice()).isEqualTo(ROCK);
        assertThat(roundsSummary.get(0).getResult()).isEqualTo(DRAW);
        verify(roundsSummaryService).saveResult(eq(GAME_ID), captor.capture());
        assertThat(captor.getValue().getResult()).isEqualTo(DRAW);
        assertThat(captor.getValue().getPlayerOneChoice()).isEqualTo(ROCK);
        assertThat(captor.getValue().getPlayerTwoChoice()).isEqualTo(ROCK);
    }

    @Test
    void givenGameIdWhenRemoveResultsThenRemoveResultsIsCalled() {
        // WHEN
        gameService.removeResults(GAME_ID);

        // THEN
        verify(roundsSummaryService).removeResults(GAME_ID);
    }

    @Test
    void givenResultSummariesWhenGetGamesSummaryThenReturnCorrectValues() {
        // GIVEN
        ResultSummary firstResultSummary = buildResultSummary(ROCK, DRAW);
        ResultSummary secondResultSummary = buildResultSummary(SCISSORS, PLAYER_2_WINS);
        ResultSummary thirdResultSummary = buildResultSummary(ROCK, DRAW);
        ResultSummary fourthResultSummary = buildResultSummary(PAPER, PLAYER_1_WINS);
        when(roundsSummaryService.getAllResultSummaries())
                .thenReturn(List.of(firstResultSummary, secondResultSummary, thirdResultSummary, fourthResultSummary));

        // WHEN
        GamesSummary gamesSummary = gameService.getGamesSummary();

        // THEN
        assertThat(gamesSummary.getTotalRoundsPlayed()).isEqualTo(4);
        assertThat(gamesSummary.getTotalWinsFirstPlayers()).isEqualTo(1);
        assertThat(gamesSummary.getTotalWinsSecondPlayers()).isEqualTo(1);
        assertThat(gamesSummary.getTotalDraws()).isEqualTo(2);
    }

    private ResultSummary buildResultSummary(Choice choice, Result result) {
        return new ResultSummary(ROUND_ID, choice, ROCK, result, false);
    }

}
