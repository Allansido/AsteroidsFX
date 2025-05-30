package dk.sdu.mmmi.cbse.common.map;

import javafx.scene.image.ImageView;

/**
 * Interface for providing map-related functionality in the game.
 * Implementations of this interface define how maps are retrieved or displayed.
 */
public interface MapSPI {

    /**
     * Retrieves the map as an `ImageView` object.
     *
     * Preconditions:
     * - The map must be available and properly initialized.
     *
     * Postconditions:
     * - An `ImageView` object representing the map is returned.
     *
     * @return The map as an `ImageView` object.
     */
    ImageView getMap();
}