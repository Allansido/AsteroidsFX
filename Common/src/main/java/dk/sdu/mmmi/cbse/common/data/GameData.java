package dk.sdu.mmmi.cbse.common.data;

public class GameData {

    private int displayWidth  = 700 ;
    private int displayHeight = 700;
    private final GameKeys keys = new GameKeys();
    private double deltaTime;


    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }
    public double getDeltaTime() {
        return deltaTime;
    }

}
