package com.sergiomartinrubio.backend.controller;

import com.sergiomartinrubio.backend.model.Choice;
import com.sergiomartinrubio.backend.model.GamesSummary;
import com.sergiomartinrubio.backend.model.Result;
import com.sergiomartinrubio.backend.model.ResultSummary;
import com.sergiomartinrubio.backend.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.sergiomartinrubio.backend.model.Choice.*;
import static com.sergiomartinrubio.backend.model.Result.PLAYER_1_WINS;
import static com.sergiomartinrubio.backend.model.Result.PLAYER_2_WINS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    private static final UUID GAME_ID = UUID.randomUUID();
    private static final UUID ROUND_ID_1 = UUID.randomUUID();
    private static final UUID ROUND_ID_2 = UUID.randomUUID();

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenRequestWhenPlayEndpointThenCreatedResponseStatus() throws Exception {
        // WHEN
        // THEN
        mockMvc.perform(post("/play")
                .sessionAttr("gameId", GAME_ID))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void givenGameIdWhenGetRoundSummaryThenReturnRoundsSummary() throws Exception {
        // WHEN
        when(gameService.getRoundsSummary(GAME_ID))
                .thenReturn(List.of(
                        buildResultSummary(ROUND_ID_1, SCISSORS, PLAYER_2_WINS),
                        buildResultSummary(ROUND_ID_2, PAPER, PLAYER_1_WINS))
                );

        // THEN
        mockMvc.perform(get("/rounds-summary")
                .sessionAttr("gameId", GAME_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].roundId").value(ROUND_ID_1.toString()))
                .andExpect(jsonPath("$.[0].playerOneChoice").value(SCISSORS.toString()))
                .andExpect(jsonPath("$.[0].playerTwoChoice").value(ROCK.toString()))
                .andExpect(jsonPath("$.[0].result").value(PLAYER_2_WINS.toString()))
                .andExpect(jsonPath("$.[1].roundId").value(ROUND_ID_2.toString()))
                .andExpect(jsonPath("$.[1].playerOneChoice").value(PAPER.toString()))
                .andExpect(jsonPath("$.[1].playerTwoChoice").value(ROCK.toString()))
                .andExpect(jsonPath("$.[1].result").value(PLAYER_1_WINS.toString()));
    }

    @Test
    void givenGameIdWhenRequestRestartEndpointThenRemoveResultsIsCalled() throws Exception {
        // WHEN
        mockMvc.perform(delete("/restart")
                .sessionAttr("gameId", GAME_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        // THEN
        verify(gameService).removeResults(GAME_ID);
    }

    @Test
    void givenGamesSummaryWhenGetGamesSummaryThenReturnGamesSummary() throws Exception {
        // GIVEN
        GamesSummary gamesSummary = GamesSummary.builder()
                .totalRoundsPlayed(10L)
                .totalWinsFirstPlayers(5L)
                .totalWinsSecondPlayers(2L)
                .totalDraws(3L)
                .build();
        when(gameService.getAllGamesSummary()).thenReturn(gamesSummary);

        // WHEN
        // THEN
        mockMvc.perform(get("/summary"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRoundsPlayed").value(10))
                .andExpect(jsonPath("$.totalWinsFirstPlayers").value(5))
                .andExpect(jsonPath("$.totalWinsSecondPlayers").value(2))
                .andExpect(jsonPath("$.totalDraws").value(3));
    }

    private ResultSummary buildResultSummary(UUID roundId, Choice choice, Result result) {
        return new ResultSummary(roundId, choice, ROCK, result, false);
    }

}