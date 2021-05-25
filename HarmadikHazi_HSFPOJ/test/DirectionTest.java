import hu.elte.hsfpoj.model.Direction;
import org.junit.Assert;
import org.junit.Test;

public class DirectionTest {

    @Test
    public void createDirectionUp() {
        // GIVEN
        Direction direction = Direction.UP;
        // WHEN

        // THEN
        Assert.assertEquals(direction.x, 0);
        Assert.assertEquals(direction.y, -1);
    }

    @Test
    public void createDirectionDown() {
        // GIVEN
        Direction direction = Direction.DOWN;
        // WHEN

        // THEN
        Assert.assertEquals(direction.x, 0);
        Assert.assertEquals(direction.y, 1);
    }

    @Test
    public void createDirectionLeft() {
        // GIVEN
        Direction direction = Direction.LEFT;
        // WHEN

        // THEN
        Assert.assertEquals(direction.x, -1);
        Assert.assertEquals(direction.y, 0);
    }

    @Test
    public void createDirectionRight() {
        // GIVEN
        Direction direction = Direction.RIGHT;
        // WHEN

        // THEN
        Assert.assertEquals(direction.x, 1);
        Assert.assertEquals(direction.y, 0);
    }
}
