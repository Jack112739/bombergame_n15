import gameInterface.GameMap;
import gameInterface.MapDisplay;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;

/**
 * class chính để chạy app, mọi hoạt động của game đều thực hiện trong map này
 * class cài dưới đây chủ yếu để test.
 */
public class Main extends Application {
    public static final double WIDTH = 800;
    public static final double HEIGHT = 600;
    MediaPlayer mediaPlayer;
    private Scene scene1, scene2;
    private Stage stage;
    private MapDisplay mapDisplay;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        stage = primaryStage;
        scene1 = initScene1();
        scene2 = initScene2();
        // playMusic("data/sound/stage_theme.mp3");
        stage.setScene(scene1);
        stage.setTitle("Bomberman - game 1.0");
        stage.show();
    }

    Scene initScene1() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(
            new Image("file:data/sprite/logScreen.png", WIDTH, HEIGHT, false, false), 0, 0);

        Button button = new Button("test");
        button.setOnAction(event -> {
            scene2 = initScene2();
            mapDisplay.start();
            stage.setScene(scene2);
        });
        StackPane layout = new StackPane();
        layout.getChildren().addAll(canvas, button);
        return new Scene(layout);
    }

    Scene initScene2() {

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        mapDisplay = new MapDisplay(1, new Rectangle(150, 150, 200, 200),gc);

        Button button = new Button("return");

        button.setLayoutY(0);
        button.setLayoutX(0);
        button.setOnAction(event -> {

            stage.setScene(scene1);
            mapDisplay.stop();

        });

        StackPane layout = new StackPane();
        layout.getChildren().addAll(canvas, button);

        Scene result = new Scene(layout);
        result.setOnKeyPressed(mapDisplay);
        return result;
    }

    public void playMusic(String file) {
        Media media = new Media(new File(file).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(10);
        mediaPlayer.setStartTime(Duration.ZERO);
        mediaPlayer.setStopTime(Duration.minutes(10));
        mediaPlayer.play();
    }
}



