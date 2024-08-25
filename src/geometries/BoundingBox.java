package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Class to represent a bounding box
 */
public class BoundingBox {
    /**
     * The minimum point of the bounding box
     */
    public Point max;

    /**
     * The maximum point of the bounding box
     */
    public Point min;

    /**
     * Default constructor
     */
    public BoundingBox() {
        this.min = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        this.max = new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    /**
     * Constructor for the bounding box
     *
     * @param min the minimum point of the bounding box
     * @param max the maximum point of the bounding box
     */
    public BoundingBox(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Check if a ray intersects the bounding box
     *
     * @param ray the ray to check
     * @return true if the ray intersects the bounding box, false otherwise
     */
    public boolean hasIntersections(Ray ray) {
        double boxMinX = min.getX(), boxMinY = min.getY(), boxMinZ = min.getZ();
        double boxMaxX = max.getX(), boxMaxY = max.getY(), boxMaxZ = max.getZ();

        Point head = ray.getHead();
        Vector direction = ray.getDirection();
        double headX = head.getX(), headY = head.getY(), headZ = head.getZ();
        double dirX = direction.getX(), dirY = direction.getY(), dirZ = direction.getZ();

        double tMin = Double.NEGATIVE_INFINITY, tMax = Double.POSITIVE_INFINITY;

        if (dirX != 0) {
            double t1 = (boxMinX - headX) / dirX;
            double t2 = (boxMaxX - headX) / dirX;
            tMin = Math.max(tMin, Math.min(t1, t2));
            tMax = Math.min(tMax, Math.max(t1, t2));
        } else if (headX <= boxMinX || headX >= boxMaxX) {
            return false;
        }

        if (dirY != 0) {
            double t1 = (boxMinY - headY) / dirY;
            double t2 = (boxMaxY - headY) / dirY;
            tMin = Math.max(tMin, Math.min(t1, t2));
            tMax = Math.min(tMax, Math.max(t1, t2));
        } else if (headY <= boxMinY || headY >= boxMaxY) {
            return false;
        }

        if (dirZ != 0) {
            double t1 = (boxMinZ - headZ) / dirZ;
            double t2 = (boxMaxZ - headZ) / dirZ;
            tMin = Math.max(tMin, Math.min(t1, t2));
            tMax = Math.min(tMax, Math.max(t1, t2));
        } else if (headZ <= boxMinZ || headZ >= boxMaxZ) {
            return false;
        }

        return tMax >= tMin;
    }

    /**
     * Get the center of the bounding box
     *
     * @return the center of the bounding box
     */
    public Point getCenter() {
        return new Point(
                (max.getX() + min.getX()) / 2.0,
                (max.getY() + min.getY()) / 2.0,
                (max.getZ() + min.getZ()) / 2.0
        );
    }

    /**
     * Union of two bounding boxes
     *
     * @param box the other bounding box
     * @return the union of the two bounding boxes
     */
    public BoundingBox union(BoundingBox box) {
        return new BoundingBox(
                new Point(
                        Math.min(min.getX(), box.min.getX()),
                        Math.min(min.getY(), box.min.getY()),
                        Math.min(min.getZ(), box.min.getZ())
                ),
                new Point(
                        Math.max(max.getX(), box.max.getX()),
                        Math.max(max.getY(), box.max.getY()),
                        Math.max(max.getZ(), box.max.getZ())
                )
        );
    }
}
