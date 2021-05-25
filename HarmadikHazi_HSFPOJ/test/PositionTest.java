import hu.elte.hsfpoj.model.Direction;
import hu.elte.hsfpoj.model.Position;
import org.junit.Assert;
import org.junit.Test;

public class PositionTest {
    @Test
    public void createPosition() {
        // GIVEN
        Position position = new Position(0,0);
        // WHEN

        // THEN
        Assert.assertEquals(position.x, 0);
        Assert.assertEquals(position.y, 0);
    }

    @Test
    public void createNotNullPosition () {
        // GIVEN
        Position position = new Position(101,87);

        // WHEN

        //THEN
        Assert.assertEquals(position.x, 101);
        Assert.assertEquals(position.y, 87);
    }

    @Test
    public void positionEqualsTrue () {
        // GIVEN
        Position position1 = new Position(0,0);
        Position position2 = new Position(0,0);

        // WHEN
        boolean areEquals = position1.equals(position2);

        Assert.assertTrue(areEquals);
    }

    @Test
    public void positionTranslateUp () {
        // GIVEN
        Position position = new Position(11,23);
        Direction direction = Direction.UP;

        // WHEN
        int currX = position.x;
        int currY = position.y;
        position = position.translate(direction);

        // THEN
        Assert.assertEquals(position.x, currX);
        Assert.assertEquals(position.y, currY - 1);
    }

    @Test
    public void positionTranslateDown () {
        // GIVEN
        Position position = new Position(11,23);
        Direction direction = Direction.DOWN;

        // WHEN
        int currX = position.x;
        int currY = position.y;
        position = position.translate(direction);

        // THEN
        Assert.assertEquals(position.x, currX);
        Assert.assertEquals(position.y, currY + 1);
    }

    @Test
    public void positionTranslateLeft () {
        // GIVEN
        Position position = new Position(11,23);
        Direction direction = Direction.LEFT;

        // WHEN
        int currX = position.x;
        int currY = position.y;
        position = position.translate(direction);

        // THEN
        Assert.assertEquals(position.x, currX - 1);
        Assert.assertEquals(position.y, currY);
    }

    @Test
    public void positionTranslateRight () {
        // GIVEN
        Position position = new Position(11,23);
        Direction direction = Direction.RIGHT;

        // WHEN
        int currX = position.x;
        int currY = position.y;
        position = position.translate(direction);

        // THEN
        Assert.assertEquals(position.x, currX + 1);
        Assert.assertEquals(position.y, currY);
    }

    @Test
    public void positionTranslateToTwoDirection () {
        // GIVEN
        Position position = new Position(11,23);
        Direction direction1 = Direction.RIGHT;
        Direction direction2 = Direction.UP;

        // WHEN
        int currX = position.x;
        int currY = position.y;
        position = position.translate(direction1);
        position = position.translate(direction2);

        // THEN
        Assert.assertEquals(position.x, currX + 1);
        Assert.assertEquals(position.y, currY - 1);
    }

}
