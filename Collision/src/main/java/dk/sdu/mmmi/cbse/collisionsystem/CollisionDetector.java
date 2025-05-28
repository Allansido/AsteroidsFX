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

    public CollisionDetector() {
    }

    @Override
    public void process(GameData gameData, World world) {

        // two for loops for all entities in the world
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {

                // if the two entities are identical, skip the iteration
                if (entity1.getID().equals(entity2.getID())) {
                    continue;
                }

                // CollisionDetection
                if (collides(entity1, entity2)) {

                    // As per Lab requirement, remove player if asteroid collides with player.
                    // Player <-> Asteroid
                    if ((entity1 instanceof Player && entity2 instanceof Asteroid) ||
                            (entity1 instanceof Asteroid && entity2 instanceof Player)) {
                        System.out.println("Player collided with asteroid.");
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                        continue;
                    }

                    // Enemy <-> Asteroid
                    if ((entity1 instanceof Enemy && entity2 instanceof Asteroid) ||
                            (entity1 instanceof Asteroid && entity2 instanceof Enemy)) {
                        System.out.println("Enemy collided with asteroid.");
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                        continue;
                    }


                    // Bullet collisions.
                    if (entity1 instanceof Bullet) {
                        Bullet bullet = (Bullet) entity1;

                        if (bullet.getShooter() instanceof Player) {
                            if (entity2 instanceof Asteroid) {
                                entity2.setHealth(entity2.getHealth() - 1);
                                ((Asteroid) entity2).setHit(true);
                            }

                            if (entity2 instanceof Enemy) {
                                entity2.setHealth(entity2.getHealth() - 1);
                                if (entity2.getHealth() <= 0) {
                                    world.removeEntity(entity2);
                                }
                            }
                        }

                        if (bullet.getShooter() instanceof Enemy) {
                            if (entity2 instanceof Asteroid) {
                                entity2.setHealth(entity2.getHealth() - 1);
                                ((Asteroid) entity2).setHit(true);
                            }

                            if (entity2 instanceof Player) {
                                entity2.setHealth(entity2.getHealth() - 1);
                            }
                        }

                        world.removeEntity(entity1); // Remove bullet
                    }
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
