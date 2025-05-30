package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Interface for creating bullets in the game.
 * Implementations of this interface define how bullets are generated based on entities and game data.
 */
public interface BulletSPI {

    /**
     * Creates a bullet entity based on the given entity and game data.
     *
     * Preconditions:
     * - `e` must not be null and should represent the entity firing the bullet.
     * - `gameData` must not be null and should contain information about the game state.
     *
     * Postconditions:
     * - A new bullet entity is created and returned.
     *
     * @param e The entity firing the bullet.
     * @param gameData The game data containing information about the game state.
     * @return A new bullet entity.
     */
    Entity createBullet(Entity e, GameData gameData);
}