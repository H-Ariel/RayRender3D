package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Interface to present a geometry. All geometries should implement this interface
 *
 * @author Ariel and Asaf
 */
abstract public class Geometry extends Intersectable {
    /**
     * The color of the geometry
     */
    protected Color emission = Color.BLACK;

    /**
     * The material of the geometry
     */
    private Material material = new Material();

    /**
     * get the value of emission color
     *
     * @return emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * set the value of emission color
     *
     * @param emission the new color
     * @return this geometry
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * get the value of material
     *
     * @return material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * set the value of material
     *
     * @param material the new material
     * @return this geometry
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * returns the normal to the geometry at a given point
     *
     * @param p point on the geometry
     * @return normal to the geometry at the given point
     */
    public abstract Vector getNormal(Point p);
}