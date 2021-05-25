import hu.elte.hsfpoj.model.*;
import org.junit.Assert;
import org.junit.Test;

public class LevelTest {


    Game game = new Game();

    @Test
    public void crateNewLevel () {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("EASY", 1);
        // WHEN
        game.loadNewGame(gameID);
        Position player = new Position(0, 6);
        Position ghost = new Position (7, 1);
        Position playerCurrent = game.getLevel().getPlayerPosition();
        Position ghostCurrent = game.getLevel().getGhostPosition();
        Item item = game.getLevel().getLevelItem(2, 9);

        // THEN
        Assert.assertEquals(game.getLevel().getGameID().getDifficulty(), "EASY");
        Assert.assertEquals(game.getLevel().getGameID().getLevel(), 1);
        Assert.assertFalse(game.getLevel().isFulfilled());
        Assert.assertFalse(game.getLevel().isGameOver());
        Assert.assertEquals(game.getLevel().getColumns(), 12);
        Assert.assertEquals(game.getLevel().getRows(), 7);
        Assert.assertEquals(game.getLevel().getLevelNumber(), 1);
        Assert.assertTrue(player.equals(playerCurrent));
        Assert.assertTrue(ghost.equals(ghostCurrent));
        Assert.assertEquals(item.representation, '#');
    }

    @Test
    public void loadNewLevel () {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);
        Position player = new Position(0, 11);
        Position ghost = new Position (9, 3);
        Position playerCurrent = game.getLevel().getPlayerPosition();
        Position ghostCurrent = game.getLevel().getGhostPosition();
        Item itemWall = game.getLevel().getLevelItem(4, 6);
        Item itemEmpty = game.getLevel().getLevelItem(4, 7);

        // THEN
        Assert.assertEquals(game.getLevel().getGameID().getDifficulty(), "MEDIUM");
        Assert.assertEquals(game.getLevel().getGameID().getLevel(), 6);
        Assert.assertFalse(game.getLevel().isFulfilled());
        Assert.assertFalse(game.getLevel().isGameOver());
        Assert.assertEquals(game.getLevel().getColumns(), 18);
        Assert.assertEquals(game.getLevel().getRows(), 12);
        Assert.assertEquals(game.getLevel().getLevelNumber(), 6);
        Assert.assertTrue(player.equals(playerCurrent));
        Assert.assertTrue(ghost.equals(ghostCurrent));
        Assert.assertEquals(itemWall.representation, '#');
        Assert.assertEquals(itemEmpty.representation, ' ');
    }

    @Test
    public void levelIsValidTester() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);
        boolean notValidPosition = game.getLevel().isValidPosition(new Position(-1, 22));
        boolean validPosition = game.getLevel().isValidPosition(new Position(1, 1));

        // THEN
        Assert.assertFalse(notValidPosition);
        Assert.assertTrue(validPosition);

    }

    @Test
    public void levelIsFreeToMove() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);
        Position playerCurrent = game.getLevel().getPlayerPosition();
        Position ghostCurrent = game.getLevel().getGhostPosition();
        Item itemWall = game.getLevel().getLevelItem(4, 6);
        Item itemEmpty = game.getLevel().getLevelItem(4, 7);

        boolean notFreeToMove = game.getLevel().isFreeToMove(new Position(1, 11));
        boolean freeToMove = game.getLevel().isFreeToMove(new Position(0, 10));

        // THEN
        Assert.assertFalse(notFreeToMove);
        Assert.assertTrue(freeToMove);
    }

    @Test
    public void levelMovePlayer() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);
        Position playerCurrent = game.getLevel().getPlayerPosition();
        Direction direction = Direction.UP;
        boolean move = game.getLevel().movePlayer(direction);
        boolean notMoving = game.getLevel().movePlayer(direction);

        // THEN
        Assert.assertEquals(game.getLevel().getPlayerPosition().x, playerCurrent.x);
        Assert.assertEquals(game.getLevel().getPlayerPosition().y, playerCurrent.y - 1);
        Assert.assertTrue(move);
        Assert.assertFalse(notMoving);
    }

    @Test
    public void endOfTheGameFalse () {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);

        // THEN
        Assert.assertFalse(game.getLevel().endOfTheGame());
    }

    @Test
    public void endOfTheGameTrue() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("EASY", 4);

        // WHEN
        game.loadNewGame(gameID);
        Position playerCurrent = game.getLevel().getPlayerPosition();
        Position ghostCurrent = game.getLevel().getGhostPosition();
        game.getLevel().movePlayer(Direction.UP);
        game.getLevel().movePlayer(Direction.UP);
        for (int i = 0; i <= 14; ++i) {
            game.getLevel().movePlayer(Direction.RIGHT);
        }
        game.getLevel().movePlayer(Direction.UP);
        game.getLevel().movePlayer(Direction.UP);
        game.getLevel().movePlayer(Direction.UP);

        // THEN
        Assert.assertTrue(game.getLevel().endOfTheGame());
    }

    @Test
    public void ghostChangedirection() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("EASY", 4);

        // WHEN
        game.loadNewGame(gameID);
        Position playerCurrent = game.getLevel().getPlayerPosition();
        Position ghostCurrent = game.getLevel().getGhostPosition();
        Direction direction = Direction.RIGHT;
        game.getLevel().ghostChangeDirection(direction);

        // THEN
        Assert.assertEquals(game.getLevel().getGhostPosition().x, ghostCurrent.x + 1);
        Assert.assertEquals(game.getLevel().getGhostPosition().y, ghostCurrent.y);
        Assert.assertEquals(game.getLevel().getGhostDirection(), Direction.RIGHT);
    }

    @Test
    public void ghostNewDirection() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("EASY", 4);

        // WHEN
        game.loadNewGame(gameID);
        Position playerCurrent = game.getLevel().getPlayerPosition();
        Position ghostCurrent = game.getLevel().getGhostPosition();
        Direction direction = Direction.RIGHT;
        game.getLevel().ghostChangeDirection(direction);
        game.getLevel().ghostChangeDirection(direction);
        boolean falseDirection = game.getLevel().getGhostDirection() == Direction.RIGHT;

        // THEN
        Assert.assertFalse(falseDirection);
    }


}
