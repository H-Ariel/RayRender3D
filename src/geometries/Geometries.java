package geometries;

import primitives.Ray;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent a collection of geometries
 */
public class Geometries extends Intersectable {
    /**
     * List of geometries
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor
     */
    public Geometries() {
    }

    /**
     * Constructor with parameters
     *
     * @param geometries list of geometries
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Constructor with parameters
     *
     * @param geometries list of geometries
     */
    public Geometries(List<Intersectable> geometries) {
        add(geometries);
    }

    /**
     * Add geometries to the list
     *
     * @param geometries list of geometries
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Add geometries to the list
     *
     * @param geometries list of geometries
     */
    public void add(List<Intersectable> geometries) {
        this.geometries.addAll(geometries);
    }

    @Override
    public void calcBoundingBox() {
        if (geometries.isEmpty()) {
            boundingBox = null;
        } else {
            boolean isInfinity = false;
            boundingBox = new BoundingBox();
            for (Intersectable g : geometries) {
                g.calcBoundingBox();
                if (g.boundingBox == null)
                    isInfinity = true;
                else
                    boundingBox = boundingBox.union(g.boundingBox);
            }

            if (isInfinity)
                boundingBox = null;
        }
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = null;

        for (Intersectable geo : geometries) {
            List<GeoPoint> tmp = geo.findGeoIntersections(ray, maxDistance);
            if (tmp == null) continue; // if there are intersections

            if (intersections == null) // if this is the first intersection
                intersections = new LinkedList<>(tmp);
            else // if there are already intersections
                intersections.addAll(tmp);
        }

        return intersections;
    }

    /**
     * Calculate the Conservative Bounding Region for the geometries
     */
    public void makeCBR() {
        calcBoundingBox();
    }

    /**
     * Store the geometries as a BVH (Bounding Volume Hierarchy)
     */
    public void makeBVH() {
        // calculate the bounding box for the geometries, so we can sort them by position
        calcBoundingBox();

        // extract infinite geometries into a separate list
        List<Intersectable> infiniteGeometries = geometries.stream().filter(g -> g.boundingBox == null).toList();
        geometries.removeAll(infiniteGeometries);

        // sort geometries based on their bounding box centroids along an axis (e.g. x-axis)
        // TODO: find better sorting algorithm
        geometries.sort(Comparator.comparingDouble(g -> g.boundingBox.getCenter().getX()));

        // combine each 3 geometries into a bounding box
        while (geometries.size() >= 3)
            geometries.add(new Geometries(geometries.removeFirst(), geometries.removeFirst(), geometries.removeFirst()));

        geometries.addAll(infiniteGeometries); // combine the infinite geometries back
        calcBoundingBox(); // recalculate the bounding box because the geometries have changed
    }

    /**
     * Flatten the geometries
     *
     * @return the flattened geometries
     */
    public Geometries flattenGeometries() {
        Geometries flatGeometries = new Geometries();
        geometries.forEach(i -> flatGeometries.add(i instanceof Geometries g ? g.flattenGeometries().geometries : List.of(i)));
        return flatGeometries;
    }

    /**
     * Get the geometries
     *
     * @return the geometries
     */
    public List<Intersectable> getGeometries() {
        return geometries;
    }
}