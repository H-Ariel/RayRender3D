package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Triangle class
 *
 * @author Asaf and Ariel
 */
class TriangleTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Triangle triangle = new Triangle(p1, p2, p3);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> triangle.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = triangle.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertEquals(0d, result.dotProduct(p1.subtract(p3)), DELTA, "Triangle's normal is not orthogonal to one of the edges");
        assertEquals(0d, result.dotProduct(p1.subtract(p2)), DELTA, "Triangle's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // NOTE: same to polygon

        Triangle triangle = new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(1, 1, 1));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is inside the triangle (1 point)
        assertEquals(List.of(new Point(1.0, 0.5, 0.25)), triangle.findIntersections(new Ray(Point.ZERO, new Vector(1, 0.5, 0.25))), "Ray's line is inside the triangle");

        // TC02: Ray's line is outside the triangle's edges (0 points)
        assertNull(triangle.findIntersections(new Ray(Point.ZERO, new Vector(1, 0.5, 1))), "Ray's line is outside the triangle's edges");

        // TC03: Ray's line is on the triangle's vertices (0 points)
        assertNull(triangle.findIntersections(new Ray(Point.ZERO, new Vector(1, 2, 2))), "Ray's line is on the triangle's vertices");

        // =============== Boundary Values Tests ==================

        // TC11: Ray's line is on the triangle's edge (0 points)
        assertNull(triangle.findIntersections(new Ray(Point.ZERO, new Vector(1, 0.5, 0))), "Ray's line is on the triangle's edge");

        // TC12: Ray's line is on the triangle's vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(Point.ZERO, new Vector(1, 1, 0))), "Ray's line is on the triangle's vertex");

        // TC13: Ray's line is on the continuation of the triangle's edge (0 points)
        assertNull(triangle.findIntersections(new Ray(Point.ZERO, new Vector(1, 2, 0))), "Ray's line is on the continuation of the triangle's edge");
    }
}