package XmlParser;

import primitives.Vector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Class to adapt a vector to XML
 */
public class VectorAdapter extends XmlAdapter<VectorAdapter.VectorValue, Vector> {
    @Override
    public Vector unmarshal(VectorValue v) {
        return new Vector(v.x, v.y, v.z);
    }

    @Override
    public VectorValue marshal(Vector v) {
        return new VectorValue(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Class to represent a vector value
     */
    public static class VectorValue {
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
         * Default constructor for VectorValue
         */
        @SuppressWarnings({"unused", "for xml"})
        public VectorValue() {
            x = y = z = 0;
        }

        /**
         * Constructor for VectorValue
         *
         * @param r the x value
         * @param g the y value
         * @param b the z value
         */
        public VectorValue(double r, double g, double b) {
            this.x = r;
            this.y = g;
            this.z = b;
        }
    }
}
