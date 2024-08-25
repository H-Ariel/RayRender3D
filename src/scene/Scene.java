package scene;

import XmlParser.ColorAdapter;
import XmlParser.GeometriesAdapter;
import geometries.Geometries;
import lighting.*;
import primitives.Color;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.LinkedList;
import java.util.List;


/**
 * Class to present a scene of geometries and lighting
 */
@XmlRootElement
public class Scene {
    /**
     * The name of the scene
     */
    @XmlAttribute
    final public String name;

    /**
     * The background color of the scene
     */
    @XmlJavaTypeAdapter(ColorAdapter.class)
    public Color background = Color.BLACK;

    /**
     * The ambient light of the scene
     */
    @XmlElement
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The geometries of the scene
     */
    @XmlJavaTypeAdapter(GeometriesAdapter.class)
    public Geometries geometries = new Geometries();

    /**
     * The lights of the scene
     */
    @XmlElementWrapper(name = "lights")
    @XmlElements({
            @XmlElement(name = "PointLight", type = PointLight.class),
            @XmlElement(name = "SpotLight", type = SpotLight.class),
            @XmlElement(name = "DirectionalLight", type = DirectionalLight.class)
    })
    public List<LightSource> lights = new LinkedList<>();


    /**
     * Constructor
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Default constructor
     */
    private Scene() {
        this.name = "default";
    }

    /**
     * Set the lights of the scene
     *
     * @param lights the lights
     * @return the scene (for chaining)
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * Set the background color of the scene
     *
     * @param background the background color
     * @return the scene (for chaining)
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Set the ambient light of the scene
     *
     * @param ambientLight the ambient light
     * @return the scene (for chaining)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Set the geometries of the scene
     *
     * @param geometries the geometries
     * @return the scene (for chaining)
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
