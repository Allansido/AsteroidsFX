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
    private final Collection<BulletSPI> bulletSPIs;

    // ✅ Default constructor for module system (ServiceLoader)
    public EnemyControlSystem() {
        this(ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList()));
    }

    // ✅ Testable constructor (used in unit tests)
    public EnemyControlSystem(Collection<BulletSPI> bulletSPIs) {
        this.bulletSPIs = bulletSPIs;
    }

    @Override
    public void process(GameData gameData, World world) {
        double centerX = gameData.getDisplayWidth() / 2f;
        double centerY = gameData.getDisplayHeight() / 2f;
        double minDistanceToCenter = 150;

        for (Entity enemy : world.getEntities(Enemy.class)) {
            double deltaX = centerX - enemy.getX();
            double deltaY = centerY - enemy.getY();
            double distanceToCenter = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distanceToCenter > minDistanceToCenter) {
                float targetAngle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

                double currentAngle = enemy.getRotation();
                double rotationSpeed = 2f;
                double angleDifference = targetAngle - currentAngle;
                angleDifference = (angleDifference + 180) % 360 - 180;

                if (Math.abs(angleDifference) < rotationSpeed) {
                    currentAngle = targetAngle;
                } else {
                    currentAngle += Math.signum(angleDifference) * rotationSpeed;
                }
                enemy.setRotation(currentAngle);
            } else {
                double randomRotation = random.nextDouble() * 20 - 5;
                enemy.setRotation(enemy.getRotation() + randomRotation);
            }

            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));
            enemy.setX(enemy.getX() + changeX);
            enemy.setY(enemy.getY() + changeY);

            if (enemy.getX() < 0) enemy.setX(1);
            if (enemy.getX() > gameData.getDisplayWidth()) enemy.setX(gameData.getDisplayWidth() - 1);
            if (enemy.getY() < 0) enemy.setY(1);
            if (enemy.getY() > gameData.getDisplayHeight()) enemy.setY(gameData.getDisplayHeight() - 1);

            if (random.nextDouble() < 0.03) {
                bulletSPIs.stream().findFirst().ifPresent(
                        spi -> world.addEntity(spi.createBullet(enemy, gameData))
                );
            }
        }
    }
}
