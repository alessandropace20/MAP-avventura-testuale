/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

   
    public static void playSfx(String classpath) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream ais = openAis(classpath);
                clip.open(ais);
                clip.start();
                clip.addLineListener(ev -> {
                    if (ev.getType() == LineEvent.Type.STOP) {
                        try { clip.close(); } catch (Exception ignored) {}
                        try { ais.close(); } catch (Exception ignored) {}
                    }
                });
            } catch (Exception e) {
                System.err.println("[Audio] SFX error: " + e.getMessage());
            }
        }, "sfx-" + classpath).start();
    }

   
    public static void preload(String... classpaths) {
        new Thread(() -> {
            for (String p : classpaths) {
                try {
                    AudioInputStream ais = openAis(p);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.close();
                    ais.close();
                } catch (Exception ignored) {
                }
            }
        }, "audio-preload").start();
    }

    
    public static AudioInputStream openAis(String classpath) throws Exception {
        return AudioSystem.getAudioInputStream(
                MusicHandler.class.getResourceAsStream(classpath)
        );
    }
}

