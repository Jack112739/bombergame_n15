package gameInterface;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.Rectangle;

/**
 * class này đảm nhận việc show map ra màn hình, quản lý xem nên vẽ map thế nào, quản lý việc qua màn show điểm
 * tất cả đều nằm trong class này, có tác dụng như 1 scene
 *
 * thuộc tính map, level : level và map hiện tại class này đang show.
 * thuộc tính display rect : hình chữ nhật thể hiện ảnh của map class này biểu diễn.
 * thuộc tính gc : graphic context của class này.
 *
 *
 * LƯU Ý: CLASS NÀy CHỈ ĐỂ TEST THỬ VÌ KHÔNG BIẾT TẠO CONTROL ÔNG SẼ PHẢI CODE LẠI 1 XÍU
 */
public class MapDisplay extends AnimationTimer implements EventHandler<KeyEvent> {
    private GameMap map;
    private int level;
    private final Rectangle displayRec;
    private final GraphicsContext gc;

    /**
     * khởi tạo mặc định
     */
    public MapDisplay(int level, int border, Rectangle displayRec, GraphicsContext gc) {
        map = new GameMap(level, displayRec.width, displayRec.height, border);
        this.displayRec = displayRec;
        this.gc = gc;
        this.level = level;
    }

    public MapDisplay(int level, Rectangle displayRec, GraphicsContext gc) {
        map = new GameMap(level, displayRec.width, displayRec.width);
        this.displayRec = displayRec;
        this.gc = gc;
    }

    @Override
    public void handle(long now) {
        if(map.status() != GameMap.Status.GAME_ON) return;
        map.handle(now);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getWidth());
        gc.drawImage(map.getImage(), displayRec.x, displayRec.y);
        if(map.status() == GameMap.Status.PLAYER_LOSE) displayLose();
        if(map.status() == GameMap.Status.PLAYER_WIN) displayWin();
    }

    /**
     * được gọi khi player thua có lẽ tùy vào game mode
     */
    void displayLose() {
        gc.setFill(Color.RED);
        gc.setFont(Font.font(30));
        gc.fillText("Omea wa mou shindeiru!", 200, 400);
    }

    /**
     * được gọi khi player thắng và qua màn
     */
    void displayWin() {
        gc.setFill(Color.BLUE);
        gc.setFont(Font.font(30));
        gc.fillText("Chotto Matte!", 200, 400);
        //load map mới và show score
        if(level > 2) return;
        level++;
        map = new GameMap(level, displayRec.width, displayRec.height, map.DISPLAY_BORDER);
    }

    @Override
    public void handle(KeyEvent event) {
        map.handle(event);
    }
}
