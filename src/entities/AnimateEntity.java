package entities;

import graphics.Sprite;
import javafx.scene.image.Image;

/**
 * 1 class có animation (ảnh động) hoặc có thời gian tồn tại
 * animation speed : tốc độ hoạt ảnh chạy của các animation.
 */
public abstract class AnimateEntity extends Entity {
    public static final int animationSpeed = 5;

    public AnimateEntity(int x, int y, Image img) {
        super(x, y, img);
    }

    public AnimateEntity(int x, int y) {
        super(x, y);
    }

    /**
     * @return animation của class đó.
     */
    public abstract Sprite getAnimation();

    /**
     * @return true nếu object player chạm vào là chết và false nếu ngược lại
     */
    public abstract boolean isDangerous();

    /**
     * @return trả về true nếu object hết thời gian tồn tại
     */
    public abstract boolean expired();
}