package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for primitives.Ray class
 */
public class RayTest {
    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     */
    @Test
    public void testGetPoint() {
        Point head = new Point(1, 2, 3);
        Ray ray = new Ray(head, Vector.X);

        // ============ Equivalence Partitions Tests ==============
        //TC01: positive distance
        assertEquals(new Point(3, 2, 3), ray.getPoint(2), "Positive distance test failed");

        //TC02: negative distance
        assertEquals(new Point(-1, 2, 3), ray.getPoint(-2), "Negative distance test failed");

        // =============== Boundary Values Tests ==================
        //TC03: distance = 0
        double tZero = 0;
        assertEquals(head, ray.getPoint(tZero), "Zero distance test failed");
    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(1, 0, 0), Vector.X);
        Point p1 = new Point(2, 0, 0),
                p2 = new Point(3, 0, 0),
                p3 = new Point(4, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: point in middle of list
        assertEquals(p1, ray.findClosestPoint(List.of(p2, p1, p3)), "closest point in middle of list");

        // =============== Boundary Values Tests ==================
        // TC11: empty list
        assertNull(ray.findClosestPoint(List.of()), "empty list");

        // TC12: first point is closest
        assertEquals(p1, ray.findClosestPoint(List.of(p1, p2, p3)), "closest point at list's start");

        // TC13: last point is closest
        assertEquals(p1, ray.findClosestPoint(List.of(p2, p3, p1)), "closest point in the end of list");
    }
}
