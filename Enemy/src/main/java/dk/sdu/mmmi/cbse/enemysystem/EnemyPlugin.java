package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Random;

public class EnemyPlugin implements IGamePluginService {
    Entity enemy;
    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy(gameData);
        world.addEntity(enemy);
        System.out.println("EnemyPlugin started at X: " + enemy.getX() + ", Y: " + enemy.getY());
    }

    public Entity createEnemy(GameData gameData) {
        Entity enemy = new Enemy();
        enemy.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);

        float randomX = random.nextFloat() * gameData.getDisplayWidth();
        float randomY = random.nextFloat() * gameData.getDisplayHeight();

        enemy.setX(randomX);
        enemy.setY(randomY);
        enemy.setRadius(8);
        enemy.setHealth(3);
        try {
            URL imageUrl = getClass().getResource("/enemy.png");
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toString());
                ImageView imageView = new ImageView(image);

                double ImageSize = enemy.getRadius() * 4; // Diameter
                imageView.setFitWidth(ImageSize);
                imageView.setFitHeight(ImageSize);
                imageView.setPreserveRatio(true); // Keep aspect ratio

                enemy.setImageView(imageView);
            } else {
                System.out.println("No asteroid image found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return enemy;
    }


    @Override
    public void stop(GameData gameData, World world) {
        world.getEntities(Enemy.class).forEach(world::removeEntity);
    }
}
