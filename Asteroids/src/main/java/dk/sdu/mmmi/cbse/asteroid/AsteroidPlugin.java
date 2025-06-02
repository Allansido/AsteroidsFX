package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Random;

/**
 *
 * @author corfixen
 */
public class AsteroidPlugin implements IGamePluginService  {

    @Override
    public void start(GameData gameData, World world) {
        Random rnd = new Random();
        int asteroidCount = rnd.nextInt(10) + 3; // Random number between 3 and 10

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

        float x = rnd.nextFloat() * gameData.getDisplayWidth();
        float y = rnd.nextFloat() * gameData.getDisplayHeight();

        asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
        asteroid.setX(x);
        asteroid.setY(y);
        asteroid.setRadius(size);
        asteroid.setHealth(3);
        asteroid.setMoveSpeed(100);
        asteroid.setRotation(rnd.nextInt(360));
        asteroid.setType("Asteroid");

        try {
            URL imageUrl = getClass().getResource("/asteroid.png");
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toString());
                ImageView imageView = new ImageView(image);

                double ImageSize = asteroid.getRadius() * 3; // Diameter
                imageView.setFitWidth(ImageSize);
                imageView.setFitHeight(ImageSize);
                imageView.setPreserveRatio(true);

                asteroid.setImageView(imageView);
            } else {
                System.out.println("No asteroid image found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asteroid;
    }
}
