package hu.elte.hsfpoj.model;

import java.util.Objects;

public class GameIdentifier {
    private final String difficulty;
    private final int level;

    public GameIdentifier(String difficulty, int level) {
        this.difficulty = difficulty;
        this.level = level;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameIdentifier that = (GameIdentifier) o;
        return level == that.level &&
                Objects.equals(difficulty, that.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(difficulty, level);
    }
}
