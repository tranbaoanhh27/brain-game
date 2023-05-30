package com.basoft.braingame;

import java.util.Locale;
import java.util.Random;

public class NumberGuessingGame {
    private NumberGuessingGameObserver observer;
    private int score, currentMax, remainingChanges;
    private static final Random random = new Random();

    public NumberGuessingGame(NumberGuessingGameObserver observer) {
        this.observer = observer;
        currentMax = 10;
        remainingChanges = 10;
        score = 0;
    }

    public void start() {
        observer.setMagicianImage(R.drawable.magician_question);
        observer.say(String.format(Locale.getDefault(), "I am thinking of a number from 0 to %d, you will become the winner if you can find out what that number is. You have %d chances left!", currentMax, remainingChanges));
    }
}
