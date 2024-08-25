package XmlParser;

import primitives.Color;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Class to adapt a color to XML
 */
public class ColorAdapter extends XmlAdapter<ColorAdapter.ColorValue, Color> {
    @Override
    public Color unmarshal(ColorValue v) {
        return new Color(v.r, v.g, v.b);
    }

    @Override
    public ColorValue marshal(Color v) {
        return v == null ? null : new ColorValue(v.getR(), v.getG(), v.getB());
    }

    /**
     * Class to represent a color value
     */
    public static class ColorValue {
        /**
         * The r value
         */
        @XmlAttribute
        public double r;

        /**
         * The g value
         */
        @XmlAttribute
        public double g;

        /**
         * The b value
         */
        @XmlAttribute
        public double b;

        /**
         * Default constructor for ColorValue
         */
        @SuppressWarnings({"unused", "for xml"})
        public ColorValue() {
            r = g = b = 0;
        }

        /**
         * Constructor for ColorValue
         *
         * @param r the r value
         * @param g the g value
         * @param b the b value
         */
        public ColorValue(double r, double g, double b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
