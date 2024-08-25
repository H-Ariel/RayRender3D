package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Unit tests for geometries.Geometries class
 */
public class GeometriesTests {
    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Geometries geometries = new Geometries(
                new Plane(new Point(5, 2, 2), Vector.X),
                new Sphere(new Point(2, 0, 0), 1d),
                new Triangle(new Point(4, -2, -1), new Point(4, 2, -1), new Point(4, 0, 1))
        );

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some of the geometries have intersections
        assertEquals(2, geometries.findIntersections(new Ray(new Point(3.5, 0, 0), Vector.X)).size(), "Some of the geometries have intersections");

        // =============== Boundary Values Tests =================
        // TC04: Empty geometries list
        assertNull(new Geometries().findIntersections(new Ray(new Point(4.5, 0, 0), Vector.X)), "Empty geometries list");

        // TC05: None of the geometries have intersections
        assertNull(geometries.findIntersections(new Ray(new Point(0.5, 0, 0), Vector.Y)), "None of the geometries have intersections");

        // TC06: Only one geometry has intersections
        assertEquals(1, geometries.findIntersections(new Ray(new Point(4.5, 0, 0), Vector.X)).size(), "Only one geometry has intersections");

        // TC07: All geometries have intersections
        assertEquals(4, geometries.findIntersections(new Ray(new Point(0.5, 0, 0), Vector.X)).size(), "All geometries have intersections");
    }


    /**
     * Test method for {@link geometries.Geometries#findGeoIntersections(primitives.Ray, double)}.
     */
    @Test
    void testFindGeoIntersections() {
        Geometries geometries = new Geometries(
                new Plane(new Point(5, 2, 2), Vector.X),
                new Sphere(new Point(2, 0, 0), 1d),
                new Triangle(new Point(4, -2, -1), new Point(4, 2, -1), new Point(4, 0, 1))
        );

        // TC01: Some of the geometries have intersections within max distance
        assertEquals(2, geometries.findGeoIntersections(new Ray(new Point(3.5, 0, 0), Vector.X), 4.0).size(), "Some of the geometries have intersections within max distance");

        // TC02: No intersections within max distance
        assertNull(geometries.findGeoIntersections(new Ray(new Point(3.5, 0, 0), Vector.X), 0.2), "No intersections within max distance");

        // TC03: One intersection within max distance
        assertEquals(1, geometries.findGeoIntersections(new Ray(new Point(4.5, 0, 0), Vector.X), 2.0).size(), "One intersection within max distance");

        // TC04: All intersections within max distance
        assertEquals(4, geometries.findGeoIntersections( new Ray(new Point(0.5, 0, 0), Vector.X), 6).size(), "All intersections within max distance");

        // TC05: Some intersections within max distance
        assertEquals(3, geometries.findGeoIntersections(new Ray(new Point(0.5, 0, 0), Vector.X), 4).size(), "Some intersections within max distance");
    }

}
