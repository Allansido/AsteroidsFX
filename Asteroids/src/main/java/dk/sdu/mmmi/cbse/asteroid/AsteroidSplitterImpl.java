package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 *
 * @author corfixen
 */
public class AsteroidSplitterImpl implements IAsteroidSplitter {

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        System.out.println("Trying to split asteroid.");

        Entity frag1 = createFragment(e.getRotation() + 35, e.getRadius(), e.getHealth(), e.getX(), e.getY());
        Entity frag2 = createFragment(e.getRotation() - 35, e.getRadius(), e.getHealth(), e.getX(), e.getY());

        if (frag1 != null) world.addEntity(frag1);
        if (frag2 != null) world.addEntity(frag2);

        world.removeEntity(e);
    }


    private Entity createFragment(double rotation, float originalSize, int originalHealth, double x, double y) {
        float newSize = originalSize / 1.5f;

        if (newSize < 10) {
            System.out.println("Too small to split.");
            return null;
        }

        Asteroid asteroid = new Asteroid();
        asteroid.setPolygonCoordinates(newSize, -newSize, -newSize, -newSize, -newSize, newSize, newSize, newSize);
        asteroid.setX(x);
        asteroid.setY(y);
        asteroid.setRadius(newSize);
        asteroid.setRotation(rotation);
        asteroid.setHealth(3); // reset health for split fragments
        asteroid.setMoveSpeed(100);
        return asteroid;
    }

}
