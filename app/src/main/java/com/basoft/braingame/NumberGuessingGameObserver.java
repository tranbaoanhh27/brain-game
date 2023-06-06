package com.basoft.braingame;

public interface NumberGuessingGameObserver {
    public void say(String speech);

    void setMagicianImage(int resourceId);

    void giveQuestion(String question);

    void getUserGuessingNumber(String speech);

    void giveCongratulation(String speech);

    void gameOver(String speech);

    void updateScore(long score);
}
