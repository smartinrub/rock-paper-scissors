package com.sergiomartinrubio.backend;

import com.sergiomartinrubio.backend.model.GamesSummary;
import com.sergiomartinrubio.backend.model.Result;
import com.sergiomartinrubio.backend.model.ResultSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetAllGamesSummaryIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void givenTwoSeparateGamesWhenGetAllSummaryGamesThenReturnValuesOfThreeSummaries() {
        // GIVEN
        ResponseEntity<String> firstResponse = restTemplate
                .exchange("http://localhost:" + port + "/play", HttpMethod.POST, null, String.class);
        HttpHeaders firstRequestHeaders = new HttpHeaders();
        firstRequestHeaders.add("Cookie", firstResponse.getHeaders().get("Set-Cookie").get(0));
        HttpEntity firstRequestEntity = new HttpEntity(null, firstRequestHeaders);

        ResponseEntity<String> secondResponse = restTemplate
                .exchange("http://localhost:" + port + "/play", HttpMethod.POST, null, String.class);
        HttpHeaders secondRequestHeaders = new HttpHeaders();
        secondRequestHeaders.add("Cookie", secondResponse.getHeaders().get("Set-Cookie").get(0));
        HttpEntity secondRequestEntity = new HttpEntity(null, secondRequestHeaders);


        restTemplate.exchange("http://localhost:" + port + "/play", HttpMethod.POST, firstRequestEntity, String.class);

        // WHEN
        GamesSummary gamesSummary = restTemplate.exchange("http://localhost:" + port + "/summary",
                HttpMethod.GET,
                null,
                GamesSummary.class)
                .getBody();

        // THEN
        List<ResultSummary> firstResultSummaries = restTemplate
                .exchange("http://localhost:" + port + "/rounds-summary",
                        HttpMethod.GET, firstRequestEntity,
                        new ParameterizedTypeReference<List<ResultSummary>>() {
                        }).getBody();

        List<ResultSummary> secondResultSummaries = restTemplate
                .exchange("http://localhost:" + port + "/rounds-summary",
                        HttpMethod.GET, secondRequestEntity,
                        new ParameterizedTypeReference<List<ResultSummary>>() {
                        }).getBody();

        List<ResultSummary> resultSummaries = new ArrayList<>();
        resultSummaries.addAll(firstResultSummaries);
        resultSummaries.addAll(secondResultSummaries);
        long drawCount = resultSummaries.stream().filter(resultSummary -> resultSummary.getResult() == Result.DRAW).count();
        long playerOneCount = resultSummaries.stream().filter(resultSummary -> resultSummary.getResult() == Result.PLAYER_1_WINS).count();
        long playerTwoCount = resultSummaries.stream().filter(resultSummary -> resultSummary.getResult() == Result.PLAYER_2_WINS).count();

        assertThat(gamesSummary.getTotalRoundsPlayed()).isEqualTo(3);
        assertThat(gamesSummary.getTotalDraws()).isEqualTo(drawCount);
        assertThat(gamesSummary.getTotalWinsFirstPlayers()).isEqualTo(playerOneCount);
        assertThat(gamesSummary.getTotalWinsSecondPlayers()).isEqualTo(playerTwoCount);
    }

}
