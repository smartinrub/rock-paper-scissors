package com.sergiomartinrubio.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ResultSummary {
    private UUID roundId;
    private Choice playerOneChoice;
    private Choice playerTwoChoice;
    private Result result;
    private Boolean cleared;
}
