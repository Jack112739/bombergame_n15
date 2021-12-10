package gameInterface;

import entities.background.*;
import entities.moveableEntities.*;
import entities.*;
import entities.Item.*;
import graphics.Sprite;
import graphics.SpriteSheet;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
 * thuộc tính mapGC GraphicContext của map này, dùng để vẽ map...
 * thuộc tính pointOfView : hình chữ nhật đại diện cho những gì player nhìn thấy trong map.
 * thuộc tính background : là 1 mảng 2 chiều gồm các NonAnimateEntity đảm nhận trách nghiệm biểu diễn map dễ hơn
 * thuộc tính player và portal : player và portal của map
 */
public class GameMap extends AnimationTimer implements EventHandler<KeyEvent> {
    public static final int DISPLAY_BORDER = (int) (Sprite.ScaleSize * 3.5);

    public final double drawX, drawY;
    private final GraphicsContext mapGC;
    private final Rectangle pointOfView;
    private BackGroundEntity[][] background;
    private Player player;
    private Portal portal;

    /**
     * hàm khởi tạo chuẩn của class Map.
     *
     * @param mapGC graphicContext của map.
     * @param level level sẽ được load từ map.
     * @param drawX,drawY vị trí x bắt đầu vẽ của map tính từ góc trên bên trái là (0,0)
     * @param scale tỷ lệ các sprite ảnh cần tăng so với ảnh gốc.
     * @param w chiều rộng của point of view từ player.
     * @param h chiều cao của point of view từ player.
     * @throws IOException nếu không load được map từ data, và kết thúc luôn chương trình.
     */
    public GameMap(GraphicsContext mapGC, int level, double drawX, double drawY, double scale, int w, int h) {
        this.mapGC = mapGC;
        this.drawX = drawX;
        this.drawY = drawY;
        this.pointOfView = new Rectangle(0, 0, w * Sprite.ScaleSize, h * Sprite.ScaleSize);
        Sprite.ScaleSize = (int) (SpriteSheet.DEFAULT_SIZE * scale);

        String mapPath = "data/level" + level + ".txt";
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
                        if (e instanceof Portal) {
                            this.portal = (Portal) e;
                        }
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
            if (pointOfView.height > getHeight() * Sprite.ScaleSize) {
                pointOfView.height = getHeight() * Sprite.ScaleSize;
            }
            if (pointOfView.width > getWidth() * Sprite.ScaleSize) {
                pointOfView.width = getWidth() * Sprite.ScaleSize;
            }
            setView();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can not load " + mapPath);
            System.exit(2);
        }
    }

    /**
     * hàm khởi tạo nhưng mặc định scale = 1.
     */
    public GameMap(GraphicsContext mapGC, int level, double drawX, double drawY, int w, int h) {
        this(mapGC, level, drawX, drawY, 1, w, h);
    }

    /**
     * hàm khởi tạo nhưng mặc định point of view của player phù hợp với map.
     */
    public GameMap(GraphicsContext mapGC, int level, double drawX, double drawY) {
        this(mapGC, level, drawX, drawY, 0, 0);
        pointOfView.width = (int) mapGC.getCanvas().getWidth();
        pointOfView.height = (int) mapGC.getCanvas().getHeight();
        setView();
    }

    /**
     * constructor mặc định.
     */
    public GameMap(GraphicsContext mapGC, int level) {
        this(mapGC, level, 0, 0);
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
                Point p = new Point(j * Sprite.ScaleSize, i * Sprite.ScaleSize);
                if (pointOfView.contains(p)) {
                    background[i][j].renderAll(mapGC);
                }
            }
        }
    }

    /**
     * hàm sẽ được gọi để cập nhật map trong mỗi frame (fps của map là 60 fps)
     * @param now
     */
    @Override
    public void handle(long now) {
        if (player == null) {
            return;
        }
        // vẽ backgound
        mapGC.setFill(Color.BLACK);
        mapGC.fillRect(0, 0, mapGC.getCanvas().getWidth(), mapGC.getCanvas().getHeight());
        // xử lý trường hợp player chết
        if (player.expired()) {
            mapGC.setFill(Color.RED);
            mapGC.setFont(Font.font(30));
            mapGC.fillText("Omea wa mou shindeiru", 200, 100);
            player = null;
            return;
        }
        this.setView();
        // vẽ backgound của map đồng thời cập nhật các entities trong map
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                Point p = new Point(j * Sprite.ScaleSize, i * Sprite.ScaleSize);
                if (pointOfView.contains(p)) {
                    background[i][j].render(mapGC);
                }
                background[i][j].update(now, this);
            }
        }
        render();
        // xử lý khi player kết thức màn chơi
        if (Entity.euclidDistance(player, portal) < Sprite.ScaleSize * 0.5) {
            mapGC.setFill(Color.GRAY);
            mapGC.setFont(Font.font(30));
            mapGC.fillText("YOU WIN! SCORE = " + player.score, 200, 500);
        }
        //  check(); // debug.
    }

    /**
     * thay đổi vị trí x và y của hình chữ nhật point of view phụ thuộc vào vị trí đứng của player.
     */
    private void setView() {
        if (player.getX() < DISPLAY_BORDER) {
            pointOfView.x = 0;
        } else if (player.getX() > getWidth() * Sprite.ScaleSize - DISPLAY_BORDER) {
            pointOfView.x = getWidth() * Sprite.ScaleSize - pointOfView.width;
        } else if (player.getX() < pointOfView.x + DISPLAY_BORDER) {
            pointOfView.x = player.getX() - DISPLAY_BORDER;
        } else if (player.getX() > pointOfView.x + pointOfView.width - DISPLAY_BORDER) {
            pointOfView.x = player.getX() - pointOfView.width + DISPLAY_BORDER;
        }

        if (player.getY() < DISPLAY_BORDER) {
            pointOfView.y = 0;
        } else if (player.getY() > getHeight() * Sprite.ScaleSize - DISPLAY_BORDER) {
            pointOfView.y = getHeight() * Sprite.ScaleSize - pointOfView.height;
        } else if (player.getY() < pointOfView.y + DISPLAY_BORDER) {
            pointOfView.y = player.getY() - DISPLAY_BORDER;
        } else if (player.getY() > pointOfView.y + pointOfView.height - DISPLAY_BORDER) {
            pointOfView.y = player.getY() - pointOfView.height + DISPLAY_BORDER;
        }
        Sprite.RenderX = -pointOfView.x + (int) drawX;
        Sprite.RenderY = -pointOfView.y + (int) drawY;
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
        if (Entity.euclidDistance(player, portal) < Sprite.ScaleSize * 0.5) {
            return;
        }
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
}