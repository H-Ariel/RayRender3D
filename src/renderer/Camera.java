package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.MissingResourceException;


/**
 * Class to present a camera
 */
public class Camera implements Cloneable {
    /**
     * The target area
     */
    private final TargetArea targetArea = new TargetArea();
    /**
     * The image writer
     */
    private ImageWriter imageWriter;
    /**
     * The ray tracer
     */
    private RayTracerBase rayTracer;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    /**
     * Number of threads to use for rendering
     */
    private int threadsCount = 4;

    /**
     * Private constructor
     */
    private Camera() {
    }

    /**
     * Get a camera builder
     *
     * @return a new camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Construct a ray from the camera to a pixel
     *
     * @param nX size of webcam in X
     * @param nY size of webcam in Y
     * @param j  the x index of the pixel
     * @param i  the y index of the pixel
     * @return the ray from the camera to the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        return targetArea.constructRay(nX, nY, j, i);
    }

    /**
     * Cast a ray from the camera to a pixel
     *
     * @param nx     size of webcam in X
     * @param ny     size of webcam in Y
     * @param column the x index of the pixel
     * @param row    the y index of the pixel
     */
    void castRay(int nx, int ny, int column, int row) {
        Ray ray = constructRay(nx, ny, column, row);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(column, row, color);
    }

    /**
     * Render the image
     *
     * @return the camera
     */
    public Camera renderImage() {
        final int nx = imageWriter.getNx(), ny = imageWriter.getNy();
        pixelManager = new PixelManager(ny, nx);

        if (threadsCount == 0)
            for (int i = 0; i < ny; i++) {
                for (int j = 0; j < nx; j++) {
                    castRay(nx, ny, j, i);
                }
            }
        else { // see further... option 2
            var threads = new LinkedList<Thread>(); // list of threads
            for (int i = 0; i < threadsCount; i++) // add appropriate number of threads
                threads.add(new Thread(() -> { // add a thread with its code
                    PixelManager.Pixel pixel; // current pixel(row,col)
                    // allocate pixel(row,col) in loop until there are no more pixels
                    while ((pixel = pixelManager.nextPixel()) != null)
                        // cast ray through pixel (and color it – inside castRay)
                        castRay(nx, ny, pixel.col(), pixel.row());
                }));
            // start all the threads
            for (var thread : threads) thread.start();
            // wait until all the threads have finished
            try {
                for (var thread : threads) thread.join();
            } catch (InterruptedException ignore) {
            }
        }

        return this;
    }

    /**
     * Print a grid on the view plane
     *
     * @param interval the interval between the lines
     * @param color    the color of the grid
     * @return the camera
     */
    public Camera printGrid(int interval, Color color) {
        int nx = imageWriter.getNx(), ny = imageWriter.getNy();
        int i, j;

        for (i = 0; i < ny; i += interval) {
            for (j = 0; j < nx; j++) {
                imageWriter.writePixel(j, i, color);
            }
        }
        for (i = 0; i < nx; i += interval) {
            for (j = 0; j < ny; j++) {
                imageWriter.writePixel(i, j, color);
            }
        }

        return this;
    }

    /**
     * Write the image to a file
     *
     * @return the camera
     */
    public Camera writeToImage() {
        imageWriter.writeToImage();
        return this;
    }

    /**
     * Camera builder
     */
    public static class Builder {
        /**
         * out camera to build
         */
        private final Camera camera = new Camera();

        /**
         * Set the camera location point
         *
         * @param point the camera location point
         * @return the camera builder
         */
        public Builder setLocation(Point point) {
            camera.targetArea.setLocation(point);
            return this;
        }

        /**
         * Set the camera direction
         *
         * @param vTo the camera direction
         * @param vUp the camera up direction
         * @return the camera builder
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            camera.targetArea.setDirection(vTo, vUp);
            return this;
        }

        /**
         * Set the view plane size
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the camera builder
         */
        public Builder setVpSize(double width, double height) {
            camera.targetArea.setVpSize(width, height);
            return this;
        }

        /**
         * Set the distance from the camera to the view plane
         *
         * @param distance the distance from the camera to the view plane
         * @return the camera builder
         */
        public Builder setVpDistance(double distance) {
            camera.targetArea.setVpDistance(distance);
            return this;
        }

        /**
         * Set the density of the view plane
         *
         * @param density the density of the view plane
         * @return the camera builder
         */
        public Builder setDensity(int density) {
            camera.targetArea.setDensity(density);
            return this;
        }

        /**
         * Set the number of threads to use for rendering
         *
         * @param threadsCount the number of threads to use for rendering
         * @return the camera builder
         */
        public Builder setMultithreading(int threadsCount) {
            if (camera.threadsCount < 0)
                throw new MissingResourceException("threads count can't be smaller than 0", "Camera", "");
            camera.threadsCount = threadsCount;
            return this;
        }

        /**
         * rotate the camera view to the given point
         *
         * @param p point to watch there
         * @return the builder
         */
        public Builder lookAt(Point p) {
            camera.targetArea.lookAt(p);
            return this;
        }

        /**
         * Set the image writer
         *
         * @param imageWriter the image writer
         * @return the camera builder
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Set the ray tracer
         *
         * @param rayTracer the ray tracer
         * @return the camera builder
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Build the camera. In case of missing parameters, an exception will be thrown.
         *
         * @return the camera
         */
        public Camera build() {
            // check composed objects
            if (camera.imageWriter == null)
                throw new MissingResourceException("imageWriter is missing", "Camera", "");
            if (camera.rayTracer == null)
                throw new MissingResourceException("rayTracer is missing", "Camera", "");

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException ignore) {
                return null;
            }
        }
    }
}