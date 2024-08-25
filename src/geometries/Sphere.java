package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class to present a sphere
 *
 * @author Ariel and Asaf
 */
public class Sphere extends RadialGeometry {
    /**
     * The center of the sphere
     */
    private final Point center;

    /**
     * Default constructor
     */
    private Sphere() {
        super(1);
        center = Point.ZERO;
    }

    /**
     * Constructor for Sphere - saves the center and the radius
     *
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }

    @Override
    public void calcBoundingBox() {
        this.boundingBox = new BoundingBox(
                new Point(center.getX() - radius, center.getY() - radius, center.getZ() - radius),
                new Point(center.getX() + radius, center.getY() + radius, center.getZ() + radius)
        );
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Vector v = ray.getDirection();
        Point p0 = ray.getHead();

        if (center.equals(p0))
            return List.of(new GeoPoint(this, center.add(v.scale(radius))));

        Vector u = center.subtract(p0);
        double tm = u.dotProduct(v);
        double dSquared = u.lengthSquared() - tm * tm;
        double thSquared = radiusSquared - dSquared;
        if (alignZero(thSquared) <= 0) // if d >= radius
            return null; // no intersections

        double th = Math.sqrt(thSquared);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t2 <= 0 || alignZero(maxDistance - t1) <= 0) return null; // the sphere is behind the ray

        if (t1 <= 0)
            return alignZero(maxDistance - t2) <= 0
                    ? null
                    : List.of(new GeoPoint(this, ray.getPoint(t2)));
        else
            return alignZero(maxDistance - t2) <= 0
                    ? List.of(new GeoPoint(this, ray.getPoint(t1)))
                    : List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
    }

    /**
     * Getter for the center
     *
     * @return the center point
     */
    public Point getCenter() {
        return center;
    }
}