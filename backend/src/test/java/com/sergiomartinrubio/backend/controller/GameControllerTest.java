package com.sergiomartinrubio.backend.controller;

import com.sergiomartinrubio.backend.model.Choice;
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

    private ResultSummary buildResultSummary(UUID roundId, Choice choice, Result result) {
        return new ResultSummary(roundId, choice, ROCK, result, false);
    }

}