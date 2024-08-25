package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Class representing a directional light source in a scene
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The direction of the light
     */
    @XmlJavaTypeAdapter(XmlParser.VectorAdapter.class)
    private final Vector direction;

    /**
     * Get the direction of the light
     *
     * @param intensity the intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Default constructor
     */
    private DirectionalLight() {
        super(Color.BLACK);
        direction = Vector.X;
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
