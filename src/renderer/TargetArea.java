package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


/**
 * Class to present a target area
 */
public class TargetArea {
    /**
     * The TargetArea location point (center of the view plane)
     */
    private Point p0 = Point.ZERO;

    /**
     * The TargetArea direction where it looks
     */
    private Vector vTo;

    /**
     * The TargetArea up direction
     */
    private Vector vUp;

    /**
     * The TargetArea right direction
     */
    private Vector vRight;

    /**
     * The height of the view plane
     */
    private double height = 0;

    /**
     * The width of the view plane
     */
    private double width = 0;

    /**
     * The distance from the TargetArea to the view plane
     */
    private double distance = 100;

    /**
     * The density of the grid
     */
    private int density = 9;


    /**
     * private Constructor for TargetArea class
     */
    public TargetArea() {
    }

    /**
     * Construct a ray from the TargetArea to a pixel
     *
     * @param nX size of webcam in X
     * @param nY size of webcam in Y
     * @param j  the x index of the pixel
     * @param i  the y index of the pixel
     * @return the ray from the TargetArea to the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        return constructRay(nX, nY, j, i, 0, 0);
    }

    /**
     * Construct a ray from the TargetArea to a pixel
     *
     * @param nX      size of webcam in X
     * @param nY      size of webcam in Y
     * @param j       the x index of the pixel
     * @param i       the y index of the pixel
     * @param jitterX the x jitter
     * @param jitterY the y jitter
     * @return the ray from the TargetArea to the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i, double jitterX, double jitterY) {
        Point pIJ = p0.add(vTo.scale(distance));
        double yI = (((nY - 1) / 2.0) - i + jitterY) * (height / nY);
        double xJ = (j - ((nX - 1) / 2.0) + jitterX) * (width / nX);
        if (!isZero(xJ))
            pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI))
            pIJ = pIJ.add(vUp.scale(yI));
        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * Constructs a grid of rays in the target area
     *
     * @return list of rays
     */
    public List<Ray> constructRayGrid() {
        List<Ray> rays = new LinkedList<>();
        for (int i = 0; i < density; ++i)
            for (int j = 0; j < density; j++) {
                // used of jitter make images more realistic but slower
                //rays.add(constructRay(density, density, j, i, Math.random() - 0.5, Math.random() - 0.5));
                rays.add(constructRay(density, density, j, i));
            }
        return rays;
    }


    /**
     * Set the TargetArea direction according to the ray
     *
     * @param ray the TargetArea direction
     * @return the TargetArea builder
     */
    public TargetArea setDirection(Ray ray) {
        this.p0 = ray.getHead();
        this.vTo = ray.getDirection();
        this.vUp = this.vTo.makePerpendicularVector();
        this.vRight = this.vUp.crossProduct(this.vTo);
        return this;
    }

    /**
     * Set the TargetArea location point
     *
     * @param point the TargetArea location point
     * @return the TargetArea builder
     */
    public TargetArea setLocation(Point point) {
        this.p0 = point;
        return this;
    }

    /**
     * Set the TargetArea density
     *
     * @param density the TargetArea density
     * @return the TargetArea builder
     */
    public TargetArea setDensity(int density) {
        if (density <= 0)
            throw new IllegalArgumentException("Density cannot be equal or smaller than 0");
        this.density = density;
        return this;
    }

    /**
     * Set the TargetArea direction
     *
     * @param vTo the TargetArea direction
     * @param vUp the TargetArea up direction
     * @return the TargetArea builder
     */
    public TargetArea setDirection(Vector vTo, Vector vUp) {
        if (!isZero(vTo.dotProduct(vUp)))
            throw new IllegalArgumentException("The vectors not are perpendicular to one another");
        this.vTo = vTo;
        this.vUp = vUp;

        this.vTo = this.vTo.normalize();
        this.vUp = this.vUp.normalize();
        this.vRight = this.vTo.crossProduct(this.vUp).normalize();

        return this;
    }

    /**
     * Set the view plane size
     *
     * @param width  the width of the view plane
     * @param height the height of the view plane
     * @return the TargetArea builder
     */
    public TargetArea setVpSize(double width, double height) {
        if (width <= 0)
            throw new IllegalArgumentException("Width cannot be equal or smaller than 0");
        if (height <= 0)
            throw new IllegalArgumentException("Height cannot be equal or smaller than 0");
        this.width = alignZero(width);
        this.height = alignZero(height);
        return this;
    }

    /**
     * Set the distance from the TargetArea to the view plane
     *
     * @param distance the distance from the TargetArea to the view plane
     * @return the TargetArea builder
     */
    public TargetArea setVpDistance(double distance) {
        if (distance <= 0)
            throw new IllegalArgumentException("Distance cannot be equal or smaller than 0");
        this.distance = alignZero(distance);
        return this;
    }

    /**
     * rotate the TargetArea view to the given point
     *
     * @param p point to watch there
     * @return the builder
     */
    public TargetArea lookAt(Point p) {
        this.vTo = p.subtract(this.p0).normalize();

        if (this.vTo.equals(Vector.Y)) {
            this.vRight = Vector.X;
            this.vUp = Vector.Z;
        } else {
            // vector Y with little angle, so it will be perpendicular to vTo
            this.vRight = Vector.Y.crossProduct(this.vTo).normalize();
            this.vUp = this.vRight.crossProduct(this.vTo).normalize();
        }

        return this;
    }
}