package hu.elte.hsfpoj.model;

import hu.elte.hsfpoj.persistance.ResultManager;
import hu.elte.hsfpoj.res.ResLoader;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class Game {
    private final HashMap<String, HashMap<Integer, Level>> levels;
    private Level level;
    private ResultManager resultManager;

    public Game() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("" + e);
        }
        try {
            resultManager = new ResultManager(10);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        levels = new HashMap<>();
        level = null;
        readLevels();
    }

    public boolean isLevelAlreadyLoaded () {
        return level != null;
    }

    public boolean step (Direction d) {
        return level.movePlayer(d);
    }

    public void loadNewGame(GameIdentifier gameID) {
        Direction direction = createDirection();
        level = null;
        level = new Level(levels.get(gameID.getDifficulty()).get(gameID.getLevel()), direction);
    }

    public HashMap<String, HashMap<Integer, Level>> getLevels() {
        return levels;
    }

    public Level getLevel() {
        return level;
    }

    public Collection<String> getDifficulties() {
        return levels.keySet();
    }

    public  Collection<Integer> getLevelsOfDifficulty(String difficulty) {
        if (!levels.containsKey(difficulty)) {
            return null;
        }
        return levels.get(difficulty).keySet();
    }

    public ResultManager getResultManager() {
        return resultManager;
    }

    public Direction createDirection() {
        int min = 1;
        int max = 4;
        Direction direction = null;
        Random random = new Random();
        int rnd = random.nextInt(max - min + 1) + min;
        if (rnd == 1) {
            direction = Direction.LEFT;
        } else if (rnd == 2) {
            direction = Direction.UP;
        } else if (rnd == 3) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.DOWN;
        }

        return direction;
    }

    public String readNextLine(Scanner sc) {
        String line = "";
        while(sc.hasNextLine() && line.trim().isEmpty()) {
            line = sc.nextLine();
        }
        return line;
    }

    public GameIdentifier readGameID(String line) {
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != ';') {
            return null;
        }

        Scanner sc = new Scanner(line);
        sc.next();

        if (!sc.hasNext()) {
            return null;
        }
        String difficulty = sc.next().toUpperCase();

        if (!sc.hasNextInt()) {
            return null;
        }
        int id = sc.nextInt();
        return new GameIdentifier(difficulty, id);
    }

    public void addNewGameLevel (Level level) {
        HashMap<Integer, Level> levelOfDiff;
        if (levels.containsKey(level.getGameID().getDifficulty())) {
            levelOfDiff = levels.get(level.getGameID().getDifficulty());
            levelOfDiff.put(level.getGameID().getLevel(), level);
        } else {
            levelOfDiff = new HashMap<>();
            levelOfDiff.put(level.getGameID().getLevel(), level);
            levels.put(level.getGameID().getDifficulty(), levelOfDiff);
        }
    }

    public void readLevels() {
        InputStream is;
        is = ResLoader.loadResource("/hu/elte/hsfpoj/res/levels.txt");

        try(Scanner sc = new Scanner(is)) {
            String line = readNextLine(sc);
            ArrayList<String> levelRows = new ArrayList<>();

            while(!line.isEmpty()) {
                GameIdentifier id = readGameID(line);
                if (id == null) {
                    return;
                }

                levelRows.clear();
                line = readNextLine(sc);
                while(!line.isEmpty() && line.trim().charAt(0) != ';') {
                    levelRows.add(line);
                    line = readNextLine(sc);
                }

                Direction direction = createDirection();
                addNewGameLevel(new Level(levelRows, id, direction));
            }
        } catch (Exception e) {
            System.out.println("Hiba a beolvasas kozben!");
        }
    }

    public boolean isNearPlayer (Position p) {
        Position playerCurrent = level.getPlayerPosition();
        if (
                (p.x > playerCurrent.x
                && (p.x - playerCurrent.x) <= 3
                && p.y > playerCurrent.y
                && (p.y - playerCurrent.y) <=3)
            || (p.x < playerCurrent.x
                && (playerCurrent.x - p.x) <= 3
                && p.y > playerCurrent.y
                && (p.y - playerCurrent.y) <=3)
            || (p.x < playerCurrent.x
                && (playerCurrent.x - p.x) <= 3
                && p.y < playerCurrent.y
                && (playerCurrent.y - p.y) <=3)
            || (p.x > playerCurrent.x
                && (p.x - playerCurrent.x) <= 3
                && p.y < playerCurrent.y
                && (playerCurrent.y - p.y) <=3)
            || (p.x == playerCurrent.x
                && p.y < playerCurrent.y
                && (playerCurrent.y - p.y) <=3)
            || (p.x == playerCurrent.x
                && p.y > playerCurrent.y
                && (p.y - playerCurrent.y) <=3)
           || (p.x > playerCurrent.x
                && (p.x - playerCurrent.x) <= 3
                && p.y == playerCurrent.y)
            || (p.x < playerCurrent.x
                && (playerCurrent.x - p.x) <= 3
                && p.y == playerCurrent.y)
            || (p.x == playerCurrent.x
                && p.y == playerCurrent.y)
        ) {
            return true;
        }

        return false;
    }

}
