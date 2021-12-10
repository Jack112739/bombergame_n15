package entities.moveableEntities;

import entities.AnimateEntity;
import entities.Entity;
import entities.background.BreakableWall;
import entities.background.Grass;
import entities.background.Wall;
import gameInterface.GameMap;
import graphics.Sprite;
import javafx.scene.image.Image;

/**
 * 1 entity thuộc class này có thể di chuyển được theo 1 vài hướng được định nghĩ trong enum Direction
 * ví dụ bao gồm AI và playe.
 *
 * thuộc tính dir : direction hiện tại mà entity này đang hướng mặt vào
 * thuộc tính time : animation cycle của entity.
 */
public abstract class MoveAbleEntity extends AnimateEntity {
    protected Direction dir;
    protected int time;

    public MoveAbleEntity(int x, int y, Image img) {
        super(x, y, img);
    }

    /**
     * hàm ánh xạ ví trí của Direction sang 1 số nguyên dương.
     *
     * @param direction direction cần map
     * @return số thu được sau khi map direction.
     */
    public static int toInt(Direction direction) {
        switch (direction) {
            case UP:
                return 0;
            case DOWN:
                return 2;
            case LEFT:
                return 3;
            case RIGHT:
                return 1;
            case DEAD:
                return 4;
            default:
                return -1;
        }
    }

    /**
     * hàm lấy hoạt ảnh của entity có thể phải override lại tùy theo từng class.
     * @return image hoạt ảnh của entity tùy theo direction và time hiện tại
     */
    protected Image getImgBaseOnAnimation() {
        //System.out.println(dir);
        int current = time / animationSpeed;
        if (current > 6) {
            if (dir != Direction.DEAD) {
                time = 3;
            }
            current = 0;
        }
        //if(dir == Direction.DEAD) System.out.printf("%d %d %d\n",current, toInt(dir), time);
        return getAnimation().getImageAt(current, toInt(dir));
    }

    /**
     *  kiểm tra xem nếu dịch chuyển thêm dx và dy thì có chạm vào tường không
     * @param dx,dy vector của hướng để thử di chuyển
     * @param map bản đồ của entity hiện tại
     * @return true nếu có thể di chuyển được theo vector (dx,dy) và false nếu ngược lại.
     */
    public boolean notCollideWall(int dx, int dy, GameMap map) {
        boolean result = true;
        int posX = (x + dx + Sprite.ScaleSize / 2) / Sprite.ScaleSize;
        int posY = (y + dy + Sprite.ScaleSize / 2) / Sprite.ScaleSize;
        // thử di chuyển entity hiện tại đi dx và dy đơn vị để xem có colide với cái gì không.
        Entity test = new Entity(x + dx, y + dy);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Entity e = map.getBackGround()[posY + i][posX + j];
                if (isCollide(test, e) && (e instanceof Wall || e instanceof BreakableWall)) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * di chuyển entity theo hướng direction.
     *
     * @param dir hướng di chuyển của entity
     * @param map bản đồ hiện tại
     */
    protected void move(Direction dir, GameMap map) {
        switch (dir) {
            case UP:
                if (notCollideWall(0, -getSpeed(), map)) {
                    y -= getSpeed();
                }
                break;
            case DOWN:
                if (notCollideWall(0, +getSpeed(), map)) {
                    y += getSpeed();
                }
                break;
            case LEFT:
                if (notCollideWall(-getSpeed(), 0, map)) {
                    x -= getSpeed();
                }
                break;
            case RIGHT:
                if (notCollideWall(+getSpeed(), 0, map)) {
                    x += getSpeed();
                }
                break;
            case STATIC:
                return;
            default:
                break;
        }
        if (this.dir == dir) {
            time++;
        } else {
            time = 0;
            this.dir = dir;
        }
        img = getImgBaseOnAnimation();
    }

    /**
     * giết entity hiện tại, chiếu hoạt ảnh chết...
     */
    public void kill() {
        dir = Direction.DEAD;
    }

    /**
     * @return tốc độ của entity.
     */
    public abstract int getSpeed();
}
