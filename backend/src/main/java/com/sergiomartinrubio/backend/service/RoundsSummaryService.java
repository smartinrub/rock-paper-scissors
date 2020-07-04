package com.sergiomartinrubio.backend.service;

import com.sergiomartinrubio.backend.model.ResultSummary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoundsSummaryService {

    private static final Map<UUID, List<ResultSummary>> RESULT_SUMMARIES_BY_GAME_ID = new HashMap<>();

    public void saveResult(UUID gameId, ResultSummary resultSummary) {
        if (RESULT_SUMMARIES_BY_GAME_ID.get(gameId) == null) {
            List<ResultSummary> resultSummaries = new ArrayList<>();
            resultSummaries.add(resultSummary);
            RESULT_SUMMARIES_BY_GAME_ID.put(gameId, resultSummaries);
        } else {
            RESULT_SUMMARIES_BY_GAME_ID.get(gameId).add(resultSummary);
        }
    }

    public List<ResultSummary> getResults(UUID gameId) {
        if (RESULT_SUMMARIES_BY_GAME_ID.get(gameId) != null) {
            return RESULT_SUMMARIES_BY_GAME_ID.get(gameId).stream()
                    .filter(resultSummary -> !resultSummary.getCleared())
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public void removeResults(UUID gameId) {
        RESULT_SUMMARIES_BY_GAME_ID.get(gameId)
                .forEach(resultSummary -> resultSummary.setCleared(true));
    }

    public List<ResultSummary> getAllResultSummaries() {
        return RESULT_SUMMARIES_BY_GAME_ID.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
