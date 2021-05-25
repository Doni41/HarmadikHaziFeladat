import hu.elte.hsfpoj.model.Item;
import org.junit.Assert;
import org.junit.Test;

public class ItemTest {

    @Test
    public void createItemPlayer () {
        // GIVEN
        Item item = Item.PLAYER;

        // WHEN

        // THEN
        Assert.assertEquals(item.representation, '@');
    }

    @Test
    public void createItemGhost () {
        // GIVEN
        Item item = Item.GHOST;

        // WHEN

        // THEN
        Assert.assertEquals(item.representation, '*');
    }

    @Test
    public void createItemDestination () {
        // GIVEN
        Item item = Item.DESTINATION;

        // WHEN

        // THEN
        Assert.assertEquals(item.representation, '.');
    }

    @Test
    public void createItemWall () {
        // GIVEN
        Item item = Item.WALL;

        // WHEN

        // THEN
        Assert.assertEquals(item.representation, '#');
    }

    @Test
    public void createItemEmpty () {
        // GIVEN
        Item item = Item.EMPTY;

        // WHEN

        // THEN
        Assert.assertEquals(item.representation, ' ');
    }
}
