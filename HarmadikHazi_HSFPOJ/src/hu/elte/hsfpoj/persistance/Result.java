package hu.elte.hsfpoj.persistance;

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
