package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for geometries.Cylinder class
 *
 * @author Ariel and Asaf
 */
class CylinderTest {
    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void getNormal() {
        Cylinder cylinder = new Cylinder(new Ray(Point.ZERO, Vector.Z), 1, 3);

        // ============ Equivalence Partitions Tests ==============

        // TC01: The point is on the side of the cylinder
        assertEquals(Vector.Y, cylinder.getNormal(new Point(0, 1, 2)), "Bad normal to cylinder-1");

        // TC02: The point is on the top of the cylinder
        assertEquals(Vector.Z, cylinder.getNormal(new Point(0.5, 0.5, 3)), "Bad normal to cylinder-2");

        // TC03: The point is on the bottom of the cylinder
        assertEquals(Vector.Z, cylinder.getNormal(new Point(0.5, 0.5, 0)), "Bad normal to cylinder-3");

        // =============== Boundary Values Tests ==================
        // TC11: The point is on cylinder's bottom center
        assertEquals(Vector.Z, cylinder.getNormal(Point.ZERO), "Bad normal to cylinder's bottom center");

        // TC12: The point is on cylinder's top center
        assertEquals(Vector.Z, cylinder.getNormal(new Point(0, 0, 3)), "Bad normal to cylinder's top center");

        // TC13: The point is on the boundary between the side and the top
        assertEquals(Vector.Z, cylinder.getNormal(new Point(0, 1, 3)), "Bad normal to cylinder's boundary between the side and the top");

        // TC14: The point is on the boundary between the side and the bottom
        assertEquals(Vector.Z, cylinder.getNormal(new Point(0, 1, 0)), "Bad normal to cylinder's boundary between the side and the bottom");
    }

    /**
     * Test method for {@link geometries.Cylinder#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // TODO: check cases. consider we have TubeTest
    }
}