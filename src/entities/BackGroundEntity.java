package entities;

import entities.bomb.Bomb;
import entities.bomb.Flame;
import gameInterface.GameMap;
import graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * đóng vai trò như 1 background của các entities trong game
 *
 * thuộc tính entiites list : linkedlist gồm các animated entites đang đè lên backgound hiện tại.
 */
public abstract class BackGroundEntity extends Entity {

    private LinkedList<AnimateEntity> entitiesList;

    public BackGroundEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        entitiesList = new LinkedList<>();
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
    public void renderAll(GraphicsContext gc) {
        for (Entity e : entitiesList) {
            e.render(gc);
        }
    }

    @Override
    public void update(long now, GameMap map) {
        Iterator<AnimateEntity> it = entitiesList.iterator();
        boolean makeFire = false;
        // duyệt bẳng linked list iterator để chạy nhanh hơn
        while (it.hasNext()) {
            AnimateEntity e = it.next();
            e.update(now, map);
            if (e.expired()) {
                // nếu entities đã hết thời gian tồn tại thì xóa khỏi list.
                if (e instanceof Bomb) {
                    makeFire = true;
                }
                it.remove();
            } else {
                int deltaX = e.x - x;
                int deltaY = e.y - y;
                // kiểm tra xem entities hiện tại còn đang ở đúng background mà nó đè lên không.
                if (Math.abs(deltaX) > Sprite.ScaleSize / 2 || Math.abs(deltaY) > Sprite.ScaleSize / 2) {
                    deltaX /= (double) Sprite.ScaleSize / 2;
                    deltaY /= (double) Sprite.ScaleSize / 2;
                    it.remove();
//                  System.out.printf("%s %d %d %d %d\n",e, x, y, x / Sprite.ScaleSize, y / Sprite.ScaleSize);
                    map.getBackGround()
                        [y / Sprite.ScaleSize + deltaY][x / Sprite.ScaleSize + deltaX]
                        .getList()
                        .add(e);
                }
            }
        }
        // nếu thêm lửa thì add 1 flame vào entites list ( java không cho vừa thêm vừa xóa)
        if (makeFire) {
            entitiesList.addFirst(new Flame(
                (x + Sprite.ScaleSize / 2) / Sprite.ScaleSize,
                (y + Sprite.ScaleSize / 2) / Sprite.ScaleSize));
        }
    }
}
