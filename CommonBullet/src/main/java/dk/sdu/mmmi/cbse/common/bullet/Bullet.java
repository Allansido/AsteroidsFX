package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author corfixen
 */
public class Bullet extends Entity {

    private Entity shooter;

    public Bullet(Entity shooter) {
        this.shooter = shooter;
        setMoveSpeed(400);
    }

    public Entity getShooter() {
        return shooter;
    }
}
