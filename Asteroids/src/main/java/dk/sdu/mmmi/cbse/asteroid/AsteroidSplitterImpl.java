package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;

/**
 *
 * @author corfixen
 */
public class AsteroidSplitterImpl implements IAsteroidSplitter {

    @Override
    public void createSplitAsteroid(Entity e, World world) {

        Entity frag1 = createFragment(e.getRotation() + 35, e.getRadius(), e.getHealth(), e.getX(), e.getY());
        Entity frag2 = createFragment(e.getRotation() - 35, e.getRadius(), e.getHealth(), e.getX(), e.getY());

        if (frag1 != null) world.addEntity(frag1);
        if (frag2 != null) world.addEntity(frag2);

        world.removeEntity(e);
    }


    private Entity createFragment(double rotation, float originalSize, int originalHealth, double x, double y) {
        float newSize = originalSize / 1.5f;

        if (newSize < 10) {
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

        try {
            URL imageUrl = getClass().getResource("/asteroid.png");
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toString());
                ImageView imageView = new ImageView(image);

                double imageSize = newSize * 2; // Diameter
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);
                imageView.setPreserveRatio(true);

                asteroid.setImageView(imageView);
            } else {
                System.out.println("No asteroid image found for fragment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return asteroid;
    }

}
