/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

import code.yankton_bank.impl.GameDesc;
import code.yankton_bank.type.AdvObject;
import code.yankton_bank.type.Player;
import code.yankton_bank.type.Room;

/**
 * Data Access Object per salvataggi di gioco.
 * - Legge e scrive lo stato partita (roomId, inventario, punteggio)
 * - Fornisce metodi di utility per caricare l’ultimo salvataggio
 */


public class GameSaveDAO {

    private static final String VERSION = "1";

    public void save(GameDesc game, String slot) throws IOException {
        if (game == null || game.getPlayer() == null) {
            throw new IllegalArgumentException("Game o Player non valido");
        }

        Player pl = game.getPlayer();
        Room cur = pl.getCurrentRoom();
        int roomId = (cur != null) ? cur.getId() : -1;
        String roomName = (cur != null && cur.getName() != null) ? cur.getName() : "";

        List<String> invNames = new ArrayList<>();
        if (pl.getInventory() != null && pl.getInventory().getObjects() != null) {
            for (AdvObject o : pl.getInventory().getObjects()) {
                if (o != null && o.getName() != null) invNames.add(o.getName());
            }
        }

        Properties props = new Properties();
        props.setProperty("version", VERSION);
        props.setProperty("timestamp", Instant.now().toString());
        props.setProperty("playerName", safe(pl.getName()));
        props.setProperty("score", String.valueOf(game.getScore()));
        props.setProperty("currentRoomId", String.valueOf(roomId));
        props.setProperty("currentRoomName", safe(roomName));
        props.setProperty("inventory", String.join(",", invNames));

        File f = getSaveFile(slot);
        ensureParentDir(f);
        try (Writer w = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8)) {
            props.store(w, "Yankton Bank Save");
        }
        System.out.println("Partita salvata su: " + f.getAbsolutePath());
    }

    public void loadInto(GameDesc game, String slot) throws IOException {
        File f = getSaveFile(slot);
        if (!f.exists()) throw new FileNotFoundException("Salvataggio non trovato: " + f.getAbsolutePath());

        Properties props = new Properties();
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            props.load(r);
        }

        String version = props.getProperty("version", "1");
        if (!"1".equals(version)) throw new IOException("Versione salvataggio non compatibile: " + version);

        String playerName = props.getProperty("playerName", "Player");
        int score = parseInt(props.getProperty("score"), 0);
        int roomId = parseInt(props.getProperty("currentRoomId"), -1);
        String roomName = props.getProperty("currentRoomName", "");
        List<String> invNames = splitCsv(props.getProperty("inventory", ""));

        Player pl = game.getPlayer();
        if (pl != null) pl.setName(playerName);

        try {
            int current = game.getScore();
            int delta = score - current;
            if (delta != 0) game.addScore(delta);
        } catch (Exception ignored) {}

        Room target = findRoomBFS(pl != null ? pl.getCurrentRoom() : null, roomId, roomName);
        if (pl != null && target != null) {
            pl.setCurrentRoom(target);
        }

        try {
            if (pl != null && pl.getInventory() != null) {
                if (pl.getInventory().getObjects() != null) {
                    pl.getInventory().getObjects().clear();
                }
                int idCounter = 9000;
                for (String n : invNames) {
                    if (n == null || n.isBlank()) continue;
                    AdvObject ghost = new AdvObject(idCounter++, n, "");
                    ghost.setPickupable(true);
                    pl.getInventory().add(ghost);
                }
            }
        } catch (Exception ignored) {}

        System.out.println("Partita caricata da: " + f.getAbsolutePath());
    }


    private static File getSaveFile(String slot) {
        if (slot == null || slot.isBlank()) slot = "slot1";
        String home = System.getProperty("user.home");
        File dir = new File(home, ".yanktonbank");
        return new File(dir, "save_" + slot + ".dat");
    }

    private static void ensureParentDir(File f) {
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
    }

    private static String safe(String s) { return (s == null) ? "" : s; }

    private static int parseInt(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private static List<String> splitCsv(String s) {
        if (s == null || s.isBlank()) return java.util.Collections.emptyList();
        String[] parts = s.split(",");
        List<String> out = new java.util.ArrayList<>();
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    private static Room findRoomBFS(Room start, int id, String name) {
        if (start == null) return null;

        if (id >= 0 && start.getId() == id) return start;
        if ((id < 0 || start.getId() != id)
                && name != null && !name.isBlank()
                && name.equalsIgnoreCase(start.getName())) {
            return start;
        }

        Set<Integer> visited = new HashSet<>();
        Deque<Room> q = new ArrayDeque<>();
        q.add(start);
        visited.add(start.getId());

        while (!q.isEmpty()) {
            Room cur = q.removeFirst();

            Map<String, Room> exits = cur.getExits();
            if (exits == null) continue;

            for (Room nxt : exits.values()) {
                if (nxt == null) continue;
                int nxtId = nxt.getId();
                if (visited.contains(nxtId)) continue;

                if (id >= 0 && nxtId == id) return nxt;
                if ((id < 0 || nxtId != id)
                        && name != null && !name.isBlank()
                        && name.equalsIgnoreCase(nxt.getName())) {
                    return nxt;
                }

                visited.add(nxtId);
                q.addLast(nxt);
            }
        }
        return null;
    }
}
