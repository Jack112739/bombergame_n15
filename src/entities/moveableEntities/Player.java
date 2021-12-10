package entities.moveableEntities;

import entities.AnimateEntity;
import entities.BackGroundEntity;
import entities.bomb.Bomb;
import entities.background.Spike;
import gameInterface.GameMap;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;


public class Player extends MoveAbleEntity implements EventHandler<KeyEvent> {
    public static final int DEAD_TIME = 100;
    private static Sprite sprite = null;
    public int score;
    private GameMap map;
    private int speed = 4;

    public Player(int x, int y) {
        super(x, y, null);
        dir = Direction.DOWN;
        time = 0;
        img = getImgBaseOnAnimation();
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    @Override
    public Sprite getAnimation() {
        if (sprite == null) {
            sprite = new Sprite(SpriteSheet.player, Sprite.ScaleSize);
        }
        return sprite;
    }

    @Override
    public boolean isDangerous() {
        return false;
    }

    @Override
    public boolean expired() {
        return dir == Direction.DEAD && time > DEAD_TIME;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public void dropBomb() {
        //  Sound v = new Sound();
        // v.createSound(Sound.PLACE_BOMB);
//        System.out.printf("add bomb! %d %d %d %d\n", x, y,
//            (x + Sprite.ScaleSize/2)/Sprite.ScaleSize,
//            (y + Sprite.ScaleSize/2)/Sprite.ScaleSize);
        map.getBackGround()
            [(y + Sprite.ScaleSize / 2) / Sprite.ScaleSize][(x + Sprite.ScaleSize / 2) / Sprite.ScaleSize]
            .getList()
            .add(new Bomb(x, y));
    }

    @Override
    public void update(long now, GameMap m) {
        if (dir == Direction.DEAD) {
            //if(sd != null)  sd = new Sound();
            // sd.createSound(Sound.DEAD);
            time++;
            img = getImgBaseOnAnimation();
        }
        int curX = (x + Sprite.ScaleSize / 2) / Sprite.ScaleSize;
        int curY = (y + Sprite.ScaleSize / 2) / Sprite.ScaleSize;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BackGroundEntity e = m.getBackGround()[curY + i][curX + j];
                if (e instanceof Spike && isCollide(e, this)) {
                    this.kill();
                    return;
                }
                for (AnimateEntity entity : e.getList()) {
                    if (entity.isDangerous() && isCollide(entity, this)) {
                        this.kill();
                        return;
                    }
                }
            }
        }

    }

    @Override
    public void handle(KeyEvent event) {
        if (dir == Direction.DEAD) {
            return;
        }

        switch (event.getCode()) {
            case ENTER:
                dropBomb();
                break;
            case UP:
                move(Direction.UP, map);
                break;
            case DOWN:
                move(Direction.DOWN, map);
                break;
            case LEFT:
                move(Direction.LEFT, map);
                break;
            case RIGHT:
                move(Direction.RIGHT, map);
                break;
            default:
                break;
        }
    }
}
