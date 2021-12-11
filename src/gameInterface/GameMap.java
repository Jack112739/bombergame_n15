package gameInterface;

import entities.background.*;
import entities.moveableEntities.*;
import entities.*;
import graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class đảm biểu diễn 1 map trong game và load lên app.
 *
 * thuộc tính drawX, drawY : tọa độ hiển thị của map.
 * thuộc tính mapImg: hình ảnh của map thông qua point of view từ player (như 1 ảnh snapshot)
 * thuộc tính pointOfView : hình chữ nhật đại diện cho những gì player nhìn thấy trong map.
 * thuộc tính background : là 1 mảng 2 chiều gồm các NonAnimateEntity đảm nhận trách nghiệm biểu diễn map dễ hơn
 * thuộc tính player : player của map
 */
public class GameMap extends AnimationTimer implements EventHandler<KeyEvent> {
    public final int DISPLAY_BORDER;

    private final WritableImage mapImg;
    private final Rectangle pointOfView;
    private BackGroundEntity[][] background;
    private Player player;

    /**
     * hàm khởi tạo chuẩn của class Map.
     *
     * @param mapPath đường dẫn của map
     * @param w chiều rộng của point of view từ player tính theo pixel.
     * @param h chiều cao của point of view từ player tính theo pixel.
     * @throws IOException nếu không load được map từ data, và kết thúc luôn chương trình.
     */
    public GameMap(String mapPath, int w, int h, int borderLength) {
        this.pointOfView = new Rectangle(0, 0, w, h);
        DISPLAY_BORDER = borderLength;

        int mapWidth, mapHeight = 0;
        ArrayList<String> files = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapPath));
            String line;
            while ((line = reader.readLine()) != null) {
                files.add(line);
                mapHeight++;
            }
            if (mapHeight > 0) {
                mapWidth = files.get(0).length();
            } else {
                mapWidth = 0;
            }
            background = new BackGroundEntity[mapHeight][mapWidth];
            for (int i = 0; i < mapHeight; i++) {
                for (int j = 0; j < mapWidth; j++) {
                    Entity e = convert(files.get(i).charAt(j), j, i);
                    if (e instanceof BackGroundEntity) {
                        background[i][j] = (BackGroundEntity) e;
                    } else if (e instanceof AnimateEntity) {
                        background[i][j] = new Grass(j, i);
                        background[i][j].getList().add((AnimateEntity) e);
                        if (e instanceof Player) {
                            player = (Player) e;
                            player.setMap(this);
                        }
                    }
                }
            }
            if (pointOfView.height > Sprite.DEFAULT_SIZE * getHeight() || pointOfView.height == 0) {
                pointOfView.height = Sprite.DEFAULT_SIZE * getHeight();
            }
            if (pointOfView.width > Sprite.DEFAULT_SIZE * getWidth() || pointOfView.width == 0) {
                pointOfView.width = Sprite.DEFAULT_SIZE * getWidth();
            }
            setView();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can not load " + mapPath);
            System.exit(2);
        }
        mapImg = new WritableImage(pointOfView.width, pointOfView.height);
    }

    /**
     * constructor theo level
     */
    public GameMap(int level, int w, int h, int borderLength) {
        this("data/map/level" + level + ".txt", w, h, borderLength);
    }

    /**
     *  constructor mặc định độ dài của border = 2 * defaultSize của ảnh;
     */
    public GameMap(int level, int w, int h) {
        this(level, w, h, Sprite.DEFAULT_SIZE * 2);
    }

    /**
     * constructor mặc định.
     */
    public GameMap(int level) {
        this(level, 0, 0, Sprite.DEFAULT_SIZE * 2);
    }

    /**
     * dùng để ảnh xạ 1 character từ file .txt (tức data của map) tới 1 entities được vài và định nghĩa ở dưới
     * vị trí của entities sẽ phụ thuộc vào posX và posY truyền vào.
     *
     * @param c ký tự để map
     * @param posX vị trí x trong mảng background.
     * @param posY vị trí y trong mảng background.
     * @return entities được map.
     */
    public static Entity convert(char c, int posX, int posY) {
        switch (c) {
            case '#':
                return new Wall(posX, posY);
            case 'P':
                return new Player(posX, posY);
            case 'S':
                return new Spike(posX, posY);
            case '*':
                return new BreakableWall(posX, posY);
            case 'E':
                return new DumbEnemies(posX, posY);
            case 'O':
                return new Portal(posX, posY);
            default:
                return new Grass(posX, posY);
        }
    }


    /**
     * hàm để lấy thông tin từ map
     *
     * @return mảng 2 chiều biểu diễn thông tin của map.
     */
    public BackGroundEntity[][] getBackGround() {
        return background;
    }

    /**
     * @return trả về chiều rộng của map.
     */
    public int getWidth() {
        return (background.length == 0) ? 0 : background[0].length;
    }

    /**
     * @return trả về chiều cao của map.
     */

    public int getHeight() {
        return background.length;
    }


    /**
     * hàm dùng để vẽ map ra màn hình, chỉ vẽ những back ground từ vị trí point of view của player.
     */
    void render() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                Point p1 = new Point(j * Sprite.DEFAULT_SIZE, i * Sprite.DEFAULT_SIZE);
                Point p2 = new Point((j + 1) * Sprite.DEFAULT_SIZE, (i + 1) * Sprite.DEFAULT_SIZE);
                if (pointOfView.contains(p1) || pointOfView.contains(p2)) {
                    background[i][j].renderAll(mapImg, pointOfView);
                }
            }
        }
    }

    /**
     * hàm sẽ được gọi để cập nhật map trong mỗi frame (fps của map là 60 fps)
     * @param now thời gian hiện tại
     */
    @Override
    public void handle(long now) {
        if (player == null) {
            return;
        }
        // xử lý trường hợp player chết
        if (player.expired()) {
            player = null;
            return;
        }
        this.setView();
        // vẽ backgound của map đồng thời cập nhật các entities trong map
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                Point p1 = new Point(j * Sprite.DEFAULT_SIZE, i * Sprite.DEFAULT_SIZE);
                Point p2 = new Point((j + 1) * Sprite.DEFAULT_SIZE, (i + 1) * Sprite.DEFAULT_SIZE);
                //System.out.println(p); // debug
                if (pointOfView.contains(p1) || pointOfView.contains(p2)) {
                    background[i][j].render(mapImg, pointOfView);
                }
                background[i][j].update(now, this);
            }
        }
        render();
        //  check(); // debug.
    }

    /**
     * thay đổi vị trí x và y của hình chữ nhật point of view phụ thuộc vào vị trí đứng của player.
     */
    private void setView() {
        if (player.getX() < DISPLAY_BORDER) {
            pointOfView.x = 0;
        } else if (player.getX() > getWidth() * Sprite.DEFAULT_SIZE - DISPLAY_BORDER) {
            pointOfView.x = getWidth() * Sprite.DEFAULT_SIZE - pointOfView.width;
        } else if (player.getX() < pointOfView.x + DISPLAY_BORDER) {
            pointOfView.x = player.getX() - DISPLAY_BORDER;
        } else if (player.getX() > pointOfView.x + pointOfView.width - DISPLAY_BORDER) {
            pointOfView.x = player.getX() - pointOfView.width + DISPLAY_BORDER;
        }

        if (player.getY() < DISPLAY_BORDER) {
            pointOfView.y = 0;
        } else if (player.getY() > getHeight() * Sprite.DEFAULT_SIZE - DISPLAY_BORDER) {
            pointOfView.y = getHeight() * Sprite.DEFAULT_SIZE - pointOfView.height;
        } else if (player.getY() < pointOfView.y + DISPLAY_BORDER) {
            pointOfView.y = player.getY() - DISPLAY_BORDER;
        } else if (player.getY() > pointOfView.y + pointOfView.height - DISPLAY_BORDER) {
            pointOfView.y = player.getY() - pointOfView.height + DISPLAY_BORDER;
        }
        //System.out.println(pointOfView); //debug
    }

    /**
     * debug, đại loại là in ra map.
     */
    private void check() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (background[i][j] instanceof Wall) {
                    System.out.print('#');
                } else if (background[i][j] instanceof BreakableWall) {
                    System.out.print('X');
                } else if (background[i][j].getList().size() > 2) {
                    System.out.print('2');
                } else if (background[i][j].getList().size() == 1) {
                    AnimateEntity e = background[i][j].getList().getFirst();
                    if (e instanceof Player) {
                        System.out.print('P');
                    } else if (e instanceof DumbEnemies) {
                        System.out.print('E');
                    }
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if(player == null ) return;
        player.handle(event);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * @return point of view của player theo khối hình chữ nhật
     */
    public Rectangle getPointOfView() {
        return (Rectangle) pointOfView.clone();
    }

    /**
     * @return hình ảnh biểu diễn map thông qua poin of view từ player.
     */
    public Image getImage() {
        return mapImg;
    }

    public enum Status {
        PLAYER_LOSE,
        GAME_ON,
        PLAYER_WIN
    }

    /**
     * trạng thái của map, có lẽ phải override tùy vào từng loại map
     */
    public Status status() {
        if(player == null || player.expired()) return Status.PLAYER_LOSE;
        else if(player.getX() + player.getY() > 400) return Status.PLAYER_WIN; // để test thôi
        else return Status.GAME_ON;
    }
}