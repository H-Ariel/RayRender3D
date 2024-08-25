package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class to present a radial geometry
 *
 * @author Ariel and Asaf
 */
public class Triangle extends Polygon {
    /**
     * Constructor for Triangle - saves 3 points on the triangle
     *
     * @param p1 point on the triangle
     * @param p2 point on the triangle
     * @param p3 point on the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * Default constructor
     */
    private Triangle() {
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        var intersection = plane.findGeoIntersections(ray, maxDistance);
        if (intersection == null)
            return null;

        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        Point p1 = vertices.getFirst();
        Point p2 = vertices.get(1);
        Vector v1 = p1.subtract(p0);
        Vector v2 = p2.subtract(p0);
        Vector n1 = v1.crossProduct(v2).normalize();
        double s1 = alignZero(n1.dotProduct(v));
        if (s1 == 0) return null;

        Point p3 = vertices.getLast();
        Vector v3 = p3.subtract(p0);
        Vector n2 = v2.crossProduct(v3).normalize();
        double s2 = alignZero(n2.dotProduct(v));
        if (s1 * s2 <= 0) return null;

        Vector n3 = v3.crossProduct(v1).normalize();
        double s3 = alignZero(n3.dotProduct(v));
        if (s1 * s3 <= 0) return null;

        return List.of(new GeoPoint(this, intersection.getFirst().point)); // we know there is only one intersection point
    }
}