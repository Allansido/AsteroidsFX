package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnemyControlSystemTest {

    private EnemyControlSystem enemyControlSystem;
    private GameData gameData;
    private World world;
    private Enemy enemy;
    private BulletSPI bulletSPI;

    @BeforeEach
    public void setUp() {
        gameData = mock(GameData.class);
        world = mock(World.class);
        bulletSPI = mock(BulletSPI.class);

        enemyControlSystem = new EnemyControlSystem(List.of(bulletSPI));

        when(gameData.getDisplayWidth()).thenReturn(800);
        when(gameData.getDisplayHeight()).thenReturn(600);

        enemy = spy(new Enemy());
        enemy.setX(100);
        enemy.setY(100);
        enemy.setRotation(0);
        enemy.setHealth(3);
        enemy.setRadius(10);

        when(world.getEntities(Enemy.class)).thenReturn(List.of(enemy));
    }

    @Test
    public void testEnemyMovesTowardCenter() {
        double oldX = enemy.getX();
        double oldY = enemy.getY();

        enemyControlSystem.process(gameData, world);

        assertNotEquals(oldX, enemy.getX(), "Enemy X should change");
        assertNotEquals(oldY, enemy.getY(), "Enemy Y should change");
    }

    @Test
    public void testEnemyCanShootBullet() {
        Entity bullet = new Entity();
        when(bulletSPI.createBullet(eq(enemy), eq(gameData))).thenReturn(bullet);

        for (int i = 0; i < 100; i++) {
            enemyControlSystem.process(gameData, world);
        }

        verify(bulletSPI, atLeastOnce()).createBullet(eq(enemy), eq(gameData));
        verify(world, atLeastOnce()).addEntity(bullet);
    }
}
