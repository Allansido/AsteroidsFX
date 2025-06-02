package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for game plugins that manage the lifecycle of game entities.
 * Plugins can add or remove entities from the game world.
 */
public interface IGamePluginService {

    /**
     * Starts the plugin and initializes its entities in the game world.
     *
     * Preconditions:
     * - `gameData` must not be null.
     * - `world` must not be null.
     *
     * Postconditions:
     * - Entities related to the plugin are added to the `world`.
     *
     * @param gameData The game data containing information about the game state.
     * @param world The game world where entities will be added.
     */
    void start(GameData gameData, World world);

    /**
     * Stops the plugin and removes its entities from the game world.
     *
     * Preconditions:
     * - `gameData` must not be null.
     * - `world` must not be null.
     *
     * Postconditions:
     * - Entities related to the plugin are removed from the `world`.
     *
     * @param gameData The game data containing information about the game state.
     * @param world The game world where entities will be removed.
     */
    void stop(GameData gameData, World world);
}