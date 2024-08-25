package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Interface to present an intersectable geometry
 * All geometries that can be intersected should implement this interface
 *
 * @author Asaf and Ariel
 */
public abstract class Intersectable {
    /**
     * The bounding box of the geometry
     */
    protected BoundingBox boundingBox;

    /**
     * Method to find the intersections of a ray with the geometry
     *
     * @param ray the ray to find the intersections with
     * @return a list of the intersections points
     */
    public final List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * Method to find the intersections of a ray with the geometry
     *
     * @param ray         the ray to find the intersections with
     * @param maxDistance the maximum distance to find the intersections in
     * @return a list of the intersections points
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        if (boundingBox != null && !boundingBox.hasIntersections(ray))
            return null;
        return findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Method to find the intersections of a ray with the geometry
     *
     * @param ray the ray to find the intersections with
     * @return a list of the intersections points with the geometry that contains the point
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * A helper method to find the intersections of a ray with the geometry
     *
     * @param ray         the ray to find the intersections with
     * @param maxDistance the maximum distance to find the intersections in
     * @return a list of the intersections points with the geometry that contains the point
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Calculate the bounding box of this intersectable.
     */
    public abstract void calcBoundingBox();

    /**
     * Class to present a point on the geometry
     */
    public static class GeoPoint {
        /**
         * The geometry that contains the point
         */
        public Geometry geometry;
        /**
         * The point on the geometry
         */
        public Point point;

        /**
         * Constructor for GeoPoint - saves the geometry and the point
         *
         * @param geometry the geometry that contains the point
         * @param point    the point on the geometry
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj instanceof GeoPoint other && (geometry == other.geometry) && point.equals(other.point);
        }

        @Override
        public String toString() {
            return this.geometry + " " + this.point;
        }
    }
}
