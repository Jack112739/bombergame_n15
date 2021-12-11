package entities;

import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.scene.image.Image;

/**
 * entities portal ( có thể thay đổi thành animated nếu cần)
 * đảm nhận như 1 vật để kiểm tra xem player đã thắng chưa tùy theo game mode.
 */
public class Portal extends BackGroundEntity {
    public static final Image DEFAULT_IMG = new Sprite(SpriteSheet.background, Sprite.DEFAULT_SIZE)
        .getImageAt(5, 0);

    public Portal(int x, int y) {
        super(x, y, DEFAULT_IMG);
    }
}
