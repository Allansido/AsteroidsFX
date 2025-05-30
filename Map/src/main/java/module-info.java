import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.map.MapPlugin;
import dk.sdu.mmmi.cbse.common.map.MapSPI;

module Map {
    requires Common;
    requires CommonMap;
    requires javafx.graphics;
    requires java.desktop;
    provides IGamePluginService with MapPlugin;
    provides MapSPI with MapPlugin;
}