package com.sergiomartinrubio.backend.util;

import com.sergiomartinrubio.backend.model.Choice;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RandomChoiceUtilsTest {

    @Test
    void whenGetRandomChoiceThenChoiceIsReturned() {
        // GIVEN
        RandomChoiceUtils randomChoiceUtils = new RandomChoiceUtils();

        // WHEN
        Choice randomChoice = randomChoiceUtils.getRandomChoice();

        // THEN
        assertThat(randomChoice).isInstanceOf(Choice.class);
    }

}