package entities.Item;

import gameInterface.GameMap;
import graphics.Sprite;
import javafx.scene.image.Image;

public class BombPass extends Item {

    public BombPass(int x, int y, Image img) {
        super(x, y, img);
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
