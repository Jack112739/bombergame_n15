package entities.background;

import entities.BackGroundEntity;
import entities.Item.BombPass;
import entities.Item.FlamePass;
import entities.Item.Item;
import entities.Item.PowerupItem;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.scene.image.Image;

/**
 * 1 dạng wall đặc biệt, có thể bị phá hủy bởi bom.
 */
public class BreakableWall extends BackGroundEntity {

    public static final Image DEFAULT_IMG = new Sprite(SpriteSheet.background, Sprite.DEFAULT_SIZE)
        .getImageAt(1, 0);

    public BreakableWall(int x, int y) {
        super(x, y, DEFAULT_IMG);
    }

    public Item getRandomItem() {
        double random = Math.random();
        if(random < 0.1) return new PowerupItem(x, y, null);
        else if(random < 0.2) return new BombPass(x, y, null);
        else if(random < 0.3)return new FlamePass(x, y, null);
        else return null;
    }
}
