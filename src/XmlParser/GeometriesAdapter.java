package XmlParser;

import geometries.*;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * XML Adapter for Geometries objects.
 * Used because there are some fields we can calculate while
 * loading file (so we don't need store them in the XML file).
 */
public class GeometriesAdapter extends XmlAdapter<GeometriesAdapter.GeometriesValue, Geometries> {
    /**
     * Default material for geometries
     */
    static final Material defaultMaterial = new Material();

    /**
     * Default emission for geometries
     */
    static final Color defaultEmission = Color.BLACK;

    @Override
    public Geometries unmarshal(GeometriesAdapter.GeometriesValue v) {
        return new Geometries(v.geometries.stream().map(i -> (Intersectable) (
                switch (i) {
                    case XmlSphere sphere -> new Sphere(sphere.center, sphere.radius);
                    case XmlPlane plane -> new Plane(plane.q, plane.normal);
                    case XmlPolygon polygon -> new Polygon(polygon.vertices.toArray(new Point[0]));
                    case null, default -> throw new IllegalArgumentException("Unknown type of geometry");
                }).setEmission(i.emission).setMaterial(v.materials.getOrDefault(i.materialName, defaultMaterial))
        ).toList());
    }

    @Override
    public GeometriesAdapter.GeometriesValue marshal(Geometries v) {
        GeometriesAdapter.GeometriesValue xmlGeometries = new GeometriesAdapter.GeometriesValue();

        for (Intersectable i : v.getGeometries()) {
            if (i instanceof Geometry g) {
                XmlGeometry geometry = switch (g) {
                    case Sphere sphere -> new XmlSphere(sphere);
                    case Plane plane -> new XmlPlane(plane);
                    case Polygon polygon -> new XmlPolygon(polygon);
                    default -> throw new IllegalArgumentException("Unknown type of geometry");
                };

                geometry.emission = g.getEmission();
                geometry.materialName = g.getMaterial().toString();

                xmlGeometries.geometries.add(geometry);

                if (!xmlGeometries.materials.containsKey(geometry.materialName))
                    xmlGeometries.materials.put(geometry.materialName, g.getMaterial());

            } else throw new IllegalArgumentException("Unknown type of geometry");
        }

        return xmlGeometries;
    }

    /**
     * XML Geometry class
     */
    @XmlRootElement
    static public class XmlGeometry {
        /**
         * The emission color
         */
        @XmlJavaTypeAdapter(ColorAdapter.class)
        public Color emission = Color.BLACK;

        /**
         * The material name
         */
        @XmlElement
        public String materialName;
    }

    /**
     * XML Sphere class
     */
    @XmlRootElement
    static class XmlSphere extends XmlGeometry {
        /**
         * The center of the sphere
         */
        @XmlJavaTypeAdapter(PointAdapter.class)
        public Point center;

        /**
         * The radius of the sphere
         */
        @XmlElement
        public double radius;

        /**
         * Default constructor for XmlSphere
         */
        @SuppressWarnings({"unused", "for xml"})
        private XmlSphere() {
            center = null;
            radius = 0;
        }

        /**
         * Constructor for XmlSphere
         *
         * @param sphere the sphere to adapt
         */
        public XmlSphere(Sphere sphere) {
            this.center = sphere.getCenter();
            this.radius = sphere.getRadius();
        }
    }

    /**
     * XML Plane class
     */
    @XmlRootElement
    static class XmlPlane extends XmlGeometry {
        /**
         * The reference point on the plane
         */
        @XmlJavaTypeAdapter(PointAdapter.class)
        public Point q;
        /**
         * The normal to the plane
         */
        @XmlJavaTypeAdapter(VectorAdapter.class)
        public Vector normal;

        /**
         * Default constructor for XmlPlane
         */
        @SuppressWarnings({"unused", "for xml"})
        private XmlPlane() {
        }

        /**
         * Constructor for XmlPlane
         *
         * @param plane the plane to adapt
         */
        public XmlPlane(Plane plane) {
            this.q = plane.getQ();
            this.normal = plane.getNormal();
        }
    }

    /**
     * XML Polygon class
     */
    @XmlRootElement
    static class XmlPolygon extends XmlGeometry {
        /**
         * The vertices of the polygon
         */
        @XmlElement(name = "point")
        @XmlJavaTypeAdapter(PointAdapter.class)
        public List<Point> vertices;

        /**
         * Default constructor for XmlPolygon
         */
        @SuppressWarnings({"unused", "for xml"})
        private XmlPolygon() {
        }

        /**
         * Constructor for XmlPolygon
         *
         * @param polygon the polygon to adapt
         */
        public XmlPolygon(Polygon polygon) {
            this.vertices = polygon.getVertices();
        }
    }

    /**
     * XML GeometriesValue class
     */
    @XmlRootElement
    static public class GeometriesValue {
        /**
         * The geometries list
         */
        @XmlElementWrapper(name = "geometries")
        @XmlElements({
                @XmlElement(name = "Plane", type = XmlPlane.class),
                @XmlElement(name = "Polygon", type = XmlPolygon.class),
                @XmlElement(name = "Sphere", type = XmlSphere.class)
        })
        public List<GeometriesAdapter.XmlGeometry> geometries = new LinkedList<>();

        /**
         * The materials list
         */
        @XmlElement
        public Map<String, Material> materials = new HashMap<>();
    }
}
