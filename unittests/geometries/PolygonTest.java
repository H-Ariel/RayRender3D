package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Polygons
 *
 * @author Dan
 */
public class PolygonTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts = {
                new Point(0, 0, 1), new Point(1, 0, 0),
                new Point(0, 1, 0), new Point(-1, 1, 1)
        };
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)),
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)),
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)),
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class,
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");
    }

    /**
     * Test method for
     * {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        final Polygon polygon = new Polygon(
                new Point(1, 0, 0),
                new Point(1, 1, 0),
                new Point(1, 1, 1),
                new Point(1, 0, 1)
        );
        final Point p550 = new Point(0.5, 0.5, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is inside the polygon (1 point)
        assertEquals(List.of(new Point(1.0, 0.75, 0.25)), polygon.findIntersections(new Ray(p550, new Vector(2, 1, 1))), "Ray's line is inside the polygon");

        // TC02: Ray's line is outside the polygon's edges (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 5, 1))), "Ray's line is outside the polygon's edges");

        // TC03: Ray's line is on the polygon's vertices (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(0.25, 0.5, 0.5), new Vector(1, 2, 2))), "Ray's line is on the polygon's vertices");

        // =============== Boundary Values Tests ==================

        // TC11: Ray's line is on the polygon's edge (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(0.5, 0.25, 0), new Vector(2, 1, 0))), "Ray's line is on the polygon's edge");

        // TC12: Ray's line is on the polygon's vertex (0 points)
        assertNull(polygon.findIntersections(new Ray(p550, new Vector(1, 1, 0))), "Ray's line is on the polygon's vertex");

        // TC13: Ray's line is on the continuation of the polygon's edge (0 points)
        assertNull(polygon.findIntersections(new Ray(p550, new Vector(1, 2, 0))), "Ray's line is on the continuation of the polygon's edge");
    }
}
