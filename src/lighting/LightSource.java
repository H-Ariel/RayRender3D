package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source in a scene
 */
public interface LightSource {
    /**
     * Get the intensity of the light at a given point
     *
     * @param p the point at which to get the intensity
     * @return the intensity of the light at the point
     */
    Color getIntensity(Point p);

    /**
     * Get the direction of the light at a given point
     *
     * @param p the point at which to get the direction
     * @return the direction of the light at the point
     */
    Vector getL(Point p);

    /**
     * Get the distance from the light source to a given point
     *
     * @param point the point to get the distance to
     * @return the distance from the light source to the point
     */
    double getDistance(Point point);
}
