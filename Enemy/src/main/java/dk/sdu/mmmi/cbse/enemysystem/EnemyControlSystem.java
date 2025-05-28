package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        double centerX = gameData.getDisplayWidth() / 2f;
        double centerY = gameData.getDisplayHeight() / 2f;
        double minDistanceToCenter = 150; // Minimum distance from the center

        for (Entity enemy : world.getEntities(Enemy.class)) {
            // Calculate angle to the center
            double deltaX = centerX - enemy.getX();
            double deltaY = centerY - enemy.getY();
            double distanceToCenter = Math.sqrt(deltaX * deltaX + deltaY * deltaY);


            if (distanceToCenter > minDistanceToCenter) {
                // Calculate angle to the center
                float targetAngle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

                // Gradually adjust rotation towards the target angle
                double currentAngle = enemy.getRotation();
                double rotationSpeed = 2f; // Adjust this value for smoother or faster turning
                double angleDifference = targetAngle - currentAngle;

                // Normalize the angle difference to the range [-180, 180]
                angleDifference = (angleDifference + 180) % 360 - 180;

                // Apply rotation adjustment
                if (Math.abs(angleDifference) < rotationSpeed) {
                    currentAngle = targetAngle; // Snap to target if close enough
                } else {
                    currentAngle += Math.signum(angleDifference) * rotationSpeed;
                }
                enemy.setRotation(currentAngle);
            } else {
                // Apply random rotation adjustment
                double randomRotation = random.nextDouble() * 20 - 5; // Random value between -5 and 5
                enemy.setRotation(enemy.getRotation() + randomRotation);
            }

            // Calculate new position
            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));
            enemy.setX(enemy.getX() + changeX);
            enemy.setY(enemy.getY() + changeY);

            // Boundary check
            if (enemy.getX() < 0) {
                enemy.setX(1);
            }
            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(gameData.getDisplayWidth() - 1);
            }
            if (enemy.getY() < 0) {
                enemy.setY(1);
            }
            if (enemy.getY() > gameData.getDisplayHeight()) {
                enemy.setY(gameData.getDisplayHeight() - 1);
            }

            // Random shooting logic
            if (random.nextDouble() < 0.01) { // 1% chance to shoot
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> world.addEntity(spi.createBullet(enemy, gameData))
                );
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}