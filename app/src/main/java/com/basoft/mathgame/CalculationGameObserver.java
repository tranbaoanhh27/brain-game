package com.basoft.mathgame;

public interface CalculationGameObserver {
    void updateHighestScore(long score);

    public enum AnswerStatus {
      TIMES_UP,
      WRONG,
      CORRECT
    };
    void updatePlayerScore(long score);
    void updatePlayerLife(int life);
    void updateRemainingTime(long millisUntilEnd);
    void updateExpression(String expression);
    void updateCorrectAnswer(long answer, AnswerStatus status);
    String getCurrentUserAnswer();
    void freezeInput();
    void unfreezeInput();
}
