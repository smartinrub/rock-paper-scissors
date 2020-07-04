package com.sergiomartinrubio.backend.util;

import com.sergiomartinrubio.backend.model.Choice;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomChoiceUtils {

    public Choice getRandomChoice() {
        Random random = new Random();
        int randomChoiceNumber = random.nextInt(Choice.values().length);
        return Choice.values()[randomChoiceNumber];
    }
}
