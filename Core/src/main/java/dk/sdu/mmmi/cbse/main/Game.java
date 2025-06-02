/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.map.MapSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author jcs
 */
class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Map<Entity, ImageView> imageViews = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServiceList;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;
    private final List<MapSPI> mapServices;

    private int destroyedAsteroids = 0;
    private final Text scoreText = new Text(10, 20, "Destroyed asteroids: 0");

    Game(List<IGamePluginService> gamePluginServices,
         List<IEntityProcessingService> entityProcessingServiceList,
         List<IPostEntityProcessingService> postEntityProcessingServices,
         List<MapSPI> mapServices) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServiceList = entityProcessingServiceList;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.mapServices = mapServices;
    }

    public void start(Stage window) throws Exception {
        Text text = new Text(10, 20, "Destroyed asteroids: 0");
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(text);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.A)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.D)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.W)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.A)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.D)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.W)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getGamePluginServices()) {
            iGamePlugin.start(gameData, world);
        }


        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        getMapServices().stream().findFirst().ifPresent(SPI -> {
            ImageView mapView = SPI.getMap();
            gameWindow.getChildren().add(mapView);
        });

        // Ensure scoreText stays on top
        scoreText.setFill(Color.WHITE);
        scoreText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 0, 1, 1);");
        gameWindow.getChildren().add(scoreText);
        scoreText.toFront();

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }

        }.start();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, world);
        }


    }

    private void draw() {
        for (Entity polygonEntity : polygons.keySet()) {
            if (!world.getEntities().contains(polygonEntity)) {
                if (polygonEntity.getClass().getSimpleName().equals("Asteroid")) {
                    destroyedAsteroids++;
                    scoreText.setText("Destroyed asteroids: " + destroyedAsteroids);
                }


                Polygon removedPolygon = polygons.get(polygonEntity);
                polygons.remove(polygonEntity);
                gameWindow.getChildren().remove(removedPolygon);

                if (polygonEntity.getImageView() != null) {
                    gameWindow.getChildren().remove(polygonEntity.getImageView());
                }
            }
        }

        for (Entity entity : imageViews.keySet()) {
            if (!world.getEntities().contains(entity)) {
                ImageView removedImage = imageViews.remove(entity);
                gameWindow.getChildren().remove(removedImage);
            }
        }

        for (Entity entity : world.getEntities()) {
            if (entity.getImageView() != null) {
                // Handle ImageView rendering
                ImageView imageView = entity.getImageView();

                if (!imageViews.containsKey(entity)) {
                    imageViews.put(entity, imageView);
                    gameWindow.getChildren().add(imageView);
                }

                double width = imageView.getFitWidth();
                double height = imageView.getFitHeight();
                imageView.setTranslateX(entity.getX() - width / 2);
                imageView.setTranslateY(entity.getY() - height / 2);
                imageView.setRotate(entity.getRotation());

            } else {
                // Handle Polygon rendering if no image view
                Polygon polygon = polygons.get(entity);
                if (polygon == null) {
                    polygon = new Polygon(entity.getPolygonCoordinates());
                    polygon.setFill(entity.getColor() != null ? entity.getColor() : Color.WHITE);
                    polygons.put(entity, polygon);
                    gameWindow.getChildren().add(polygon);
                }

                polygon.setTranslateX(entity.getX());
                polygon.setTranslateY(entity.getY());
                polygon.setRotate(entity.getRotation());


            }
        }
    }


    public List<IGamePluginService> getGamePluginServices() {
        return gamePluginServices;
    }

    public List<IEntityProcessingService> getEntityProcessingServices() {
        return entityProcessingServiceList;
    }

    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postEntityProcessingServices;
    }

    public List<MapSPI> getMapServices() {
        return mapServices;
    }

}
