package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Class to present an ambient light
 */
public class AmbientLight extends Light {
    /**
     * A constant for no ambient light
     */
    static public final AmbientLight NONE = new AmbientLight(Color.BLACK, 0);

    /**
     * Default constructor
     */
    private AmbientLight() {
        super(Color.BLACK);
    }

    /**
     * Constructor
     *
     * @param iA the intensity of the ambient light
     * @param aK the attenuation factor
     */
    public AmbientLight(Color iA, Double3 aK) {
        super(iA.scale(aK));
    }

    /**
     * Constructor
     *
     * @param iA the intensity of the ambient light
     * @param aK the attenuation factor
     */
    public AmbientLight(Color iA, double aK) {
        super(iA.scale(aK));
    }
}
