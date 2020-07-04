package com.sergiomartinrubio.backend.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GamesSummary {

    private final Long totalRoundsPlayed;
    private final Long totalWinsFirstPlayers;
    private final Long totalWinsSecondPlayers;
    private final Long totalDraws;

}
