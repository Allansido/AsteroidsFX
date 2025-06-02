package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for processing entities during the game loop.
 * Implementations of this interface define how entities are updated based on game data.
 */
public interface IEntityProcessingService {

    /**
     * Processes entities in the game world.
     *
     * Preconditions:
     * - `gameData` must not be null.
     * - `world` must not be null and should contain entities to process.
     *
     * Postconditions:
     * - Entities in the `world` may have updated positions, states, or other attributes.
     *
     * @param gameData The game data containing information about the game state.
     * @param world The game world containing the entities to process.
     */
    void process(GameData gameData, World world);
}