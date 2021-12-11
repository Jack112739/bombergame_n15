package entities.moveableEntities;

import gameInterface.GameMap;
import graphics.Sprite;
import graphics.SpriteSheet;

/**
 * enemies đơn giản, di chuyển ngẫu nhiên (random).
 */
public class DumbEnemies extends MoveAbleEntity {
    public static long DEAD_TIME = 200;
    private static Sprite sprite = null;

    /**
     * constructor mặc định.
     * @param x,y vị trí của enemies này trong map.
     */
    public DumbEnemies(int x, int y) {
        super(x, y, null);
        dir = Direction.DOWN;
        time = 0;
        img = getImgBaseOnAnimation();
    }

    /**
     * map 1 số thực random về direction
     * @return 1 direction ngẫu nhiên, không bao gồm chết.
     */
    static Direction randomDirection() {
        int rand = (int) (Math.random() * 5);
        switch (rand) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.LEFT;
            case 3:
                return Direction.RIGHT;
            default:
                return Direction.STATIC;
        }
    }

    @Override
    public Sprite getAnimation() {
        if (sprite == null) {
            sprite = new Sprite(SpriteSheet.dumbEnemies, Sprite.DEFAULT_SIZE);
        }
        return sprite;
    }

    @Override
    public boolean isDangerous() {
        return dir != Direction.DEAD;
    }

    @Override
    public boolean expired() {
        return dir == Direction.DEAD && time > DEAD_TIME;
    }

    @Override
    public void update(long now, GameMap map) {
        if (dir == Direction.DEAD) {
            time++;
            img = getImgBaseOnAnimation();
            return;
        }
        double random = Math.random();
        if (random > 0.9) {
            dir = randomDirection();
        }
        move(dir, map);
    }


    @Override
    public int getSpeed() {
        return 1;
    }
}
