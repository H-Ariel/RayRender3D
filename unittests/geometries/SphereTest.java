package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for geometries.Sphere class
 *
 * @author Ariel and Asaf
 */
class SphereTest {
    /**
     * sample point for test
     */
    private static final Point p100 = new Point(1, 0, 0);

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void getNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        assertEquals(Vector.X, new Sphere(Point.ZERO, 1).getNormal(p100), "wrong normal vector for sphere - x axis");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    void findGeoIntersectionsHelper() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01,  new Vector(1, 1, 0))), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01,  new Vector(3, 1, 0)))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(p01)))
                .toList();
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(List.of(gp1, gp2), result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        //assertEquals(1, sphere.findIntersections(ray).size(), "no 1 intersection");
        assertEquals(List.of(new Point(1.7071067811865475, 0.5, 0.5)),
                sphere.findIntersections( new Ray(new Point(0.5, 0.5, 0.5), Vector.X)), "no 1 intersection");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-2, 0, 0), new Vector(-1, 0, 0))), "no 0 intersection");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        assertEquals(List.of(new Point(1.9348469228349534, 0.355051025721682, 0.0)),
                sphere.findIntersections(new Ray(gp1, Vector.X)), "not 1 intersection");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(gp1, new Vector(-1, 0, 0))), "not 0 intersection");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        assertEquals(List.of(Point.ZERO, new Point(2, 0, 0)),
                sphere.findIntersections(new Ray(new Point(-1, 0, 0), Vector.X)), "not 2 intersection");

        // TC14: Ray starts at sphere and goes inside (1 point)
        assertEquals(List.of(new Point(1, -1, 0)),
                sphere.findIntersections(new Ray(new Point(1, 1, 0), new Vector(0, -1, 0))), "not 1 intersection");

        // TC15: Ray starts inside (1 point)
        assertEquals(List.of(new Point(1, -1, 0)),
                sphere.findIntersections(new Ray(new Point(1, 0.5, 0), new Vector(0, -1, 0))), "not 1 intersection");

        // TC16: Ray starts at the center (1 point)
        assertEquals(List.of(new Point(1, 1, 0)),
                sphere.findIntersections(new Ray(new Point(1, 0, 0), Vector.Y)), "not 1 intersection");

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 1, 0), Vector.Y)), "not 0 intersection");

        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 2, 0), Vector.Y)), "not 0 intersection");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, -2, 0), Vector.Y)), "not 0 intersection");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(Point.ZERO, Vector.Y)), "not 0 intersection");

        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, 2, 0), Vector.Y)), "not 0 intersection");

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), Vector.Y)), "not 0 intersection");

        // TC23: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertEquals(List.of(new Point(0.5, 0.8660254037844386, 0.0)),
                sphere.findIntersections(new Ray(new Point(0.5, 0, 0), Vector.Y)), "not 0 intersection");
    }
}