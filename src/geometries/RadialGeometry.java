package geometries;

import static primitives.Util.alignZero;

/**
 * Class to present a radial geometry
 *
 * @author Ariel and Asaf
 */
abstract class RadialGeometry extends Geometry {
    /**
     * The radius of the geometry
     */
    protected final double radius;
    /**
     * The squared radius of the geometry
     */
    protected final double radiusSquared;

    /**
     * Constructor for RadialGeometry - saves the radius
     *
     * @param radius radius of the geometry
     */
    public RadialGeometry(double radius) {
        if (alignZero(radius) <= 0)
            throw new IllegalArgumentException("radius should be Natural number");
        this.radius = radius;
        radiusSquared = radius * radius;
    }

    /**
     * Getter for the radius
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }
}