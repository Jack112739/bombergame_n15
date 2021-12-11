package entities.background;

import entities.BackGroundEntity;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.scene.image.Image;

/**
 * entity spike, nếu player hoặc bất enemies chạm vào sẽ chết luôn.
 */
public class Spike extends BackGroundEntity {
    public static final Image DEFAULT_IMG = new Sprite(SpriteSheet.background, Sprite.DEFAULT_SIZE)
        .getImageAt(4, 0);

    public Spike(int x, int y) {
        super(x, y, DEFAULT_IMG);
    }
}