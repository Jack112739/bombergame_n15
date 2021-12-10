package graphics;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * lưu trữ thông tin về 1 sprite (1 mảng 2d bao gồm các ảnh đã được xử lý như resize, transparent)
 * xử lý các hình ảnh thô từ class spriteSheet
 * môi animatedEntities đều phải có sprite.
 *
 * thuộc tính size : size của từng ảnh trong mảng images tức mỗi ảnh trong mảng đó có kích cỡ [size x size]
 * thuộc tính sheet : sheet gốc của các ảnh mà cần class này xử lý.
 * thuộc tính images : mảng 1 chiều các ảnh trong sprite.
 */
public class Sprite {

    public static final int TRANSPARENT_COLOR = 0xffffffff;
    public static int RenderX = 0, RenderY = 0;
    public static int ScaleSize = SpriteSheet.DEFAULT_SIZE;

    public final int size;
    public final SpriteSheet sheet;
    private final Image[] images;

    /**
     * constructor lấy ảnh từ sheet và mỗi ảnh sẽ được resize lại theo @param size.
     *
     * @param sheet sheet cần xử lý ảnh
     * @param size size để resize lại các ảnh trong sheet
     */
    public Sprite(SpriteSheet sheet, int size) {
        this.size = size;
        this.sheet = sheet;
        images = new Image[getHeight() * getWidth()];
        loadImage();
    }

    /**
     * khởi tạo 1 sprite chứa 1 ảnh với kích cỡ size và chỉ có màu color.
     *
     * @param size kích cỡ của ảnh trong sprite
     * @param color màu của ảnh cần khởi tạo
     */
    Sprite(int size, int color) {
        this.size = size;
        this.sheet = null;
        images = new Image[1];
        fillColor(color);
    }

    /**
     * thực hiện việc resize và chỉnh sửa lại ảnh @img sao cho phù hợp với các thuộc tính của class
     * không thể chỉnh trực tiếp, cần chuyển qua bằng imageView.
     *
     * @param img ảnh cần chỉnh sửa
     * @param scaled tỷ số chỉnh sửa
     * @return ảnh sau khi được chỉnh sửa về chỉnh để transparent
     */

    private Image resample(WritableImage img, int scaled) {
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(scaled);
        imageView.setFitHeight(scaled);
        SnapshotParameters snapshot = new SnapshotParameters();
        snapshot.setFill(Color.TRANSPARENT);
        return imageView.snapshot(snapshot, null);
    }

    /**
     * trả về 1 ảnh chỉ có 1 màu với mã argb của màu là color
     * @param color màu của color biểu diễn trong argb 32 bit
     */

    private void fillColor(int color) {
        WritableImage img = new WritableImage(size, size);
        PixelWriter writer = img.getPixelWriter();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                writer.setArgb(i, j, color);
            }
        }
        for (int i = 0; i < images.length; i++) {
            images[i] = new ImageView(img).getImage();
        }
    }

    /**
     * hàm này thực hiện việc load image từ 1 spriteSheet (tức là thuộc tính sheet định nghĩa ở trên)
     * dùng trong constructor của lớp này  để khởi tạo mảng
     */

    private void loadImage() {
        final int sheetSize = sheet.size;
        final int h = getHeight();
        final int w = getWidth();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int currentTopLeftX = j * sheetSize;
                int currentTopLeftY = i * sheetSize;
                WritableImage img = new WritableImage(sheetSize, sheetSize);
                PixelWriter writer = img.getPixelWriter();
                for (int i1 = 0; i1 < sheetSize; i1++) {
                    for (int j1 = 0; j1 < sheetSize; j1++) {
                        int rgb = sheet.getRGB(currentTopLeftX + i1, currentTopLeftY + j1);
                        if (rgb == TRANSPARENT_COLOR) {
                            writer.setArgb(i1, j1, 0);
                        } else {
                            writer.setArgb(i1, j1, rgb);
                        }
                    }
                }
                images[i * w + j] = resample(img, size);
            }
        }
    }

    /**
     *  trả về chiều cao của sprite.
     */

    public int getHeight() {
        if (sheet == null) {
            return 1;
        } else {
            return sheet.getHeight();
        }
    }

    /**
     * trả về số chiều rộng của sprite.
     */

    public int getWidth() {
        if (sheet == null) {
            return 1;
        } else {
            return sheet.getWidth();
        }
    }

    /**
     * dùng để lấy ra duy nhất 1 ảnh (được khởi tạo từ trước) ở vị trí cụ thể trong spriteSheet của ảnh đó.
     *
     * @param posX vị trí x của hình ảnh trong sprite 2d
     * @param posY vị trí y của hình ảnh trong mảng sprite 2d;
     * @return ảnh ở vị trí posX pos Y
     */
    public Image getImageAt(int posX, int posY) {
        return images[posY * getWidth() + posX];
    }

}
