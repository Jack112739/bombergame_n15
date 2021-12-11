package entities.background;

import entities.BackGroundEntity;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.scene.image.Image;

/**
 * entity đại diện cho trường trong map không thể đi qua và cũng không thể bị phá hủy bởi bom.
 */
public class Wall extends BackGroundEntity {

    public static final Image DEFAULT_IMG = new Sprite(SpriteSheet.background, Sprite.DEFAULT_SIZE)
        .getImageAt(0, 0);

    public Wall(int x, int y) {
        super(x, y, DEFAULT_IMG);
    }
}
