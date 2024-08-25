package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for primitives.Point class
 *
 * @author Ariel
 */
class PointTest {
    /**
     * Point for tests
     */
    private static final Point p1 = new Point(1, 2, 3);
    /**
     * Point for tests
     */
    private static final Point p2 = new Point(2, 4, 6);
    /**
     * Point for tests
     */
    private static final Point p3 = new Point(2, 4, 5);
    /**
     * Vector for tests
     */
    private static final Vector v1 = new Vector(1, 2, 3);

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private static final double DELTA = 0.00001;


    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: add vector to point
        assertEquals(p1.add(v1), p2, "(point + vector) = other point does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: subtract between two points
        assertEquals(p2.subtract(p1), v1, "(point2 - point1) does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC01: subtract from itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "(point - itself) does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void distance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: distance between two points
        assertEquals(3, p1.distance(p3), DELTA, "distance between points is wrong");

        // =============== Boundary Values Tests ==================
        // TC01: distance to itself
        assertEquals(0, p1.distance(p1), DELTA, "point distance to itself is not zero");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void distanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: distance squared between two points
        assertEquals(9, p1.distanceSquared(p3), DELTA, "squared distance between points is wrong");

        // =============== Boundary Values Tests ==================
        // TC01: distance squared to itself
        assertEquals(0, p1.distanceSquared(p1), DELTA, "point squared distance to itself is not zero");
    }
}