package com.mycompany.thediamondheist.database;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Centralizza cartella dati e file usati dal gioco.
 * Evita path hard-coded sparsi nel codice.
 */
public final class DatabaseManager {
    
    private static final String APP_DIR_NAME = ".thediamondheist";
    private static final String SCOREBOARD_FILE = "scoreboard.txt";
    private static final String SAVE_PREFIX = "save_";
    private static final String SAVE_EXT = ".dat";

    private DatabaseManager() {}

    public static Path getAppDir() {
        String home = System.getProperty("user.home");
        Path dir = Path.of(home, APP_DIR_NAME);
        try {
            Files.createDirectories(dir);
        } catch (Exception ignored) {}
        return dir;
    }

    public static File getScoreboardFile() {
        return getAppDir().resolve(SCOREBOARD_FILE).toFile();
    }

    public static File getSaveFile(String slot) {
        if (slot == null || slot.isBlank()) slot = "slot1";
        String filename = SAVE_PREFIX + slot + SAVE_EXT;
        return getAppDir().resolve(filename).toFile();
    }
}
