package renderer;

import geometries.Geometries;
import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import java.util.List;


/**
 * Test for glossy and diffused reflection
 */
public class GlossyDiffusedTest {
    /**
     * The scene for the tests
     */
    final Scene scene = new Scene("GlossyDiffusedTest")
            .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15))
            .setBackground(new Color(150, 150, 200))
            .setGeometries(new Geometries(
                    // add red sphere
                    new Sphere(new Point(-100, 50, 0), 90)
                            .setEmission(new Color(java.awt.Color.RED))
                            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),
                    // add little green sphere behind the red sphere
                    new Sphere(new Point(40, 80, 50), 70)
                            .setEmission(new Color(java.awt.Color.GREEN))
                            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100))
            ))
            .setLights(List.of(new DirectionalLight(new Color(java.awt.Color.WHITE), new Vector(3, 1, 0))));

    /**
     * The camera for the tests
     */
    final Camera.Builder camera = Camera.getBuilder()
            .setDirection(Vector.Y, Vector.Z)
            .setLocation(new Point(0, -400, 0))
            .setVpSize(200, 200)
            .setVpDistance(100)
            .setRayTracer(new SimpleRayTracer(scene, 17));

    /**
     * Test glossy reflection
     */
    @Test
    void testGlossy() {
        // add rectangle so the sphere will be reflected
        scene.geometries.add(new Plane(new Point(0, 0, -90), Vector.Z)
                .setEmission(new Color(java.awt.Color.BLUE))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKr(0.5).setKg(50))
        );

        camera.setImageWriter(new ImageWriter("Glossy - Test 1", 500, 500))
                .build().renderImage().writeToImage();
    }

    /**
     * Test diffused reflection
     */
    @Test
    void testDiffused() {
        // add rectangle to hide the sphere (blurry reflection)
        scene.geometries.add(new Polygon(
                new Point(-100, -50, 200),
                new Point(-100, -50, -200),
                new Point(200, -50, -200),
                new Point(200, -50, 200))
                .setEmission(new Color(java.awt.Color.BLUE))
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKt(0.5).setKb(50))
        );

        camera.setImageWriter(new ImageWriter("Diffused - Test 1", 500, 500))
                .build().renderImage().writeToImage();
    }
}
