package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Class representing a point light source in a scene
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the light
     */
    @XmlJavaTypeAdapter(XmlParser.PointAdapter.class)
    protected Point position;

    /**
     * The constant attenuation factor
     */
    @XmlElement
    private double kC = 1;

    /**
     * The linear attenuation factor
     */
    @XmlElement
    private double kL = 0;

    /**
     * The quadratic attenuation factor
     */
    @XmlElement
    private double kQ = 0;

    /**
     * Size of the soft shadow, determining its softness or blur level.
     */
    private double shadowSoftness = 0;


    /**
     * Default constructor
     */
    private PointLight() {
        super(Color.BLACK);
    }

    /**
     * Constructor for a point light
     *
     * @param intensity the intensity of the light
     * @param position  the position of the light
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Set the constant attenuation factor
     *
     * @param kC the constant attenuation factor
     * @return this
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Set the linear attenuation factor
     *
     * @param kL the linear attenuation factor
     * @return this
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Set the quadratic attenuation factor
     *
     * @param kQ the quadratic attenuation factor
     * @return this
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Get the position of the light
     *
     * @return the position of the light
     */
    @XmlElement
    public double getShadowSoftness() {
        return shadowSoftness;
    }

    /**
     * Set the size of the soft shadow
     *
     * @param shadowSoftness the size of the soft shadow
     * @return this
     */
    public PointLight setShadowSoftness(double shadowSoftness) {
        this.shadowSoftness = shadowSoftness;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double distance = p.distance(position);
        return intensity.scale(1 / (kC + kL * distance + kQ * distance * distance));
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return point.distance(position);
    }
}
