package entities.background;

import entities.BackGroundEntity;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.scene.image.Image;

/**
 * entity grass : mọi thứ đều có thể đi qua được.
 */
public class Grass extends BackGroundEntity {

    public static final Image DEFAULT_IMG = new Sprite(SpriteSheet.background, Sprite.DEFAULT_SIZE)
        .getImageAt(2, 0);

    public Grass(int x, int y) {
        super(x, y, DEFAULT_IMG);
    }

}
