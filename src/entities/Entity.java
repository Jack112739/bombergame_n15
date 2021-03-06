package entities;

import gameInterface.GameMap;
import graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.*;

/**
 * 1 vật thể trong game mọi thứ có trong game đều là entity
 * 1 entity có thể là 1 entity rỗng tức không cần làm gì cả.
 *
 * thuộc tính x : tạo độ x ở trong map tính theo pixel
 * thuộc tính y : tọa độ y ở trong map tính theo pixel
 * thuộc tính img : image hiện tại của entities.
 */
public class Entity {
    protected int x;
    protected int y;

    protected Image img;

    /**
     *
     * @param xUnit,yUnit vị trí trong map không tính theo pixel
     * @param img ảnh hiện tại của map
     */
    public Entity(int xUnit, int yUnit, Image img) {
        this(xUnit * Sprite.DEFAULT_SIZE, yUnit * Sprite.DEFAULT_SIZE);
        this.img = img;
    }

    /**
     * constructor 1 entity theo vị trí pixel x và y
     * @param x,y vị trí của entity này tính theo pixel
     */
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
        img = null;
    }

    /**
     * xem 2 entites e1 và e2 có hit box chạm vào nhau không (để xử lý va chạm)
     * @return true nếu e1 chạm và e2 và false nếu ngược lại.
     */
    public static boolean isCollide(Entity e1, Entity e2) {
        if (e1 == null || e2 == null) {
            return false;
        }
        return (Math.abs(e1.getX() - e2.getX()) < Sprite.DEFAULT_SIZE - 10)
            && (Math.abs(e1.getY() - e2.getY()) < Sprite.DEFAULT_SIZE - 10);
    }

    /**
     * khoảng cách euclid giữa 2 entites e1, e2
     * @return sqrt(..^2 + ...^2)
     */
    public static double euclidDistance(Entity e1, Entity e2) {
        return Math.sqrt((e1.x - e2.x) * (e1.x - e2.x) + (e1.y - e2.y) * (e1.y - e2.y));
    }

    /**
     * vẽ ảnh vào trong image của map
     *
     * @param mapImg ảnh cần được vẽ đè.
     */
    public void render(WritableImage mapImg, Rectangle pointOfView) {
        PixelWriter pixel = mapImg.getPixelWriter();
        for(int i=y; i< y + img.getHeight(); i++) for(int j=x; j < x + img.getWidth(); j++) {
            Point point = new Point(j, i);
            int argb = img.getPixelReader().getArgb(j - x, i - y);
            if(pointOfView.contains(point) && argb != 0) {
                pixel.setArgb(j - pointOfView.x, i - pointOfView.y, argb);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * hàm để thực hiện việc cập nhật 1 hình ảnh trong canvas và sẽ được gọi trong mỗi frame
     * từ hàm handle của class GameMap.
     *
     * @param map Map của entites hiện tại
     */
    public void update(long now, GameMap map) {};
}
