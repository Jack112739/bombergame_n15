package entities.Item;

import gameInterface.GameMap;
import graphics.Sprite;
import javafx.scene.image.Image;

public class FlamePass extends Item {

    public FlamePass(int x, int y, Image img) {
        super(x, y, null);
    }

    @Override
    public void ItemUse(GameMap map) {

    }

    @Override
    public void update(long now, GameMap map) {

    }

    @Override
    public Sprite getAnimation() {
        return null;
    }

}
