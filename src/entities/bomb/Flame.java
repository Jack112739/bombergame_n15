package entities.bomb;

import entities.AnimateEntity;
import gameInterface.GameMap;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.scene.image.Image;

/**
 * 1 flame đơn giản được sinh ra sau khi bomb nổ
 * thuộc tính time : bộ đếm animation
 * LIFE_TIME: thời gian tồn tại ( theo fps tức là LIFE_TIME/60 giây) của flame
 */
public class Flame extends AnimateEntity {
    public static final Image DEFAULT_IMG = new Sprite(SpriteSheet.background, Sprite.ScaleSize)
        .getImageAt(3, 0);
    public static long LIFE_TIME = 50;
    protected int time;

    public Flame(int x, int y) {
        super(x, y, DEFAULT_IMG);
    }

    @Override
    public Sprite getAnimation() {
        return null;
    }

    @Override
    public boolean isDangerous() {
        return true;
    }

    @Override
    public boolean expired() {
        return time > LIFE_TIME;
    }


    @Override
    public void update(long now, GameMap map) {
        time++;
    }
}
