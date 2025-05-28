package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author corfixen
 */
public class AsteroidPlugin implements IGamePluginService  {

    @Override
    public void start(GameData gameData, World world) {
        Random rnd = new Random();
        int asteroidCount = rnd.nextInt(6) + 1; // Random number between 1 and 6

        for (int i = 0; i < asteroidCount; i++) {
            Entity asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }


    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }


    Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();
        Random rnd = new Random();
        int size = rnd.nextInt(20) + 20; // Size between 20–40

        // Randomize position within game world boundaries
        float x = rnd.nextFloat() * gameData.getDisplayWidth();
        float y = rnd.nextFloat() * gameData.getDisplayHeight();

        asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
        asteroid.setX(x);
        asteroid.setY(y);
        asteroid.setRadius(size); // Set the radius based on the size
        asteroid.setHealth(3); // Set a default health value
        asteroid.setMoveSpeed(100); // Set a default move speed
        // Randomly rotating asteroids.
        asteroid.setRotation(rnd.nextInt(360));
        return asteroid;
    }
}
