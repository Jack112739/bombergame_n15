package entities.Item;

import gameInterface.GameMap;
import graphics.Sprite;
import javafx.scene.image.Image;

public class PowerupItem extends Item {

    public PowerupItem(int x, int y, Image image) {
        super(x, y, image);
    }

    @Override
    public void update(long now, GameMap map) {
        if (isCollide(map.getPlayer(), this)) {
            check = false;
        }
        ItemUse(map);
    }

    @Override
    public Sprite getAnimation() {
        return null;
    }

    @Override
    public void ItemUse(GameMap map) {

    }
}
