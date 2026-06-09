package code.yankton_bank.util;

import javax.sound.sampled.*;

/**
 * Gestione audio ed effetti:
 * - Effetti sonori
 * - Loop musicali
 * - Volume dagli audio in game
 */
public class MusicHandler {

    private static Clip loopClip;

   
    public static void playLoop(String classpath) {
        stopLoop();
        try {
            AudioInputStream ais = openAis(classpath);
            loopClip = AudioSystem.getClip();
            loopClip.open(ais);
            loopClip.loop(Clip.LOOP_CONTINUOUSLY);
            loopClip.start();
        } catch (Exception e) {
            System.err.println("[Audio] loop error: " + e.getMessage());
        }
    }

   
    public static void stopLoop() {
        if (loopClip != null) {
            loopClip.stop();
            loopClip.close();
            loopClip = null;
        }
    }
    
    public static AudioInputStream openAis(String classpath) throws Exception {
        return AudioSystem.getAudioInputStream(
                MusicHandler.class.getResourceAsStream(classpath)
        );
    }
}
