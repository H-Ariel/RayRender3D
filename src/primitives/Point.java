package primitives;

/**
 * The class is in charge of defining the different aspects of the POINT
 *
 * @author Asaf and Ariel
 */
public class Point {
    /**
     * The point (0, 0, 0)
     */
    public static final Point ZERO = new Point(0, 0, 0);
    /**
     * The point
     */
    protected final Double3 xyz;

    /**
     * Constructor for Point - saves the point
     *
     * @param xyz the point
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Default constructor
     */
    private Point() {
        xyz = Double3.ZERO;
    }

    /**
     * Constructor for Point - saves the point
     *
     * @param x the x value of the point
     * @param y the y value of the point
     * @param z the z value of the point
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * the add function moves a point and adds it to the vector
     *
     * @param vector the vector to add
     * @return the new point
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * the subtract function removes a point and adds it to the vector
     *
     * @param point the point to subtract
     * @return the new point
     */
    public Vector subtract(Point point) {
        return new Vector(xyz.subtract(point.xyz));
    }

    /**
     * calculates the distance between two points
     *
     * @param point the point to calculate the distance from
     * @return the distance between the two points
     */
    public double distance(Point point) {
        return Math.sqrt(distanceSquared((point)));
    }

    /**
     * the distance between two points in the power of 2
     *
     * @param point the point to calculate the distance from
     * @return the distance between the two points in the power of 2
     */
    public double distanceSquared(Point point) {
        double x = xyz.d1 - point.xyz.d1;
        double y = xyz.d2 - point.xyz.d2;
        double z = xyz.d3 - point.xyz.d3;
        return x * x + y * y + z * z;
    }

    /**
     * get the x value of the point
     *
     * @return the x value of the point
     */
    public double getX() {
        return xyz.d1;
    }

    /**
     * get the y value of the point
     *
     * @return the y value of the point
     */
    public double getY() {
        return xyz.d2;
    }

    /**
     * get the z value of the point
     *
     * @return the z value of the point
     */
    public double getZ() {
        return xyz.d3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Point other && xyz.equals(other.xyz);
    }

    @Override
    public String toString() {
        return "" + xyz;
    }
}
