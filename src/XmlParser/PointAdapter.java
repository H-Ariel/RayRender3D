package XmlParser;

import primitives.Point;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Class to adapt a point to XML
 */
public class PointAdapter extends XmlAdapter<PointAdapter.PointValue, Point> {
    @Override
    public Point unmarshal(PointValue v) {
        return new Point(v.x, v.y, v.z);
    }

    @Override
    public PointValue marshal(Point v) {
        return new PointValue(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Class to represent a point value
     */
    public static class PointValue {
        /**
         * The x value
         */
        @XmlAttribute
        public double x;

        /**
         * The y value
         */
        @XmlAttribute
        public double y;

        /**
         * The z value
         */
        @XmlAttribute
        public double z;

        /**
         * Default constructor for PointValue
         */
        @SuppressWarnings({"unused", "for xml"})
        public PointValue() {
            x = y = z = 0;
        }

        /**
         * Constructor for PointValue
         *
         * @param r the x value
         * @param g the y value
         * @param b the z value
         */
        public PointValue(double r, double g, double b) {
            this.x = r;
            this.y = g;
            this.z = b;
        }
    }
}
