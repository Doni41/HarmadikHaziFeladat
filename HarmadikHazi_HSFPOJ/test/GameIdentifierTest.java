import hu.elte.hsfpoj.model.GameIdentifier;
import org.junit.Assert;
import org.junit.Test;

public class GameIdentifierTest {
    @Test
    public void createGameIDEasy() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("EASY", 1);

        // WHEN

        // THEN
        Assert.assertEquals(gameID.getDifficulty(), "EASY");
        Assert.assertEquals(gameID.getLevel(), 1);
    }

    @Test
    public void createGameIDMedium() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("MEDIUM", 7);

        // WHEN

        // THEN
        Assert.assertEquals(gameID.getDifficulty(), "MEDIUM");
        Assert.assertEquals(gameID.getLevel(), 7);
    }

    @Test
    public void createGameIDHard() {
        // GIVEN
        GameIdentifier gameID = new GameIdentifier("HARD", 10);

        // WHEN

        // THEN
        Assert.assertEquals(gameID.getDifficulty(), "HARD");
        Assert.assertEquals(gameID.getLevel(), 10);
    }

    @Test
    public void createGameIDEqualsTrue() {
        // GIVEN
        GameIdentifier gameI1 = new GameIdentifier("MEDIUM", 7);
        GameIdentifier gameI2 = new GameIdentifier("MEDIUM", 7);

        // WHEN
        boolean areEquals = gameI1.equals(gameI2);

        // THEN
        Assert.assertTrue(areEquals);
    }

    @Test
    public void createGameIDEqualsFalse() {
        // GIVEN
        GameIdentifier gameI1 = new GameIdentifier("MEDIUM", 8);
        GameIdentifier gameI2 = new GameIdentifier("MEDIUM", 7);

        // WHEN
        boolean areEquals = gameI1.equals(gameI2);

        // THEN
        Assert.assertFalse(areEquals);
    }
}
