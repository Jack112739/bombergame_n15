package entities.bomb;


import entities.AnimateEntity;
import entities.Entity;
import entities.BackGroundEntity;
import entities.moveableEntities.MoveAbleEntity;
import entities.background.BreakableWall;
import entities.background.Grass;
import entities.background.Wall;
import gameInterface.GameMap;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.scene.image.Image;

import java.util.LinkedList;

/**
 * entity này là 1 loại bom tiêu chuẩn, sẽ nổ khi được thả bởi player hoặc entity khác (cài đặt thêm)
 * thuộc tính firePower : sức công phá của bomb hiện tại.
 * thuộc tính time: bộ đếm animation
 */
public class Bomb extends AnimateEntity {
    public static int LIFE_TIME = 200;
    public static int firePower = 2;
    private static Sprite sprite = null;
    protected int time;

    public Bomb(int x, int y) {
        super(x, y);
        time = 0;
        img = getImgBaseOnTime();
    }

    /** cho bomb nổ và tỏa lửa theo hướng được biểu diễn bởi vector dx,dy trong map cho trước
     *
     * @param curX,curY vị trí hiện tại (trong mảng 2 chiều background) của entity trong map.
     * @param dx,dy vector biểu diễn hướng cần nổ.
     * @param map map hiện tại chứa bomb này.
     */
    private void explodeDirect(int curX, int curY, int dx, int dy, GameMap map) {
        int determine = (dx == 1 && dy == 0) ? 1 : 0;
        for (int i = 0; i < firePower + determine; i++) {
            curX += dx;
            curY += dy;
            BackGroundEntity current = map.getBackGround()[curY][curX];
            LinkedList<AnimateEntity> list = current.getList();
            // nếu là tường thì dừng
            if (current instanceof Wall) {
                break;
            } else if (current instanceof BreakableWall) {
                // tạo 1 grass mới và phá đi breakable wall này, phải cài đặt thêm item
                Grass grass = new Grass(curX, curY);
                grass.setList(current.getList());
                grass.addFlame();
                // add thêm item nếu có thể ?
                // Item item = ((BreakableWall) current).getRandomItem();
                // if(item != null) grass.getList().add(item);
                map.getBackGround()[curY][curX] = grass;
            }
            for (Entity e : list) {
                if (e instanceof MoveAbleEntity) {
                    ((MoveAbleEntity) e).kill();
                }
                // hiệu ứng nổ dây chuyền của bomb
                if(e instanceof Bomb) {
                    if(!((Bomb) e).expired()) ((Bomb) e).explode(map);
                }
            }
            current.addFlame();
            if (current instanceof BreakableWall) {
                break;
            }
        }
    }

    /**
     * hàm sẽ được gọi để kích nổ 1 entity bomb
     * @param map map hiện tại của bomb
     */
    private void explode(GameMap map) {
        // Sound sd = new Sound();
        // sd.createSound(Sound.BOMB_EXPLOSION);
        time = LIFE_TIME + 5; // huy bom hien tai nay
        int curX = (x + Sprite.ScaleSize / 2) / Sprite.ScaleSize;
        int curY = (y + Sprite.ScaleSize / 2) / Sprite.ScaleSize;
        explodeDirect(curX -1, curY, 1, 0, map);
        explodeDirect(curX, curY, 0, 1, map);
        explodeDirect(curX, curY, -1, 0, map);
        explodeDirect(curX, curY, 0, -1, map);
    }

    @Override
    public Sprite getAnimation() {
        if (sprite == null) {
            sprite = new Sprite(SpriteSheet.bomb, Sprite.ScaleSize);
        }
        return sprite;
    }

    @Override
    public boolean isDangerous() {
        return false;
    }

    @Override
    public boolean expired() {
        return time > LIFE_TIME;
    }

    /**
     * @return hoạt ảnh hiện tại của bomb, tùy theo thuộc tính time (bộ đếm animation)
     */
    public Image getImgBaseOnTime() {
        return getAnimation().getImageAt(time / 60, 0);
    }

    @Override
    public void update(long now, GameMap map) {
        if (time > LIFE_TIME-1) {
            explode(map);
        }
        time++;
        img = getImgBaseOnTime();
    }
}
