package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration test for Camera and geometries
 */
public class IntegrationTest {
    /**
     * The size of the view plane
     */
    private static final int WIDTH = 3, HEIGHT = 3;

    /**
     * The camera builder
     */
    private static final Camera.Builder builder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
            .setLocation(new Point(0, 0, 0.5))
            .setDirection(new Vector(0, 0, -1), Vector.Y)
            .setVpDistance(1)
            .setVpSize(WIDTH, HEIGHT);

    /**
     * Get the number of intersection points between the camera rays and the geometry
     *
     * @param geometry the geometry to check intersection points with
     * @param expected the expected number of intersection points
     */
    static void testRaysCount(Intersectable geometry, int expected) {
        Camera camera = builder.build();

        int i, j, count = 0;
        for (i = 0; i < HEIGHT; i++) {
            for (j = 0; j < WIDTH; j++) {
                // get a ray from camera and check the intersection points
                Ray r = camera.constructRay(WIDTH, HEIGHT, j, i);
                var intersections = geometry.findIntersections(r);
                if (intersections != null)
                    count += intersections.size();
            }
        }

        assertEquals(expected, count, "Wrong number of intersection points");
    }

    /**
     * Test method for {@link renderer.Camera#constructRay(int, int, int, int)} with {@link geometries.Sphere}
     * This is an integration test for Camera and Sphere
     */
    @Test
    void sphereTest() {
        // TC01: sphere is in front of the camera (2 points)
        Sphere sphere = new Sphere(new Point(0, 0, -3), 1.0);
        testRaysCount(sphere, 2);

        // TC02: sphere is in front of the camera (18 points)
        sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
        testRaysCount(sphere, 18);

        // TC03: sphere is in front of the camera (10 points)
        sphere = new Sphere(new Point(0, 0, -2), 2);
        testRaysCount(sphere, 10);

        // TC04: sphere is in front of the camera (9 points)
        sphere = new Sphere(new Point(0, 0, 1), 2);
        testRaysCount(sphere, 9);

        // TC05: sphere is behind the camera (0 points)
        sphere = new Sphere(new Point(0, 0, 1), 0.5);
        testRaysCount(sphere, 0);
    }

    /**
     * Test method for {@link renderer.Camera#constructRay(int, int, int, int)} with {@link geometries.Plane}
     * This is an integration test for Camera and Plane
     */
    @Test
    void planeTest() {
        // TC01: plane is in front of the camera (9 points)
        Plane plane = new Plane(new Point(2, 2, -2), new Point(1, 2, -2), new Point(2, 1, -2));
        testRaysCount(plane, 9);

        // TC02: plane is in front of the camera (9 points)
        plane = new Plane(new Point(0, 0, -0.5), new Vector(0, 1, -5));
        testRaysCount(plane, 9);

        // TC03: plane is in front of the camera (6 points)
        plane = new Plane(new Point(0, 0, -5), new Vector(0, 6, -5));
        testRaysCount(plane, 6);
    }

    /**
     * Test method for {@link renderer.Camera#constructRay(int, int, int, int)} with {@link geometries.Triangle}
     * This is an integration test for Camera and Triangle
     */
    @Test
    void triangleTest() {
        // TC01: triangle is in front of the camera (1 point)
        Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        testRaysCount(triangle, 1);

        // TC02: triangle is in front of the camera (2 points)
        triangle = new Triangle(new Point(1, -1, -2), new Point(-1, -1, -2), new Point(0, 20, -2));
        testRaysCount(triangle, 2);
    }
}
