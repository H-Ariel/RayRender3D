package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import static primitives.Util.alignZero;

/**
 * Class representing a spotlight source in a scene
 */
public class SpotLight extends PointLight {
    /**
     * Direction of the spotlight
     */
    @XmlJavaTypeAdapter(XmlParser.VectorAdapter.class)
    private final Vector direction;

    /**
     * The narrow beam factor
     */
    @XmlElement
    protected double narrowBeam = 1;

    /**
     * Default constructor
     */
    private SpotLight() {
        super(Color.BLACK, Point.ZERO);
        direction = Vector.X;
    }

    /**
     * Constructor for a spotlight
     *
     * @param intensity the intensity of the spotlight
     * @param position  the position of the spotlight
     * @param direction the direction of the spotlight
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Set the narrow beam factor
     *
     * @param narrowBeam the narrow beam factor
     * @return this
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double dirL = alignZero(direction.dotProduct(getL(p)));
        if (dirL <= 0) return Color.BLACK;
        return super.getIntensity(p).scale(narrowBeam == 1 ? dirL : Math.pow(dirL, narrowBeam));
    }
}
