package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class to present a plane
 *
 * @author Ariel and Asaf
 */
public class Plane extends Geometry {
    /**
     * The reference point on the plane
     */
    private final Point q;
    /**
     * The normal to the plane
     */
    private final Vector normal;

    /**
     * Default constructor
     */
    private Plane() {
        q = null;
        normal = null;
    }

    /**
     * Constructor for Plane - saves point on the plane and the normal to the plane
     *
     * @param q      point on the plane
     * @param normal normal to the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Constructor for Plane - calculates the normal to the plane and save point on the plane
     *
     * @param p1 point on the plane
     * @param p2 point on the plane
     * @param p3 point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.q = p1;
        Vector vec1 = p2.subtract(p1);
        Vector vec2 = p3.subtract(p1);
        normal = vec1.crossProduct(vec2).normalize();
    }

    /**
     * returns the normal to the plane
     *
     * @return normal to the plane
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    @Override
    public void calcBoundingBox() {
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        double nv = normal.dotProduct(v);
        if (Util.isZero(nv) || q.equals(p0))
            return null;

        double t = alignZero(normal.dotProduct(q.subtract(p0)) / nv);
        return (t <= 0 || alignZero(maxDistance - t) <= 0)
                ? null
                : List.of(new GeoPoint(this, ray.getPoint(t)));
    }

    /**
     * Getter for the point on the plane
     *
     * @return the point on the plane
     */
    public Point getQ() {
        return q;
    }
}