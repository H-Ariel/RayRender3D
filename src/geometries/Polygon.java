package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected final List<Point> vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected final Plane plane;
    /**
     * The size of the polygon - the amount of the vertices in the polygon
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by
     *                 edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length == 0) { // default constructor
            this.vertices = null;
            plane = null;
            size = 0;
            return;
        }

        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector n = plane.getNormal();
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public void calcBoundingBox() {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
        for (Point p : vertices) {
            if (p.getX() < minX) minX = p.getX();
            if (p.getY() < minY) minY = p.getY();
            if (p.getZ() < minZ) minZ = p.getZ();
            if (p.getX() > maxX) maxX = p.getX();
            if (p.getY() > maxY) maxY = p.getY();
            if (p.getZ() > maxZ) maxZ = p.getZ();
        }

        this.boundingBox = new BoundingBox(
                new Point(minX, minY, minZ),
                new Point(maxX, maxY, maxZ)
        );
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        var intersectionPoint = plane.findGeoIntersections(ray, maxDistance);
        // the intersection point with the plane. we know it can be 1 or 0
        if (intersectionPoint == null) return null;

        // check if the intersection point is a vertex of the polygon
        for (Point v : vertices) {
            if (v.equals(intersectionPoint.getFirst().point))
                return null;
        }

        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        Vector[] vectors = new Vector[size];
        Vector[] normals = new Vector[size];

        for (int i = 0; i < size; i++) { // calculate the vectors from the intersection point to the vertices
            vectors[i] = vertices.get(i).subtract(p0);
        }
        for (int i = 0; i < size; i++) { // calculate the normal vectors to the polygon
            normals[i] = vectors[i].crossProduct(vectors[(i + 1) % size]).normalize();
        }

        // in the next code we check if the intersection point is in the polygon. we do it
        // by checking if the intersection point is in the same direction of the normal vectors
        // i.e. the dot product of the direction vector and the normal vector is positive
        double sign = v.dotProduct(normals[0]);
        for (Vector n : normals) {
            if (!Util.compareSign(sign, n.dotProduct(v)))
                return null;
        }

        intersectionPoint.getFirst().geometry = this;
        return intersectionPoint;
    }

    /**
     * Getter for the vertices of the polygon
     * @return the vertices of the polygon
     */
    public List<Point> getVertices() {
        return vertices;
    }
}