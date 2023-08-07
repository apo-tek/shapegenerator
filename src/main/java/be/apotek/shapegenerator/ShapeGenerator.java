package be.apotek.shapegenerator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShapeGenerator {

    public static boolean preconditionsCheck(final int points, final double min, final double... doubles) {
        return !(points >= min) && Arrays.stream(doubles).allMatch(d -> d > 0.0);
    }

    /**
     * This method generates the points of a circle centered around (0,0,0)
     *
     * @param radius The radius of the circle
     * @param points The amount (positive) of points that the list should contain. This represents the precision
     *               and resolution of the generated circle. It is recommended not to be lower than 8
     * @return The array of generated points or null if the parameters are invalid
     */
    public static Vector[] circle(final double radius, final int points) {
        return ellipse(radius, radius, points);
    }

    /**
     * This method generates the points of an ellipse centered around (0,0,0) in the horizontal plan
     *
     * @param r1     The radius along the x-axis of the ellipse (must be positive)
     * @param r2     The radius along the z-axis of the ellipse (must be positive)
     * @param points The amount of points that the ellipse should contain (must be positive and greater or equal
     *               to 4). This is a measurement of the resolution of the ellipse
     * @return The array of generated points or null if the parameters are invalid
     */
    public static Vector[] ellipse(final double r1, final double r2, final int points) {
        if (preconditionsCheck(points, 4, r1, r2)) return null;

        Vector[] generated = new Vector[points];
        for (int i = 0; i < points; i++) {
            double projX = Math.cos(i * 2 * Math.PI / (double) points);
            double projZ = Math.sin(i * 2 * Math.PI / (double) points);
            double radius = r1 == r2 ? r1 : Math.sqrt((r1 * r1 * r2 * r2) / (r1 * r1 * projZ * projZ + r2 * r2 * projX * projX));
            generated[i] = new Vector(radius * projX, 0, radius * projZ);
        }

        return generated;
    }

    /**
     * This method generates the points of a regular polygon centered around (0,0,0) in the horizontal plan
     *
     * @param length The length of the polygon's sides;
     * @param sides  The amount of sides of the polygon (must be positive)
     * @param points The amount of points that the polygon should contain (must be positive and greater or equal
     *               to {@code sides}). This is a measurement of the resolution of the polygon
     * @return The array of generated points or null if the parameters are invalid.
     */
    public static Vector[] polygon(final double length, final int sides, final int points) {
        if (preconditionsCheck(points, sides, sides, length) || sides < 3) return null;
        double radius = length / Math.sqrt(2 * (1 - Math.cos(2 * Math.PI / (double) sides)));
        Vector[] generated = new Vector[points];
        double[] beams = new double[points / sides];

        final double angle = (Math.PI * 2 / sides) / (double) (points / sides);
        final double slopeAngle = 0.5 * Math.PI + Math.PI / (double) sides;
        final double slope = Math.tan(slopeAngle);
        final double invSlope = 1.0 / slope;

        for (int i = 0; i < points / sides; i++) {
            if (i == 0) {
                beams[0] = radius;
                continue;
            }
            double projX = Math.cos(i * angle);
            double projZ = Math.sin(i * angle);
            beams[i] = Math.abs(radius / (projX - projZ * invSlope));
        }

        for (int j = 0; j < points; j++) {
            double r = beams[j % (points / sides)];
            generated[j] = new Vector(r * Math.cos(j * angle), 0, r * Math.sin(j * angle));
        }

        return generated;
    }

    /**
     * This method generates the points of a square centered around (0,0,0) in the horizontal plan
     *
     * @param length The length of the square (must be positive)
     * @param points The amount of points that the square should contain (must be positive and greater or equal
     *               to 4). This is a measurement of the resolution of the square
     * @return The array of generated points or null if the parameters are invalid
     */
    public static Vector[] square(final double length, final int points) {
        return polygon(length, 4, points);
    }

    /**
     * This method generates the points of a regular pentagon centered around (0,0,0) in the horizontal plan
     *
     * @param length The length of the pentagon (must be positive)
     * @param points The amount of points that the pentagon should contain (must be positive and greater or equal
     *               to 5). This is a measurement of the resolution of the pentagon
     * @return The array of generated points or null if the parameters are invalid
     */
    public static Vector[] pentagon(final double length, final int points) {
        return polygon(length, 5, points);
    }

    /**
     * This method generates the points of a regular hexagon centered around (0,0,0) in the horizontal plan
     *
     * @param length The length of the hexagon (must be positive)
     * @param points The amount of points that the hexagon should contain (must be positive and greater or equal
     *               to 6). This is a measurement of the resolution of the hexagon
     * @return The array of generated points or null if the parameters are invalid
     */
    public static Vector[] hexagon(final double length, final int points) {
        return polygon(length, 6, points);
    }

    /**
     * This method generates the points of a regular octagon centered around (0,0,0) in the horizontal plan
     *
     * @param length The length of the octagon (must be positive)
     * @param points The amount of points that the octagon should contain (must be positive and greater or equal
     *               to 8). This is a measurement of the resolution of the octagon
     * @return The array of generated points or null if the parameters are invalid
     */
    public static Vector[] octagon(final double length, final int points) {
        return polygon(length, 8, points);
    }

    public static Vector[] polygonalStar(final double radius, final int branches, final int points) {
        if (preconditionsCheck(points, 2 * branches, branches, radius)) return null;
        Vector[] generated = new Vector[points];
        double innerRadius = radius / ((Math.tan(Math.PI * 0.4f) + Math.tan(Math.PI * 0.3f)) * Math.sin(Math.PI * 0.2f));

        for (int i = 0; i < branches; i++) {
            double startingAngle = (float) (i * 2 * (Math.PI / (double) branches));
            double middleAngle = (float) (startingAngle + (Math.PI / (double) branches));
            double endAngle = (float) ((i + 1) * 2 * (Math.PI / (double) branches));
            Vector startingPoint = new Vector(radius * Math.cos(startingAngle), 0, radius * Math.sin(startingAngle));
            Vector middlePoint = new Vector(innerRadius * Math.cos(middleAngle), 0, innerRadius * Math.sin(middleAngle));
            Vector endPoint = new Vector(radius * Math.cos(endAngle), 0, radius * Math.sin(endAngle));
            Vector firstBase = middlePoint.clone().subtract(startingPoint);
            Vector secondBase = endPoint.clone().subtract(middlePoint);
            for (int j = 0; j < points / (2 * branches); j++) {
                generated[(2 * j) + i * (points / branches)] = startingPoint.clone().add(firstBase.clone().multiply(j * 2 * branches / (double) points));
                generated[(2 * j + 1) + i * (points / branches)] = middlePoint.clone().add(secondBase.clone().multiply(j * 2 * branches / (double) points));
            }
        }

        return generated;
    }

    /**
     * @param r1          The radius of the ellipsoid alongside the x direction
     * @param r2          The radius of the ellipsoid alongside the y direction
     * @param r3          The radius of the ellipsoid alongside the z direction
     * @param granularity The amount (positive) of points that the surface should contain in each of its 2 dimension
     * @return The array of generated points or null if the parameters are invalid
     */
    public static Vector[] ellipsoid(final double r1, final double r2, final double r3, final int granularity) {
        if (preconditionsCheck(granularity, 4, r1, r2, r3)) return null;

        int repartition = (int) Math.max(Math.floor(Math.sqrt(granularity)), 2);

        Vector[] generated = new Vector[repartition * repartition];

        for (int i = 0; i < repartition; i++) {
            double colatitude = -0.5 * Math.PI + i * 2 * Math.PI / repartition;
            double projY = Math.cos(colatitude);
            for (int j = 0; j < repartition; j++) {
                double longitude = -Math.PI + j * 2 * Math.PI / repartition;
                double projX = Math.sin(colatitude) * Math.cos(longitude);
                double projZ = Math.sin(colatitude) * Math.sin(longitude);
                generated[i * repartition + j] = new Vector(r1 * projX, r2 * projY, r3 * projZ);
            }
        }

        return generated;
    }

    public static Vector[] sphere(final double radius, final int granularity) {
        return ellipsoid(radius, radius, radius, granularity);
    }


    public static Vector[] circularTore(final double r1, final double r2, final int granularity) {
        if (preconditionsCheck(granularity, 4, r1, r2))
            return null;

        Vector translate = new Vector(0, 0, r1 + r2);
        int repartition = 64;
        Vector[] generated = new Vector[repartition * granularity];
        Vector[] circle = circle(r1, granularity);

        if (circle == null)
            return null;

        for (Vector vector : circle) {
            vector.rotateAroundZ(Math.PI / 2.0);
            vector.add(translate);
        }

        for (int i = 0; i < repartition; i++) {
            double angle = 2 * i * Math.PI / (double) repartition;
            for (int j = 0; j < circle.length; j++) {
                generated[j + (i * circle.length)] = circle[j].clone().rotateAroundY(angle);
            }
        }

        return generated;
    }

}
