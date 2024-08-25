package lighting;

import primitives.Color;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Abstract class representing a light source in a scene
 */
@XmlRootElement
public abstract class Light {
    /**
     * The intensity of the ambient light
     */
    @XmlJavaTypeAdapter(XmlParser.ColorAdapter.class)
    protected final Color intensity;

    /**
     * Default constructor
     */
    private Light() {
        intensity = Color.BLACK;
    }

    /**
     * Constructor to create a light with a given intensity
     *
     * @param intensity the intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Get the intensity of the light
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
