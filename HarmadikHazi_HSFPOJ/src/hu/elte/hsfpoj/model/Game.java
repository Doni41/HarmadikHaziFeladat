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

    /**
     * creates a new ResultManager object with sql connection, and a HashMap for levels than reads the levels from
     * the levels.txt textfile
     */
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

    /**
     * checks is a level already loaded
     * @return
     */
    public boolean isLevelAlreadyLoaded () {
        return level != null;
    }

    /**
     * checks if a player can move to the diretion
     * @param d is a direction parameter
     * @return true if the player can move to the d direction
     */
    public boolean step (Direction d) {
        return level.movePlayer(d);
    }

    /**
     * loads a new game
     * @param gameID is a GameIdentifier
     */
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

    public ResultManager getResultManager() {
        return resultManager;
    }

    /**
     * creates a random number between 1 and 4 and
     * @return a new Direcion object
     */
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

    /**
     * reads the next line of the levels.txt file
     * @param sc
     * @return
     */
    public String readNextLine(Scanner sc) {
        String line = "";
        while(sc.hasNextLine() && line.trim().isEmpty()) {
            line = sc.nextLine();
        }
        return line;
    }

    /**
     * reads a new GameIdentifier from levels.txt
     * @param line
     * @return a new GameIdentifier object
     */
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

    /**
     * adds game levels to the HashMap
     * @param level
     */
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

    /**
     * reads a new level from levels.txt file
     */
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

    /**
     * checks that an Item is in the 3 radius area of the player
     * @param p is a position
     * @return true if the item is in the 3 radius area
     */
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
