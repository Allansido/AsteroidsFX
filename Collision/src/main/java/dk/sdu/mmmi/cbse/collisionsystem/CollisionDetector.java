package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public class CollisionDetector implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity e1 : world.getEntities()) {
            for (Entity e2 : world.getEntities()) {
                if (e1.getID().equals(e2.getID())) continue;
                if (!collides(e1, e2)) continue;

                // Bullet collision
                if (e1 instanceof Bullet) {
                    Bullet bullet = (Bullet) e1;
                    Entity shooter = bullet.getShooter();
                    Entity target = e2;

                    if (shooter != null && shooter.getID().equals(target.getID())) continue;

                    // ASTEROID HIT
                    if (target instanceof Asteroid) {
                        Asteroid asteroid = (Asteroid) target;
                        asteroid.setHit(true);
                        if (shooter instanceof Player) {
                            asteroid.setProperty("destroyedByPlayer", true);
                        }
                    }

                    // PLAYER HIT
                    else if (target instanceof Player && !(shooter instanceof Player)) {
                        target.setHealth(target.getHealth() - 1);
                        if (target.getHealth() <= 0) {
                            world.removeEntity(target);
                        }
                    }

                    // ENEMY HIT
                    else if (target instanceof Enemy && !(shooter instanceof Enemy)) {
                        target.setHealth(target.getHealth() - 1);
                        if (target.getHealth() <= 0) {
                            world.removeEntity(target);
                        }
                    }

                    // Remove bullet after collision
                    world.removeEntity(e1);
                }

                // Player <-> Enemy or Asteroid
                if ((e1 instanceof Player && (e2 instanceof Asteroid || e2 instanceof Enemy)) ||
                        (e2 instanceof Player && (e1 instanceof Asteroid || e1 instanceof Enemy))) {
                    world.removeEntity(e1);
                    world.removeEntity(e2);
                }

                // Enemy <-> Asteroid
                if ((e1 instanceof Enemy && e2 instanceof Asteroid) ||
                        (e2 instanceof Enemy && e1 instanceof Asteroid)) {
                    world.removeEntity(e1);
                    world.removeEntity(e2);
                }
            }
        }
    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }
}
