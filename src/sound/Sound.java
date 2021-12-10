package sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    public static final String BOMB_EXPLOSION = "explosion";
    public static final String PLACE_BOMB = "place_bomb";
    public static final String DEAD = "dead2";
    Clip clip;

    public void createSound(String file) {
        String path = "data/sound/" + file + ".wav";
        File music = new File(path);

        try {
            if (music.exists()) {

                AudioInputStream ad = AudioSystem.getAudioInputStream(music);
                clip = AudioSystem.getClip();
                clip.open(ad);
                clip.start();
            } else {
                System.out.println("not play");
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        clip.stop();
    }
}
