package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class to present a tube
 *
 * @author Ariel and Asaf
 */
public class Tube extends RadialGeometry {
    /**
     * The axis of the tube
     */
    protected final Ray axis;

    /**
     * Constructor for Tube - saves the axis ray and the radius
     *
     * @param axisRay axis ray of the tube
     * @param radius  radius of the tube
     */
    public Tube(Ray axisRay, double radius) {
        super(radius);
        this.axis = axisRay;
    }

    @Override
    public void calcBoundingBox() {
    }

    @Override
    public Vector getNormal(Point p) {
        double t = axis.getDirection().dotProduct(p.subtract(axis.getHead()));
        return p.subtract(axis.getPoint(t)).normalize();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        // based on https://github.com/sheinakorem/ISE5783_8715_6534/blob/master/src/geometries/Tube.java

        Point rayHead = ray.getHead();
        Point axisHead = axis.getHead();
        Vector rayDirection = ray.getDirection();
        Vector axisDirection = axis.getDirection();

        Vector rayAdjusted = rayDirection;
        double rayAxisDotProduct = rayDirection.dotProduct(axisDirection);
        if (!isZero(rayAxisDotProduct)) {
            Vector axisScaled = axisDirection.scale(rayAxisDotProduct);
            if (rayDirection.equals(axisScaled))
                return null;
            rayAdjusted = rayDirection.subtract(axisScaled);
        }

        double adjustedDotProduct = 0;
        double squaredDifference = 0;
        if (!rayHead.equals(axisHead)) {
            Vector headDifference = rayHead.subtract(axisHead);
            double headDotAxis = headDifference.dotProduct(axisDirection);
            if (isZero(headDotAxis)) {
                adjustedDotProduct = rayAdjusted.dotProduct(headDifference);
                squaredDifference = headDifference.lengthSquared();
            } else {
                Vector axisOriginScaled = axisDirection.scale(headDotAxis);
                if (!headDifference.equals(axisOriginScaled)) {
                    Vector differenceAdjusted = headDifference.subtract(axisOriginScaled);
                    adjustedDotProduct = rayAdjusted.dotProduct(differenceAdjusted);
                    squaredDifference = differenceAdjusted.lengthSquared();
                }
            }
        }

        double a = rayAdjusted.lengthSquared();
        double b = 2 * adjustedDotProduct;
        double c = alignZero(squaredDifference - radiusSquared);

        double discriminant = alignZero(b * b - 4 * a * c);
        if (discriminant <= 0)
            return null;

        double a2 = 2 * a;
        double tm = -b / a2;
        double th = Math.sqrt(discriminant) / a2;
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);
        double t1MinDis = alignZero(t1 - maxDistance);
        double t2MinDis = alignZero(t2 - maxDistance);

        if (t1 > 0 && t2 > 0 && t1MinDis <= 0 && t2MinDis <= 0)
            return List.of(new GeoPoint(this, ray.getPoint(t2)), new GeoPoint(this, ray.getPoint(t1)));
        if (t1 > 0 && t1MinDis <= 0)
            return List.of(new GeoPoint(this, ray.getPoint(t1)));
        if (t2 > 0 && t2MinDis <= 0)
            return List.of(new GeoPoint(this, ray.getPoint(t2)));
        return null;
    }
}