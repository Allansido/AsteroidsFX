package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessor implements IEntityProcessingService {

    private IAsteroidSplitter asteroidSplitter = new AsteroidSplitterImpl();

    @Override
    public void process(GameData gameData, World world) {

        for (Entity asteroid : world.getEntities(Asteroid.class)) {

            if (((Asteroid) asteroid).isHit()) {
                asteroid.setHealth(asteroid.getHealth() - 1); // Decrement health
                ((Asteroid) asteroid).setHit(false);

                if (asteroid.getHealth() <= 0) {
                    if (asteroidSplitter != null) {
                        asteroidSplitter.createSplitAsteroid(asteroid, world);
                    } else {
                        world.removeEntity(asteroid);
                    }
                    continue;
                }
            }

            // Movement and screen wrapping
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));

            asteroid.setX(asteroid.getX() + changeX * 0.5);
            asteroid.setY(asteroid.getY() + changeY * 0.5);

            if (asteroid.getX() < 0) {
                asteroid.setX(asteroid.getX() + gameData.getDisplayWidth());
            }
            if (asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setX(asteroid.getX() - gameData.getDisplayWidth());
            }
            if (asteroid.getY() < 0) {
                asteroid.setY(asteroid.getY() + gameData.getDisplayHeight());
            }
            if (asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setY(asteroid.getY() - gameData.getDisplayHeight());
            }
        }
    }

    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }
}
