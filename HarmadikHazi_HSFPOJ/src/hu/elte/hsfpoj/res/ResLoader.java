package hu.elte.hsfpoj.res;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResLoader {
    /**
     * loads a resource file
     * @param resName is the full path of the resource file
     * @return
     */
    public static InputStream loadResource(String resName){
        return ResLoader.class.getResourceAsStream(resName);
    }

    /**
     * loads an image
     * @param resName is the full path of the picture
     * @return
     * @throws IOException
     */
    public static Image loadImage(String resName) throws IOException {
        URL url = ResLoader.class.getResource(resName);
        return ImageIO.read(url);
    }
}
