package hu.elte.hsfpoj.res;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResLoader {
    public static InputStream loadResource(String resName){
        return ResLoader.class.getResourceAsStream(resName);
    }

    public static Image loadImage(String resName) throws IOException {
        URL url = ResLoader.class.getResource(resName);
        return ImageIO.read(url);
    }
}
