package com.sergiomartinrubio.backend.controller;

import com.sergiomartinrubio.backend.model.GamesSummary;
import com.sergiomartinrubio.backend.model.ResultSummary;
import com.sergiomartinrubio.backend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final HttpSession httpSession;

    @ResponseStatus(CREATED)
    @PostMapping("/play")
    public void play() {
        if (httpSession.getAttribute("gameId") == null) {
            httpSession.setAttribute("gameId", UUID.randomUUID());
        }

        gameService.play((UUID) httpSession.getAttribute("gameId"));
    }

    @GetMapping("/rounds-summary")
    public ResponseEntity<List<ResultSummary>> getGameSummary() {
        if (httpSession.getAttribute("gameId") == null) {
            httpSession.setAttribute("gameId", UUID.randomUUID());
        }

        return ResponseEntity.ok(gameService.getRoundsSummary((UUID) httpSession.getAttribute("gameId")));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/restart")
    public void removeResults() {
        UUID gameId = (UUID) httpSession.getAttribute("gameId");
        if (gameId != null) {
            gameService.removeResults(gameId);
        }
    }

    @GetMapping("/summary")
    public GamesSummary getGamesSummary() {
        return gameService.getGamesSummary();
    }

}
