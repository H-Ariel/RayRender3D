package primitives;


import static primitives.Util.isZero;

/**
 * Vector class represents a vector in 3D space
 *
 * @author Asaf and Ariel
 */
public class Vector extends Point {
    /**
     * unit vector on the X axis
     */
    public static final Vector X = new Vector(1, 0, 0);
    /**
     * unit vector on the Y axis
     */
    public static final Vector Y = new Vector(0, 1, 0);
    /**
     * unit vector on the Z axis
     */
    public static final Vector Z = new Vector(0, 0, 1);

    /**
     * Default constructor
     */
    private Vector() {
        super(Double3.ONE);
    }

    /**
     * Constructor for Vector - saves the vector
     *
     * @param xyz the point
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("can't create vector 0");
    }

    /**
     * Constructor for Vector - saves the point
     *
     * @param x the x value of the point
     * @param y the y value of the point
     * @param z the z value of the point
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("can't create vector 0");
    }

    /**
     * vector size in the power of 2
     *
     * @return the size of the vector in the power of 2
     */
    public double lengthSquared() {
        return super.distanceSquared(Point.ZERO);
    }

    /**
     * return the length of the vector
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * adds vectors
     *
     * @param vector the vector to add
     * @return the new vector
     */
    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    /**
     * multiplying vector by scalar
     *
     * @param scalar the scalar to multiply by
     * @return the new vector
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

    /**
     * skalar multiplication
     *
     * @param vector the vector to multiply by
     * @return the new vector
     */
    public double dotProduct(Vector vector) {
        Double3 d = xyz.product(vector.xyz);
        return d.d1 + d.d2 + d.d3;
    }

    /**
     * returns the cross product of the vectors
     *
     * @param vector the vector to multiply by
     * @return the new vector
     */
    public Vector crossProduct(Vector vector) {
        return new Vector(
                xyz.d2 * vector.xyz.d3 - xyz.d3 * vector.xyz.d2,
                xyz.d3 * vector.xyz.d1 - xyz.d1 * vector.xyz.d3,
                xyz.d1 * vector.xyz.d2 - xyz.d2 * vector.xyz.d1
        );
    }

    /**
     * normalizes the vector
     *
     * @return the new vector
     */
    public Vector normalize() {
        return this.scale(1 / length());
    }

    /**
     * returns a vector perpendicular to the vector
     *
     * @return the new vector
     */
    public Vector makePerpendicularVector() {
        return ((isZero(xyz.d1) && isZero(xyz.d2)) ? Vector.X
                : new Vector(xyz.d2, -xyz.d1, 0)).normalize();
    }

    /**
     * Reflects this vector across the given vector.
     *
     * @param n the vector to reflect across
     * @return a new vector that is the reflection of this vector across {@code n}
     */
    public Vector reflect(Vector n) {
        double dotProduct = this.dotProduct(n);
        Vector scaledNormal = n.scale(dotProduct * 2);
        return this.subtract(scaledNormal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Vector other && super.equals(other);
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }
}
