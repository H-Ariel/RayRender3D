package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 *
 * @author Ariel and Asaf
 */
class PlaneTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private static final double DELTA = 0.00001;


    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testConstructor() {
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(2, -1, 0);
        Point p4 = new Point(1, 1, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: build plane by 3 points
        assertDoesNotThrow(() -> new Plane(p1, p2, p4));

        // =============== Boundary Values Tests ==================
        // TC01: Test for the first and second points are same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1), "Failed constructing a plane");
        // TC02: Test for all point on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p3), "Failed constructing a plane");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // TC01: There is a simple single test here - using a quad
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> plane.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = plane.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertEquals(0d, result.dotProduct(p1.subtract(p3)), DELTA, "Triangle's normal is not orthogonal to one of the edges");
        assertEquals(0d, result.dotProduct(p1.subtract(p2)), DELTA, "Triangle's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testFindIntersections() {
        Plane plane = new Plane(new Point(1, 0, 0), new Point(1, 0, 1), new Point(1, 1, 1));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Test where ray starts outside the plain, isn't parallel, and creates a non-right angle, and intercepts the plane
        assertEquals(List.of(new Point(1, 1, 0)), plane.findIntersections(new Ray(Point.ZERO, new Vector(1, 1, 0))), "no 1 intersection");

        // TC02: Test where ray starts outside the plane, isn't parallel, and creates a non-right angle, and does not intercepts the plane
        assertNull(plane.findIntersections(new Ray(Point.ZERO, new Vector(-1, -1, 0))), "no 0 intersection");

        // =============== Boundary Values Tests ==================
        // TC11: Test checking if our ray is parallel to the plane, and also out of it
        assertNull(plane.findIntersections(new Ray(Point.ZERO, Vector.Z)), "no 0 intersection");
        // TC12: Test checking if our ray is on the plane
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 0), Vector.Z)), "no 0 intersection");

        // TC21: Ray is perpendicular to the plane
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 0), Vector.X)), "not 1 intersection");

        // TC22: The ray is before the plane
        assertEquals(List.of(new Point(1, 0, 0)), plane.findIntersections(new Ray(Point.ZERO, Vector.X)), "not 1 intersection");

        // TC22: The ray is after the plane
        assertEquals(List.of(new Point(1, 0, 0)), plane.findIntersections(new Ray(Point.ZERO, new Vector(2, 0, 0))), "not 1 intersection");

        // TC31: ray start on plane, not parallel, non right angle
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 1), new Vector(0.5, 0.5, 0))), "not 0 intersection");

        // TC41: same above + head is relative point of plane
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0.5, 0.5, 0))), "not 0 intersection");
    }
}