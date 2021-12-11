package entities;

import entities.bomb.Flame;
import gameInterface.GameMap;
import graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * đóng vai trò như 1 background của các entities trong game
 *
 * thuộc tính entiites list : linkedlist gồm các animated entites đang đè lên backgound hiện tại.
 */
public abstract class BackGroundEntity extends Entity {

    private LinkedList<AnimateEntity> entitiesList;
    private boolean makeFire;

    public BackGroundEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        entitiesList = new LinkedList<>();
        makeFire = false;
    }

    public LinkedList<AnimateEntity> getList() {
        return entitiesList;
    }

    public void setList(LinkedList<AnimateEntity> list) {
        this.entitiesList = list;
    }

    /**
     * render tất cả các entites trong list.
     */
    public void renderAll(WritableImage mapImg, Rectangle pointOfView) {
        for (Entity e : entitiesList) {
            e.render(mapImg, pointOfView);
        }
    }

    @Override
    public void update(long now, GameMap map) {
        Iterator<AnimateEntity> it = entitiesList.iterator();
        // duyệt bẳng linked list iterator để chạy nhanh hơn
        while (it.hasNext()) {
            AnimateEntity e = it.next();
            e.update(now, map);
            if (e.expired()) {
                // nếu entities đã hết thời gian tồn tại thì xóa khỏi list.
                it.remove();
            } else {
                int deltaX = e.x - x;
                int deltaY = e.y - y;
                // kiểm tra xem entities hiện tại còn đang ở đúng background mà nó đè lên không.
                if (Math.abs(deltaX) > Sprite.DEFAULT_SIZE / 2 || Math.abs(deltaY) > Sprite.DEFAULT_SIZE / 2) {
                    deltaX /= (double) Sprite.DEFAULT_SIZE / 2;
                    deltaY /= (double) Sprite.DEFAULT_SIZE / 2;
                    it.remove();
//                  System.out.printf("%s %d %d %d %d\n",e, x, y, x / Sprite.ScaleSize, y / Sprite.ScaleSize);
                    map.getBackGround()
                        [y / Sprite.DEFAULT_SIZE + deltaY][x / Sprite.DEFAULT_SIZE + deltaX]
                        .getList()
                        .add(e);
                }
            }
        }
        // nếu thêm lửa thì add 1 flame vào entites list ( java không cho vừa thêm vừa xóa)
        if (makeFire) {
            entitiesList.addFirst(new Flame(
                (x + Sprite.DEFAULT_SIZE / 2) / Sprite.DEFAULT_SIZE,
                (y + Sprite.DEFAULT_SIZE / 2) / Sprite.DEFAULT_SIZE));
            makeFire = false;
        }
    }

    public void addFlame() {
        makeFire = true;
    }
}
