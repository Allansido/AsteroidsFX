package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class PlayerPlugin implements IGamePluginService {

    private Entity player;

    public PlayerPlugin() {

    }

    @Override
    public void start(GameData gameData, World world) {
        player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    private Entity createPlayerShip(GameData gameData) {

        Entity playerShip = new Player();
        playerShip.setPolygonCoordinates(-5,-5,10,0,-5,5);
        playerShip.setX(gameData.getDisplayHeight() * 0.25);
        playerShip.setY(gameData.getDisplayWidth() * 0.25);
        playerShip.setHealth(3);
        playerShip.setRadius(8);

        try {
            URL imageUrl = getClass().getResource("/player.png");
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toString());
                ImageView imageView = new ImageView(image);

                double ImageSize = playerShip.getRadius() * 3; // Diameter
                imageView.setFitWidth(ImageSize);
                imageView.setFitHeight(ImageSize);
                imageView.setPreserveRatio(true); // Keep aspect ratio

                playerShip.setImageView(imageView);
            } else {
                System.out.println("No asteroid image found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }

}
