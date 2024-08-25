package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

/**
 * Test ImageWriter Class
 */
public class ImageWriterTest {

    /**
     * Test method for {@link renderer.ImageWriter#writeToImage()}
     * Test by creating a grid image
     */
    @Test
    void testWriteToImage() {
        int t = 50; // square size (pixels)
        int w = 801, h = 501; // width and height of image (pixels)

        ImageWriter writer = new ImageWriter("kowabunga", w, h);

        Color background = new Color(255d, 255d, 0d);
        Color grid = new Color(127d, 0d, 127d);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                writer.writePixel(j, i, (i % t == 0 || j % t == 0) ? grid : background);
            }
        }

        writer.writeToImage();
    }
}
