package primitives;

import geometries.Intersectable.GeoPoint;

import java.util.List;


/**
 * class to present a ray
 *
 * @author Asaf and Ariel
 */
public class Ray {
    /**
     * The delta for the ray
     */
    public static final double DELTA = 0.1;
    /**
     * The head of the ray
     */
    private final Point head;
    /**
     * The direction of the ray
     */
    private final Vector direction;

    /**
     * constructor for ray - saves the head and the direction
     *
     * @param head the head of the ray
     * @param vec  the direction of the ray
     */
    public Ray(Point head, Vector vec) {
        this.head = head;
        this.direction = vec.normalize();
    }

    /**
     * constructor for ray - saves the head and the direction
     *
     * @param p0        the head of the ray
     * @param direction the direction of the ray
     * @param normal    the normal to the point
     */
    public Ray(Point p0, Vector direction, Vector normal) {
        double dotProduct = direction.dotProduct(normal);
        this.head = p0.add(normal.scale(dotProduct > 0 ? DELTA : -DELTA));
        this.direction = direction.normalize();
    }

    /**
     * getter for the head of the ray
     *
     * @return the head of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * getter for the point on the ray at a certain distance from the head
     *
     * @param t the distance from the head
     * @return the point on the ray at the distance t from the head
     */
    public Point getPoint(double t) {
        try {
            return head.add(direction.scale(t));
        } catch (IllegalArgumentException ignore) {
            return head;
        }
    }

    /**
     * getter for the direction of the ray
     *
     * @return the direction of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * find the closest point to the head of the ray
     *
     * @param points list of intersections points with the ray
     * @return the closest point to the head of the ray
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }

    /**
     * find the closest GEO-point to the head of the ray
     *
     * @param points list of intersections points with the ray
     * @return the closest point to the head of the ray as a GeoPoint
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        if (points == null || points.isEmpty()) return null;

        GeoPoint closest = null;
        double minDistanceSquared = Double.POSITIVE_INFINITY;
        for (var point : points) {
            double distanceSquared = point.point.distanceSquared(head);
            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                closest = point;
            }
        }

        return closest;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Ray ray && this.head.equals(ray.head) && this.direction.equals(ray.direction);
    }
    
    @Override
    public String toString() {
        return this.head + "" + this.direction;
    }
}