package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for post-processing entities after the main game loop processing.
 * Implementations of this interface define additional logic to be applied to entities.
 */
public interface IPostEntityProcessingService {

    /**
     * Performs post-processing on entities in the game world.
     *
     * Preconditions:
     * - `gameData` must not be null.
     * - `world` must not be null and should contain entities to process.
     *
     * Postconditions:
     * - Entities in the `world` may have additional updates or cleanup applied.
     *
     * @param gameData The game data containing information about the game state.
     * @param world The game world containing the entities to process.
     */
    void process(GameData gameData, World world);
}