package hu.elte.hsfpoj.model;

import java.util.ArrayList;
import java.util.Random;

public class Level {
    private boolean fulfilled;
    private boolean gameOver;
    private int levelNumber;
    private final int rows;
    private final int columns;
    private GameIdentifier gameID;
    private final Item[][] level;
    private Position player;
    private Position ghost;
    private Direction ghostDirection;

    /**
     * creates a new Level object
     * @param levelRows
     * @param gameID
     * @param ghostDirection
     */
    public Level(ArrayList<String> levelRows, GameIdentifier gameID, Direction ghostDirection) {
        this.gameID = gameID;
        fulfilled = false;
        gameOver = false;
        levelNumber = gameID.getLevel();
        int c = 0;
        // reading columns number from the file
        for (String s : levelRows) {
            if (s.length() > c) {
                c = s.length();
            }
        }
        rows = levelRows.size();
        columns = c;
        level = new Item[rows][columns];
        player = new Position(0, 0);
        ghost = new Position(rows, columns);

        for (int i = 0; i < rows; ++i) {
            String s = levelRows.get(i);
            for (int j = 0; j < s.length(); ++j) {
                switch (s.charAt(j)) {
                    case '@':   player = new Position(j, i);
                                level[i][j] = Item.PLAYER;
                                break;
                    case '#':   level[i][j] = Item.WALL;
                                break;
                    case '.':   level[i][j] = Item.DESTINATION;
                                break;
                    case '*':   ghost = new Position(j, i);
                                level[i][j] = Item.GHOST;
                                break;
                    case ' ':   level[i][j] = Item.EMPTY;
                                break;
                    default:    level[i][j] = Item.EMPTY;
                                break;
                }
            }
            for (int j = s.length(); j < columns; ++j) {
                level[i][j] = Item.EMPTY;
            }
        }
        this.ghostDirection = ghostDirection;
    }

    public Level(Level lvl, Direction direction) {
        this.gameID = lvl.getGameID();
        this.fulfilled = lvl.isFulfilled();
        this.levelNumber = lvl.getLevelNumber();
        this.rows = lvl.getRows();
        this.columns = lvl.getColumns();
        this.level = new Item[rows][columns];
        this.player = new Position(lvl.getPlayerPosition().x, lvl.getPlayerPosition().y);
        this.ghost = new Position(lvl.getGhostPosition().x, lvl.getGhostPosition().y);
        this.ghostDirection = direction;
        for (int i = 0; i < rows; ++i) {
            System.arraycopy(lvl.level[i], 0, level[i], 0, columns);
        }
    }

    /**
     * checks if a position is int the game area
     * @param p a position
     * @return
     */
    public boolean isValidPosition(Position p) {
        return p.x >= 0 && p.y >= 0 && p.x < columns && p.y < rows;
    }

    /**
     * checks the next position of the player is EMPTY or the DESTINATION
     * @param p is the next position
     * @return
     */
    public boolean isFreeToMove (Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        Item item = level[p.y][p.x];

        return item == Item.EMPTY || item == Item.DESTINATION;
    }

    /**
     * checks the next position of the ghost is EMPTY
     * @param p is the next position
     * @return
     */
    public boolean ifFreeForGhost (Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        Item item = level[p.y][p.x];

        return item == Item.EMPTY;
    }

    /**
     * move the player to the next position
     * @param d is the next direction
     * @return
     */
    public boolean movePlayer (Direction d) {
        Position current = player;
        Position next = current.translate(d);
        if (!isFulfilled() && isFreeToMove(next)) {
            player = next;
            level[current.y][current.x] = Item.EMPTY;
            level[next.y][next.x] = Item.PLAYER;
            return true;
        }
        return false;
    }

    /**
     * checks the player's and ghost's position
     * @return
     */
    public boolean endOfTheGame () {

        Position playerLeft = new Position(player.x - 1, player.y);
        Position playerRight = new Position(player.x + 1, player.y);
        Position playerDown = new Position(player.x, player.y + 1);
        Position playerUp = new Position(player.x, player.y - 1);
        Position ghostCurrent = ghost;

        if (ghostCurrent.equals(playerLeft)
                || ghostCurrent.equals(playerRight)
                || ghostCurrent.equals(playerDown)
                || ghostCurrent.equals(playerUp)) {
            gameOver = true;
            return true;

        }

        return false;
    }

    /**
     * create a new direction to the ghost if it's necessary
     * @param d is the old direction
     */
    public void ghostChangeDirection (Direction d) {
        Position current = ghost;
        Position next = current.translate(d);
        if (isValidPosition(next) && ifFreeForGhost(next)) {
            ghost = next;
            level[current.y][current.x] = Item.EMPTY;
            level[next.y][next.x] = Item.GHOST;
            ghostDirection = d;
        } else {
            int min = 1;
            int max = 4;
            Random random = new Random();
            int rnd = -1;

            Direction direction = d;
            while (!(isValidPosition(current.translate(direction)) && ifFreeForGhost(current.translate(direction)))) {
                rnd = random.nextInt(max - min + 1) + min;
                if (rnd == 1) {
                    direction = Direction.LEFT;
                } else if (rnd == 2) {
                    direction = Direction.UP;
                } else if (rnd == 3) {
                    direction = Direction.RIGHT;
                } else if (rnd == 4) {
                    direction = Direction.DOWN;
                }
            }

            ghost = current.translate(direction);
            level[current.y][current.x] = Item.EMPTY;
            level[ghost.y][ghost.x] = Item.GHOST;
            ghostDirection = direction;
        }
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public GameIdentifier getGameID() {
        return gameID;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Item getLevelItem (int row, int column) {
        return level[row][column];
    }

    public Position getPlayerPosition() {
        return player;
    }

    public Position getGhostPosition() {
        return ghost;
    }

    public Direction getGhostDirection() {
        if (ghostDirection == null) {
            return null;
        }
        return ghostDirection;
    }

    public boolean isFulfilled () {
        return player.x == columns - 1 && player.y == 0;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
