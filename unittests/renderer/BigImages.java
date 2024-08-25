package renderer;

import XmlParser.XmlParser;
import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

/**
 * Test rendering a high resolution images with many geometries and lights
 */
public class BigImages {
    /**
     * test all the effects together and create a super image
     */
    @Test
    void multiTest1() {
        Scene scene = new Scene("Super Test")
                .setAmbientLight(new AmbientLight(new Color(0, 0, 127), new Double3(0.15)))
                .setBackground(new Color(64, 64, 255));

        // add spirals of spheres
        double R = 500; // radius of the spiral
        double N = 15;  // number of spheres in the spiral
        Color[] colors = {
                new Color(255, 0, 255),
                new Color(0, 0, 255),
                new Color(255, 255, 0),
                new Color(0, 255, 0),
                new Color(255, 0, 0)
        };
        Material mat = new Material().setKd(0.5).setKs(0.5).setShininess(100);
        Material mat2 = new Material().setKd(0.5).setKs(0.5).setShininess(30).setKr(0.15);
        double x, y, z;

        for (int i = 0; i < N * 3; i++) {
            x = Math.sqrt(R) * Math.cos(2 * Math.PI * i / N);
            y = Math.sqrt(R) * Math.sin(2 * Math.PI * i / N);
            z = i * 5;

            // add spheres in the spiral
            scene.geometries.add(
                    new Sphere(new Point(x, y, z), 5)
                            .setEmission(colors[i % colors.length]).setMaterial(mat2)
            );

            // add polygons around the spiral
            scene.geometries.add(
                    new Polygon(new Point(x, y, z), new Point(x + 10, y + 10, z), new Point(x + 10, y, z + 10), new Point(x, y - 10, z + 10))
                            .setEmission(colors[(i + 2) % colors.length]).setMaterial(mat)
            );

            R += 50; // increase the radius of the spiral
        }

        // base plane
        scene.geometries.add(new geometries.Plane(new Point(-1, -1, 0), new Point(1, -1, 0), new Point(1, 1, 0))
                .setEmission(new Color(50, 50, 50)).setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(50).setKr(0.01)));

        // bubble sphere around the spiral
        scene.geometries.add(new Sphere(Point.ZERO, 220)
                .setEmission(new Color(0, 0, 127)).setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKt(0.8)));

        // add lights
        scene.lights.add(
                new SpotLight(new Color(255, 128, 0), new Point(-250, 500, 500), new Vector(1, -1, -1))
                //.setShadowSoftness(3)
        );

        scene.geometries.makeBVH();

        Camera.Builder builder = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(scene));

        builder.setImageWriter(new ImageWriter("multiTest1-a", 1500, 1500))
                .setLocation(new Point(-700, 0, 150))
                .lookAt(new Point(0, 0, 50))
                .setVpSize(200, 200)
                .setVpDistance(300);

        builder.build().renderImage().writeToImage();

        builder.setImageWriter(new ImageWriter("multiTest1-b", 1500, 1500))
                .setLocation(new Point(0, 0, 150))
                .lookAt(Point.ZERO)
                .setVpSize(500, 500)
                .setVpDistance(50);

        //    builder.build().renderImage().writeToImage();
    }

    /**
     * test all the effects together and create an amazing image
     */
    @Test
    void multiTest2() {
        // Build a pyramid
        Geometry side1 = new Triangle(Point.ZERO, new Point(100, 0, 0), new Point(50, 50, 100))
                .setEmission(new Color(0, 0, 0)).setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(60));
        Geometry side2 = new Triangle(new Point(100, 0, 0), new Point(50, 100, 0), new Point(50, 50, 100))
                .setEmission(new Color(0, 0, 0)).setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(60));
        Geometry side3 = new Triangle(new Point(50, 100, 0), Point.ZERO, new Point(50, 50, 100))
                .setEmission(new Color(0, 0, 0)).setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(60));

        Geometry base = new Plane(new Point(0, 1, 0), new Point(1, 0, 0), new Point(1, 1, 0))
                .setEmission(new Color(0, 127, 0)).setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(60).setKr(0.4));

        Geometry sphere = new Sphere(new Point(80, 200, 100), 25)
                .setEmission(new Color(127, 0, 200)).setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(60));

        Scene scene = new Scene("Amazing image")
                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.3)));

        scene.geometries.add(side1, side2, side3, base, sphere);

        // add light-blue sphere around all the scene
        scene.geometries.add(new Sphere(Point.ZERO, 1000)
                .setEmission(new Color(0, 0, 127)).setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKr(0.5)));

        scene.lights.add(new SpotLight(new Color(500, 300, 0), new Point(50, 50, 150), new Vector(0, 0, -1)).setKl(0.0001).setKq(0.000005));

        Camera cam = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(scene))
                .setLocation(new Point(-700, -700, 30))
                .setDirection(new Vector(1, 1, 0), Vector.Z)
                .setVpSize(200, 200).setVpDistance(1000)
                .setImageWriter(new ImageWriter("multiTest2", 1000, 1000))
                .build();

        //cam.renderImage().writeToImage();

        XmlParser.saveSceneToXml(scene, "./xml/multiTest2.xml");
    }

    /**
     * same to multiTest2 but load the scene from xml
     */
    @Test
    void loadMultiTest2() {
        Scene scene = XmlParser.loadSceneFromXml("./xml/multiTest2.xml");
        Camera cam = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(scene))
                .setLocation(new Point(-700, -700, 30))
                .setDirection(new Vector(1, 1, 0), Vector.Z)
                .setVpSize(200, 200).setVpDistance(1000)
                .setImageWriter(new ImageWriter("multiTest2-xml", 1000, 1000))
                .build();
        cam.renderImage().writeToImage();
    }

    /**
     * test to render a sunset boat
     */
    @Test
    void sunsetBoat() {
        Scene scene = new Scene("Sunset Boat")
                .setAmbientLight(new AmbientLight(new Color(BLACK), new Double3(0.3)));//.setBackground(new Color(255, 127, 0));

        Sphere sky = (Sphere) new Sphere(Point.ZERO, 1000)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100))
                .setEmission(new Color(255, 127, 0));

        Plane sea = (Plane) new Plane(Point.ZERO, Vector.Z)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKr(0.5).setKg(10))
                .setEmission(new Color(0, 0, 191));

        Sphere sun = (Sphere) new Sphere(new Point(0, 300, 0), 200)
                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKt(0.5))
                .setEmission(new Color(200, 127, 0));
        PointLight sunLight = new PointLight(
                new Color(255, 255, 0), new Point(0, 300, 0)).setKl(0.001).setKq(0.05);

        Material boatMat = new Material().setKd(0.5).setKs(0.5).setShininess(100);
        Color boatColor = Color.BLACK;

        Geometries boat = new Geometries(
                new Polygon(
                        new Point(-13, 0, 0),
                        new Point(13, 0, 0),
                        new Point(20, 0, 10),
                        new Point(-20, 0, 10)
                ).setMaterial(boatMat).setEmission(boatColor),
                new Polygon(
                        new Point(-1, 0, 10),
                        new Point(1, 0, 10),
                        new Point(1, 0, 30),
                        new Point(-1, 0, 30)
                ).setMaterial(boatMat).setEmission(boatColor),

                new Polygon(
                        new Point(1, 0, 30),
                        new Point(1, 0, 15),
                        new Point(17, 0, 15)
                ).setMaterial(boatMat).setEmission(boatColor)
        );

        scene.geometries.add(sky, sea, boat, sun);
        scene.geometries.makeCBR();
        scene.lights.add(sunLight);

        Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(scene))
                .setLocation(new Point(0, -200, 5))
                .setDirection(Vector.Y, Vector.Z)
                .setVpSize(400, 400).setVpDistance(300)
                .setImageWriter(new ImageWriter("sunset-boat", 500, 500))
                .setMultithreading(10)
                .build()
                .renderImage().writeToImage();
    }

    @Test
    void building() {
        Scene scene = new Scene("Building")
                .setAmbientLight(new AmbientLight(new Color(BLACK), new Double3(0.3)))
                .setBackground(new Color(0, 127, 255));

        Material buildingMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(100);
        Color buildingColor = new Color(127, 127, 127);

        double columnRadius = 10;
        double columnHeight = 150;
        int columnsPerSide = 8;
        double spacing = 50;
        double baseWidth = (columnsPerSide - 1) * spacing;
        double baseLength = (columnsPerSide + 2) * spacing;

        double baseWidth2 = baseWidth / 2;
        double baseLength2 = baseLength / 2;

        // Create columns
        for (int i : new int[]{0, columnsPerSide - 1}) {
            for (int j = 0; j < columnsPerSide + 2; j++) {
                double x = -baseWidth2 + i * spacing;
                double y = -baseLength2 + j * spacing;
                scene.geometries.add(
                        new Cylinder(new Ray(new Point(x, y, 0), Vector.Z), columnRadius, columnHeight)
                                .setMaterial(buildingMaterial)
                                .setEmission(buildingColor)
                );
            }
        }

        // Create the base (floor)
        scene.geometries.add(
                new Polygon(
                        new Point(-baseWidth2 - spacing, -baseLength2 - spacing, 0),
                        new Point(baseWidth2 + spacing, -baseLength2 - spacing, 0),
                        new Point(baseWidth2 + spacing, baseLength2 + spacing, 0),
                        new Point(-baseWidth2 - spacing, baseLength2 + spacing, 0)
                ).setMaterial(buildingMaterial)
                        .setEmission(buildingColor)
        );

        Point p1 = new Point(-baseWidth2 - columnRadius, -baseLength2 - columnRadius, columnHeight);
        Point p2 = new Point(baseWidth2 + columnRadius, -baseLength2 - columnRadius, columnHeight);
        Point p3 = new Point(baseWidth2 + columnRadius, baseLength2 + columnRadius, columnHeight);
        Point p4 = new Point(-baseWidth2 - columnRadius, baseLength2 + columnRadius, columnHeight);
        Point p5 = new Point(0, -baseLength2 - columnRadius, columnHeight + 70);
        Point p6 = new Point(0, baseLength2 + columnRadius, columnHeight + 70);

        // add roof
        scene.geometries.add(
                // base
                new Polygon(p1, p2, p3, p4).setMaterial(buildingMaterial).setEmission(buildingColor),
                // add triangles (front and back)
                new Triangle(p1, p2, p5).setMaterial(buildingMaterial).setEmission(buildingColor),
                new Triangle(p3, p4, p6).setMaterial(buildingMaterial).setEmission(buildingColor),
                // add polygons (sides)
                new Polygon(p1, p5, p6, p4).setMaterial(buildingMaterial).setEmission(buildingColor),
                new Polygon(p2, p5, p6, p3).setMaterial(buildingMaterial).setEmission(buildingColor)
        );

        // add plane for ground
        scene.geometries.add(new Plane(Point.ZERO, Vector.Z)
                .setEmission(new Color(25, 75, 25))
                .setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(50)));

        scene.geometries.makeBVH();

        // Add lighting
//        scene.lights.add(new PointLight(
//                new Color(255, 255, 255), new Point(-100, -100, 10*columnHeight))
//                .setKl(0.000001).setKq(0.000005));
        scene.lights.add(new DirectionalLight(new Color(155, 155, 155), new Vector(1, 1, -0.5)));

        // Camera setup
        Camera.Builder cam = Camera.getBuilder()
                .setRayTracer(new SimpleRayTracer(scene))
                .setImageWriter(new ImageWriter("building", 1000, 1000))
                .setVpSize(300, 300).setVpDistance(370);

        cam.setLocation(new Point(-400, -800, 250))
                //.setDirection(new Vector(0, 1, -0.15), new Vector(0,0.15,1))
                .setDirection(new Vector(1, 2, -0.6), new Vector(1, 2, 5 / 0.6))
                .build().renderImage().writeToImage();
    }

}
