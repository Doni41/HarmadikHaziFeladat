package hu.elte.hsfpoj.model;

public class Result {

    private String name;
    private int highScore;

    public Result(String name, int highScore) {
        this.name = name;
        this.highScore = highScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public String getName() {
        return name;
    }
}
