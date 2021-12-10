package entities.Item;

import entities.AnimateEntity;
import gameInterface.GameMap;
import javafx.scene.image.Image;

/**
 * đại diện cho 1 item có thể sử dụng trong game.
 */
public abstract class Item extends AnimateEntity {
    protected boolean check;

    public Item(int x, int y, Image img) {
        super(x, y, img);
        check = true;
    }

    @Override
    public boolean isDangerous() {
        return false;
    }

    @Override
    public boolean expired() {
        return check;
    }

    public abstract void ItemUse(GameMap map);
}
