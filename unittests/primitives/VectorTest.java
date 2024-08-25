package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for primitives.Vector class
 *
 * @author Asaf
 */
class VectorTest {
    /**
     * Vector for tests
     */
    private static final Vector v1 = new Vector(1, 2, 3);
    /**
     * Vector for tests
     */
    private static final Vector v1Opposite = new Vector(-1, -2, -3);
    /**
     * Vector for tests
     */
    private static final Vector v2 = new Vector(-2, -4, -6);
    /**
     * Vector for tests
     */
    private static final Vector v3 = new Vector(0, 3, -2);
    /**
     * Vector for tests
     */
    private static final Vector v4 = new Vector(1, 2, 2);

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private static final double DELTA = 0.00001;

    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}.
     * and {@link primitives.Vector#Vector(primitives.Double3)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test constructor with a point equals to constructor with three numbers
        assertEquals(new Vector(new Double3(1, 2, 3)), v1, "ERROR: constructors are not equal");

        // =============== Boundary Values Tests ==================
        // TC11: test the constructor with zero coordinates
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "ERROR: zero vector does not throw an exception");
        // TC12: test the constructor with zero Double3
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO), "ERROR: zero vector does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: add vector to vector
        assertEquals(new Vector(2, 4, 5), v1.add(v4), "ERROR: addition of vectors does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: addition of opposite vectors
        assertEquals(v1Opposite, v1.add(v2), "ERROR: addition of opposite vectors does not work correctly");
        // TC12: addition of vector to its opposite
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1Opposite), "ERROR: Vector + -itself does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     * inherited from {@link primitives.Point}
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: subtract vector from vector
        assertEquals(Vector.Z, v1.subtract(v4), "ERROR: subtraction of vectors does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: subtract vector from itself
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1), "ERROR: Vector - itself does not throw an exception");
        // TC12: subtract vectors in the same direction
        assertEquals(new Vector(-3, -6, -9), v2.subtract(v1), "ERROR: subtraction of vectors in the same direction does not work correctly");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void scalarMultiply() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: positive scalar
        assertEquals(new Vector(2, 4, 6), v1.scale(2), "ERROR: positive scalar multiplication does not work correctly");
        // TC02: negative scalar
        assertEquals(v2, v1.scale(-2), "ERROR: negative scalar multiplication does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: scalar = 0
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "ERROR: scalar = 0 does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void dotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: dot product of two vectors
        assertEquals(11, v1.dotProduct(v4), DELTA, "ERROR: dot product of vectors does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: dot product of orthogonal vectors
        assertEquals(0, v1.dotProduct(v3), "ERROR: dot product of orthogonal vectors does not work correctly");
        // TC12: vector of unit size with other vector
        assertEquals(1, v1.dotProduct(Vector.X), "ERROR: dot product of unit vector does not work correctly");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void crossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: cross product of two vectors
        assertEquals(new Vector(-2, 1, 0), v1.crossProduct(v4), "ERROR: cross product of vectors does not work correctly");

        // =============== Boundary Values Tests ==================
        // TC11: cross product of vectors in the same direction
        assertThrows(IllegalArgumentException.class, () -> v1Opposite.crossProduct(v2), "ERROR: cross product of parallel vectors does not throw an exception");
        // TC12: cross product of opposite vectors
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v2), "ERROR: cross product of opposite vectors does not work correctly");
        // TC13: cross product of opposite vectors and equals
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1Opposite), "ERROR: cross product of opposite vectors does not work correctly");
        // TC14: equals vectors
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1), "ERROR: cross product of opposite vectors does not work correctly");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void lengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: single case to vector's length squared value
        assertEquals(9, v4.lengthSquared(), DELTA, "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void length() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: single case to vector's length value
        assertEquals(3, v4.length(), "ERROR: length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void normalize() {
        Vector xAxis = Vector.X;
        // ============ Equivalence Partitions Tests ==============
        // TC01: single case to vector's normalization
        assertEquals(1, v1.normalize().length(), DELTA, "ERROR: the normalized vector is not a unit vector");

        // =============== Boundary Values Tests ==================
        // TC11: unit vector (which already normalized)
        assertEquals(xAxis, xAxis.normalize(), "ERROR: the normalized vector is not a unit vector");
    }

    /**
     * Test method for {@link primitives.Vector#makePerpendicularVector()}.
     */
    @Test
    void makePerpendicularVector() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: single case to vector's perpendicular vector
        Vector v = new Vector(1, 1, 0);
        Vector vPerpendicular = v.makePerpendicularVector();
        assertEquals(0, vPerpendicular.dotProduct(v), "ERROR: the perpendicular vector is not perpendicular to the original vector");

        // =============== Boundary Values Tests ==================
        // TC11: vector with x = 0 and y = 0
        v = Vector.Z;
        vPerpendicular = v.makePerpendicularVector();
        assertEquals(0, vPerpendicular.dotProduct(v), "ERROR: the perpendicular vector is not perpendicular to the original vector");
    }

    /**
     * Test method for {@link primitives.Vector#reflect(primitives.Vector)}.
     */
    @Test
    public void testReflect() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(new Vector(1, -2, 3), v1.reflect(Vector.Y), "Reflection across the y-axis should be correct.");
    }
}