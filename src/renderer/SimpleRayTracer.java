package renderer;

import geometries.Intersectable;
import lighting.DirectionalLight;
import lighting.LightSource;
import lighting.PointLight;
import primitives.*;
import scene.Scene;

import java.util.List;

import static geometries.Intersectable.GeoPoint;
import static java.lang.Math.abs;
import static primitives.Util.*;

/**
 * A simple ray tracer
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * The maximum level of recursion
     */
    protected static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * The minimum value of k to calculate the color
     */
    protected static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * The initial value of k
     */
    protected static final Double3 INITIAL_K = Double3.ONE;
    /**
     * The density of the grid, used for glossy and diffused reflection.
     */
    protected final int density;

    /**
     * Constructor
     *
     * @param scene the scene to trace rays in
     */
    public SimpleRayTracer(Scene scene) {
        this(scene, 9);
    }

    /**
     * Constructor
     *
     * @param scene   the scene to trace rays in
     * @param density the density of the grid (used for glossy and diffused reflection)
     */
    public SimpleRayTracer(Scene scene, int density) {
        super(scene);
        this.density = density;
    }

    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    /**
     * Calculate the color at a point
     *
     * @param gp  the point to calculate the color for
     * @param ray the ray that hit the point
     * @return the color at the point
     */
    Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }

    /**
     * Calculate the color at a point
     *
     * @param intersection the point
     * @param ray          the ray that hit the point
     * @param level        the level of the recursion
     * @param k            the k value of the point
     * @return the color at the point
     */
    Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
    }

    /**
     * Find the closest intersection of a ray with the scene's geometries
     *
     * @param ray the ray
     * @return the closest intersection
     */
    GeoPoint findClosestIntersection(Ray ray) {
        return ray.findClosestGeoPoint(scene.geometries.findGeoIntersections(ray));
    }

    /**
     * Calculate the global effects at a point (reflected and refracted rays)
     *
     * @param gp    the point
     * @param ray   the ray
     * @param level the level of the recursion
     * @param k     the k value of the point
     * @return the color at the point
     */
    Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffectAverageColor(getReflectedRay(gp, v, n), n, level, k, material.kR, material.kG) // average color of reflected ray
                .add(calcGlobalEffectAverageColor(getRefractedRay(gp, v, n), n, level, k, material.kT, material.kB)); // average color of refracted ray
    }

    Ray getReflectedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, v.reflect(n), n);
    }
    Ray getRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, v, n);
    }

    /**
     * Calculate the global effect at a point
     *
     * @param ray   the ray to calculate the color for
     * @param level the level of the recursion
     * @param k     the factor of the color
     * @param kx    the factor of the effect
     * @return the color at the point
     */
    Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level - 1, kx).scale(k));
    }

    /**
     * Calculate the local effects at a point (diffusive and specular)
     *
     * @param gp  the point
     * @param ray the ray
     * @param k   the k value of the point
     * @return the color at the point
     */
    Color calcLocalEffects(Intersectable.GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission();

        Vector n = gp.geometry.getNormal(gp.point);
        Vector v = ray.getDirection();
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;
        Material material = gp.geometry.getMaterial();

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));

            //if (nl * nv > 0) {
            if (compareSign(nl, nv)) {
                Double3 ktr;
                if (lightSource instanceof DirectionalLight || (lightSource instanceof PointLight &&
                        isZero(((PointLight) lightSource).getShadowSoftness()))) {
                    // directional light or point light with sharp shadow
                    ktr = transparency(gp, l, n, lightSource);
                } else {
                    assert lightSource instanceof PointLight; // PointLight or SpotLight
                    ktr = calcLocalSoftShadowsEffects((PointLight) lightSource, gp, n, l);
                }

                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, l, v))));
                }
            }
        }

        return color;
    }

    /**
     * Calculate the transparency of the point (shadow)
     *
     * @param gp    the point
     * @param l     the light vector
     * @param n     the normal at the point
     * @param light the light source
     * @return the transparency of the point
     */
    Double3 transparency(GeoPoint gp, Vector l, Vector n, LightSource light) {
        Ray lightRay = new Ray(gp.point, l.scale(-1), n); // from point to light source
        var intersections = scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
        Double3 ktr = Double3.ONE;

        if (intersections == null)
            return ktr;

        for (GeoPoint p : intersections) {
            ktr = ktr.product(p.geometry.getMaterial().kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
        }

        return ktr;
    }

    /**
     * Calculate the diffusive effect at a point
     *
     * @param material the material at the point
     * @param nl       the dot product of the normal and the light vector
     * @return the diffusive effect at the point
     */
    Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(abs(nl));
    }

    /**
     * Calculate the specular effect at a point
     *
     * @param material the material at the point
     * @param n        the normal at the point
     * @param l        the light vector at the point
     * @param v        the view vector at the point
     * @return the specular effect at the point
     */
    Double3 calcSpecular(Material material, Vector n, Vector l, Vector v) {
        double vr = alignZero(v.dotProduct(l.reflect(n)));
        return vr >= 0 ? Double3.ZERO : material.kS.scale(Math.pow(-vr, material.nShininess));
    }

    /**
     * Construct the rays of target area from a ray
     *
     * @param ray    the ray to construct the rays from
     * @param n      the normal at the point
     * @param vpSize the size of the view plane
     * @return the rays
     */
    List<Ray> constructRays(Ray ray, Vector n, double vpSize) {
        double res = ray.getDirection().dotProduct(n);
        return isZero(vpSize) ? List.of(ray) :
                new TargetArea().setDirection(ray).setDensity(density).setVpSize(vpSize, vpSize)
                        .constructRayGrid().stream()
                        .filter(r -> compareSign(r.getDirection().dotProduct(n), res)).toList();
    }

    /**
     * Calculate the average color of the reflected and refracted rays
     * (used for glossy and diffused reflection)
     *
     * @param baseRay the base ray to calculate the rays from
     * @param n       the normal at the point of the base ray
     * @param level   the level of the recursion of the rays
     * @param k       the k value of the point at the base ray
     * @param kx      the color of the effect at the point
     * @param vpSize  the size of the view plane for the grid
     * @return the color at the point
     */
    Color calcGlobalEffectAverageColor(Ray baseRay, Vector n, int level, Double3 k, Double3 kx, double vpSize) {
        Color color = Color.BLACK;
        List<Ray> rays = constructRays(baseRay, n, vpSize);
        for (Ray r : rays)
            color = color.add(calcGlobalEffect(r, level, k, kx));
        return color.reduce(rays.size());
    }

    /**
     * Calculate the local effects at a point with soft shadows
     *
     * @param lightSource the light source
     * @param gp          the point
     * @param n           the normal at the point
     * @param l           the light vector at the point
     * @return the color at the point
     */
    Double3 calcLocalSoftShadowsEffects(PointLight lightSource, GeoPoint gp, Vector n, Vector l) {
        Double3 ktr = Double3.ZERO;
        List<Ray> rays = constructRays(new Ray(gp.point, l), n, lightSource.getShadowSoftness());
        for (Ray r : rays)
            ktr = ktr.add(transparency(gp, r.getDirection(), n, lightSource));
        ktr = ktr.reduce(rays.size());
        return ktr;
    }
}
