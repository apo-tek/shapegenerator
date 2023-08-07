import be.apotek.shapegenerator.ShapeGenerator;
import be.apotek.shapegenerator.Vector;
import org.junit.Assert;
import org.junit.Test;

public class GenerationTests {

    @Test
    public void ellipseTest() {
        int points = 100000;
        int r1 = 10;
        int r2 = 5;
        long start = System.currentTimeMillis();
        Vector[] generated = ShapeGenerator.ellipse(r1, r2, points);
        long end = System.currentTimeMillis();
        System.out.printf("Performance : %d ms%n", end - start);
        Assert.assertNotNull(generated);
    }

    @Test
    public void polyTest() {
        int sides = 5;
        int points = 100000;
        double radius = 10;
        long start = System.currentTimeMillis();
        Vector[] generated = ShapeGenerator.polygon(radius, sides, points);
        long end = System.currentTimeMillis();
        System.out.printf("Performance : %d ms%n", end - start);
        Assert.assertNotNull(generated);
    }

    @Test
    public void starTest() {
        int branches = 5;
        int points = 100;
        double radius = 10;
        long start = System.currentTimeMillis();
        Vector[] generated = ShapeGenerator.polygonalStar(radius, branches, points);
        long end = System.currentTimeMillis();
        System.out.printf("Performance : %d ms%n", end - start);
        Assert.assertNotNull(generated);
    }

}
