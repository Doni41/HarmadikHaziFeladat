import hu.elte.hsfpoj.persistance.Result;
import org.junit.Assert;
import org.junit.Test;

public class ResultTest {

    @Test
    public void createResult() {
        // GIVEN
        Result result = new Result("Akos", 10);

        // WHEN

        // THEN
        Assert.assertEquals(result.getName(), "Akos");
        Assert.assertEquals(result.getHighScore(), 10);

    }
}
