package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class to present a cylinder
 *
 * @author Ariel and Asaf
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder
     */
    private final double height;
    /**
     * The bottom base of the cylinder
     */
    private final Circle bottomBase;
    /**
     * The top base of the cylinder
     */
    private final Circle topBase;

    /**
     * Constructor for Tube - saves the axis ray and the radius
     *
     * @param axisRay axis ray of the tube
     * @param radius  radius of the tube
     * @param height  height of the tube
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        if (alignZero(height) <= 0)
            throw new IllegalArgumentException("height of cylinder must be positive");
        this.height = height;

        this.bottomBase = new Circle(axisRay.getHead(), radius, axisRay.getDirection());
        this.topBase = new Circle(axisRay.getPoint(height), radius, axisRay.getDirection());
    }

    @Override
    public Vector getNormal(Point p) {
        Point p0 = axis.getHead();

        if (p.equals(p0))
            return axis.getDirection().normalize();

        Vector v = axis.getDirection();
        double t = v.dotProduct((p.subtract(p0)));
        if (isZero(t) || isZero(t - height)) {
            return v.normalize();
        }
        return super.getNormal(p);
    }

    @Override
    public void calcBoundingBox() {
        bottomBase.calcBoundingBox();
        topBase.calcBoundingBox();

        this.boundingBox = new BoundingBox(
                new Point(
                        Math.min(bottomBase.boundingBox.min.getX(), topBase.boundingBox.min.getX()),
                        Math.min(bottomBase.boundingBox.min.getY(), topBase.boundingBox.min.getY()),
                        Math.min(bottomBase.boundingBox.min.getZ(), topBase.boundingBox.min.getZ())
                ),
                new Point(
                        Math.max(bottomBase.boundingBox.max.getX(), topBase.boundingBox.max.getX()),
                        Math.max(bottomBase.boundingBox.max.getY(), topBase.boundingBox.max.getY()),
                        Math.max(bottomBase.boundingBox.max.getZ(), topBase.boundingBox.max.getZ())
                )
        );

    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = new LinkedList<>();

        List<GeoPoint> tubeIntersections = super.findGeoIntersectionsHelper(ray, maxDistance);
        if (tubeIntersections != null) {
            for (GeoPoint geoPoint : tubeIntersections) {
                double t = axis.getDirection().dotProduct(geoPoint.point.subtract(axis.getHead()));
                if (alignZero(t) >= 0 && alignZero(t - height) <= 0) {
                    geoPoint.geometry = this;
                    intersections.add(geoPoint);
                }
            }
        }

        List<GeoPoint> bottomBaseIntersections = bottomBase.findGeoIntersections(ray);
        if (bottomBaseIntersections != null) {
            GeoPoint gp = bottomBaseIntersections.getFirst();
            gp.geometry = this;
            intersections.add(gp);
        }

        List<GeoPoint> topBaseIntersections = topBase.findGeoIntersections(ray);
        if (topBaseIntersections != null) {
            GeoPoint gp = topBaseIntersections.getFirst();
            gp.geometry = this;
            intersections.add(gp);
        }

        return intersections.isEmpty() ? null : intersections;
    }
}