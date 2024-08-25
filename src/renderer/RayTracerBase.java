package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class for ray tracing
 */
public abstract class RayTracerBase {
    /**
     * The scene to trace rays in
     */
    protected final Scene scene;

    /**
     * Constructor
     *
     * @param scene the scene to trace rays in
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Trace a ray in the scene
     *
     * @param ray the ray to trace
     * @return the color of the ray after tracing
     */
    public abstract Color traceRay(Ray ray);
}
