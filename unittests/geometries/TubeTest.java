package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for geometries.Tube class
 *
 * @author Ariel and Asaf
 */
class TubeTest {
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        assertEquals(Vector.Y, new Tube(new Ray(Point.ZERO, Vector.Z), 1).getNormal(new Point(0, 1, 0)), "Bad normal to tube");

        // =============== Boundary Values Tests ==================
        // TC02: (P-P0) is orthogonal to the ray's direction
        assertEquals(Vector.Y, new Tube(new Ray(Point.ZERO, Vector.X), 1).getNormal(new Point(0, 1, 0)), "Bad normal to tube");
    }

    /**
     * Test method for {@link geometries.Tube#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // based on https://github.com/DanielElbaz/Mini-Project-in-Java/blob/master/unitTests/geometries/TubeTests.java

        Tube tube = new Tube(new Ray(new Point(1, 0, 0), Vector.Z), 1);

        Point p110 = new Point(1, 1, 0);
        Point p200 = new Point(2, 0, 0);
        Point p1m10 = new Point(1, -1, 0);
        Vector v1m10 = new Vector(1, -1, 0);
        Vector v0m10 = new Vector(0, -1, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the tube (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(3, 2, 0), new Vector(0, 1, 0))), "ray doesn't the tube direction");

        // TC02: Ray starts before and cross the tube (2 points)
        assertEquals(List.of(p110, p200), tube.findIntersections(new Ray(new Point(0, 2, 0), v1m10)), "Ray starts before and cross the tube");

        // TC03: Ray starts inside the tube (1 point)
        assertEquals(List.of(p200), tube.findIntersections(new Ray(new Point(1.5, 0.5, 0), v1m10)), "Ray starts inside the tube");

        // TC04: Ray starts after the tube
        assertNull(tube.findIntersections(new Ray(new Point(3, -1, 0), v1m10)), "Ray starts after the tube");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the tube (but not the axis ray)
        // TC11: Ray starts at tube and goes inside (1 point)
        assertEquals(List.of(p200), tube.findIntersections(new Ray(p110, v1m10)), "Ray starts at tube and goes inside");

        // TC12: Ray starts at tube and goes outside (0 points)
        assertNull(tube.findIntersections(new Ray(p200, v1m10)), "Ray starts at tube and goes outside");

        // **** Group: Ray's line goes through the axis ray
        // TC13: Ray starts before the tube (2 points)
        assertEquals(List.of(p110, p1m10), tube.findIntersections(new Ray(new Point(1, 3, 0), v0m10)), "Ray starts before the tube");

        // TC14: Ray starts at tube and goes inside (1 point)
        assertEquals(List.of(p1m10), tube.findIntersections(new Ray(p110, v0m10)), "Ray starts at tube and goes inside");

        // TC15: Ray starts inside (1 point)
        assertEquals(List.of(p1m10), tube.findIntersections(new Ray(new Point(1, 0.5, 0), v0m10)), "Ray starts inside");

        // TC16: Ray starts at the axis ray (1 point)
        assertEquals(List.of(p1m10), tube.findIntersections(new Ray(new Point(1, 0, 0), v0m10)), "Ray starts at the axis ray");

        // TC17: Ray starts at tube and goes outside (0 points)
        assertNull(tube.findIntersections(new Ray(p1m10, v1m10)), "Ray starts at tube and goes outside");

        // TC18: Ray starts after tube (0 points)
        assertNull(tube.findIntersections(new Ray(new Point(1, -2, 0), v1m10)), "Ray starts after tube");

        // **** Group: Ray's line is tangent to the tube (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(tube.findIntersections(new Ray(new Point(0, 1, 1), Vector.X)), "Ray starts before the tangent point");

        // TC20: Ray starts at the tangent point
        assertNull(tube.findIntersections(new Ray(new Point(1, 1, 1), Vector.X)), "Ray starts at the tangent point");

        // TC21: Ray starts after the tangent point
        assertNull(tube.findIntersections(new Ray(new Point(3, 1, 1), Vector.X)), "Ray starts after the tangent point");

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to tube's axis point
        assertNull(tube.findIntersections(new Ray(new Point(1, 3, 0), Vector.X)), "Ray's line is outside, ray is orthogonal to ray start to tube's axis point");

        // TC23: Ray contained in tube boundary
        assertNull(tube.findIntersections(new Ray(new Point(1, 1, 1), Vector.Z)), "Ray contained in tube boundary");
    }
}