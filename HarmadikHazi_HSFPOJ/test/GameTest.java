import hu.elte.hsfpoj.model.Direction;
import hu.elte.hsfpoj.model.Game;
import hu.elte.hsfpoj.model.GameIdentifier;
import hu.elte.hsfpoj.model.Position;
import org.junit.Assert;
import org.junit.Test;

public class GameTest {

    Game game = new Game();

    @Test
    public void createGame() {
        // GIVEN

        // WHEN

        // THEN
        Assert.assertNotNull(game.getResultManager());
        Assert.assertFalse(game.isLevelAlreadyLoaded());
        Assert.assertFalse(game.getLevels().isEmpty());
    }

    @Test
    public void gameLoaded() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);

        // THEN
        Assert.assertNotNull(game.getResultManager());
        Assert.assertTrue(game.isLevelAlreadyLoaded());
    }

    @Test
    public void playerStep() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);
        Direction direction = Direction.UP;
        boolean step = game.step(direction);

        // THEN
        Assert.assertTrue(step);
    }

    @Test
    public void createDirection() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);
        boolean newDirection =  game.createDirection() == Direction.DOWN ||
                                game.createDirection() == Direction.LEFT ||
                                game.createDirection() == Direction.UP ||
                                game.createDirection() == Direction.RIGHT;

        // THEN
        Assert.assertTrue(newDirection);
    }

    @Test
    public void isNearPlayer () {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 6);

        // WHEN
        game.loadNewGame(gameID);

        Position near1 = new Position(0, 8);
        Position near2 = new Position(3, 8);
        Position near3 = new Position(3, 11);

        Position notNear1 = new Position(0, 7);
        Position notNear2 = new Position(4, 8);
        Position notNear3 = new Position(4, 11);

        boolean isNear1 = game.isNearPlayer(near1);
        boolean isNear2 = game.isNearPlayer(near2);
        boolean isNear3 = game.isNearPlayer(near3);

        boolean isNotNear1 = game.isNearPlayer(notNear1);
        boolean isNotNear2 = game.isNearPlayer(notNear2);
        boolean isNotNear3 = game.isNearPlayer(notNear3);

        // THEN
        Assert.assertTrue(isNear1);
        Assert.assertTrue(isNear2);
        Assert.assertTrue(isNear3);
        Assert.assertFalse(isNotNear1);
        Assert.assertFalse(isNotNear2);
        Assert.assertFalse(isNotNear3);

    }


}
