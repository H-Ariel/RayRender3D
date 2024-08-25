package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class to present a circle in 3D space
 */
public class Circle extends RadialGeometry {

    // TODO: add unit tests

    /**
     * The center of the circle
     */
    private final Point center;

    /**
     * The plane of the circle
     */
    private final Plane plane;

    /**
     * Constructor for Circle - saves the center, radius and normal
     *
     * @param center the center of the circle
     * @param radius the radius of the circle
     * @param normal the normal of the circle
     */
    public Circle(Point center, double radius, Vector normal) {
        super(radius);
        this.center = center;
        this.plane = new Plane(center, normal);
    }

    @Override
    public void calcBoundingBox() {
        this.boundingBox = new BoundingBox(
                new Point(center.getX() - radius, center.getY() - radius, center.getZ() - radius),
                new Point(center.getX() + radius, center.getY() + radius, center.getZ() + radius)
        );
    }

    @Override
    public Vector getNormal(Point p) {
        return plane.getNormal();
    }

    @Override
    protected List<Intersectable.GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersectable.GeoPoint> planeIntersections = plane.findGeoIntersections(ray);
        if (planeIntersections == null)
            return null;

        Intersectable.GeoPoint gp = planeIntersections.getFirst();
        if (alignZero(gp.point.distance(center)) > radius)
            return null;

        gp.geometry = this;
        return List.of(gp);
    }
}
