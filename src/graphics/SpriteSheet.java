package graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 *  các cụm hình ảnh trong game sẽ được lưu trữ vào các sprite riêng biệt.
 *  class này quản lý các ảnh đó.
 *
 *  size: kích cỡ thực từ ảnh trong được load.
 *  path: đường dẫn đến vị trí của sprite trong máy.
 *  image: bộ nhớ đệm (buffer) bao gồm các ảnh trong sheet.
 *  width và height : số lượng ảnh (2 chiều) trong sheet.
 */
public class SpriteSheet {

    /**
     * các sprite mặc định được load sẵn trước khi khởi tạo game.
     */
    public static final int DEFAULT_SIZE = 32;
    public static final SpriteSheet background = new SpriteSheet("data/sprite/background.png", DEFAULT_SIZE);
    public static final SpriteSheet player = new SpriteSheet("data/sprite/player.png", DEFAULT_SIZE);
    public static final SpriteSheet dumbEnemies = new SpriteSheet("data/sprite/dumbEnemies.png", DEFAULT_SIZE);
    public static final SpriteSheet bomb = new SpriteSheet("data/sprite/bomb.png", DEFAULT_SIZE);
    public static final SpriteSheet ItemSpeed = new SpriteSheet("data/sprite/powerup_speed.png", DEFAULT_SIZE);

    public final int size;
    private final String path;
    public BufferedImage image;
    private int width;
    private int height;

    /**
     * constructor load ảnh từ đường dẫn path và kích cỡ mặc định của sprite là size.
     *
     * @param path đường dẫn ảnh.
     * @param size kích cỡ ảnh.
     * @throws IOException nếu không load được ảnh ( sẽ kết thúc chương trình nếu lỗi xảy ra)
     */
    public SpriteSheet(String path, int size) {
        this.size = size;
        this.path = path;
        try {
            URL a = new URL("file:" + path);
            image = ImageIO.read(a);
            width = image.getWidth() / size;
            height = image.getHeight()/ size;
        } catch (IOException e) {
            System.out.println("can not load from path" + path);
            e.printStackTrace();
            System.exit(2);
        }
    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * trả về giá trị argb của ảnh có vị trí (posX, posY) trong sheet
     *
     * @param posX vị trí x
     * @param posY vị trí y
     * @return giá trị argb theo số nguyên 32 bit.
     */
    public int getRGB(int posX, int posY) {
        return image.getRGB(posX, posY);

    }
}
