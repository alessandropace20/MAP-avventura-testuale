/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.ui;


import code.yankton_bank.GameDescription;
import code.yankton_bank.impl.GameDesc;
import code.yankton_bank.parser.Parser;
import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Controller principale del gioco Yankton Bank.
 * - Crea GameDesc (stato di gioco) e Parser
 * - Registra gli observer (move/open/use/pickup + look/inventory/help/end)
 * - Mostra la GUI (GameFrame) e reindirizza System.out nella console
 * - Gestisce salvataggio/caricamento su file
 */
public class GameController {

    private final GameDescription game;
    private final Parser parser;
    private final List<GameObserver> observers = new ArrayList<>();
    private final GameFrame frame;

    private static GameController CURRENT;

    public GameController(boolean showIntro) {
        GameDesc g = new GameDesc();
        g.init();
        this.game = g;

        this.parser = new Parser(g.getCommands(), g.getPlayer());

        observers.add(new code.yankton_bank.impl.MoveObserver());
        observers.add(new code.yankton_bank.impl.OpenObserver());
        observers.add(new code.yankton_bank.impl.UseObserver());
        observers.add(new code.yankton_bank.impl.PickUpObserver());

        observers.add(new code.yankton_bank.impl.LookObserver());
        observers.add(new code.yankton_bank.impl.InventoryObserver());
        observers.add(new code.yankton_bank.impl.HelpObserver());

        observers.add(new code.yankton_bank.impl.EndObserver());

        this.frame = new GameFrame(this);

        CURRENT = this;

        redirectSystemOutTo(frame::println);

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
            if (showIntro) frame.println(game.getIntro());
            refreshView(); 

            GameDesc gd = (GameDesc) game;
            Room cur = gd.getPlayer().getCurrentRoom();
            if (cur != null) {
                System.out.println("== " + cur.getName() + " ==");
                String desc = cur.getDescription();
                if (desc != null && !desc.isBlank()) {
                    System.out.println(desc);
                }
            }
        });
    }
    

    public void submit(String raw) {
        String input = raw == null ? "" : raw.trim();
        if (input.isEmpty()) return;

        ParserOutput p = parser.parse(input);
        for (GameObserver obs : observers) {
            obs.update(p, game);
        }

        saveLastToFile();

        refreshView();
    }

    private void refreshView() {
        GameDesc gd = (GameDesc) game;
        Room cur = gd.getPlayer().getCurrentRoom();
        if (cur == null) return;

        gd.onEnter(cur);

        frame.setStatus("Stanza: " + cur.getName());
        frame.setRoomImageById(cur.getId());
    }


    public static void returnToMenu() {
        try {
            if (CURRENT != null && CURRENT.frame != null) {
                CURRENT.frame.dispose();
            }
        } catch (Throwable ignored) { }
        SwingUtilities.invokeLater(() -> new MainMenuFrame().setVisible(true));
    }


    private File getSaveFile() {
        File dir = new File(System.getProperty("user.home"), ".yanktonbank");
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, "last.sav");
    }

    private String serialize() {
        GameDesc g = (GameDesc) game;
        StringBuilder sb = new StringBuilder();

        Room cur = g.getPlayer().getCurrentRoom();
        sb.append("roomId=").append(cur != null ? cur.getId() : -1).append(';');

        sb.append("inv=");
        var invList = g.getPlayer().getInventory().getObjects();
        for (int i = 0; i < invList.size(); i++) {
            var o = invList.get(i);
            if (o != null && o.getName() != null && !o.getName().isBlank()) {
                sb.append(o.getName());
                if (i < invList.size() - 1) sb.append(',');
            }
        }
        sb.append(';');

        sb.append("score=").append(g.getScore()).append(';');

        return sb.toString();
    }

    private void loadFromSerialized(String data) {
        if (data == null || data.isBlank()) return;
        GameDesc g = (GameDesc) game;

        String roomPart = null, invPart = null, scorePart = null;
        for (String part : data.split(";")) {
            int i = part.indexOf('=');
            if (i <= 0) continue;
            String k = part.substring(0, i).trim();
            String v = part.substring(i + 1).trim();
            switch (k) {
                case "roomId" -> roomPart = v;
                case "inv"    -> invPart = v;
                case "score"  -> scorePart = v;
            }
        }

        try {
            int id = Integer.parseInt(roomPart == null ? "-1" : roomPart);
            Room r = switch (id) {
                case 1 -> g.getIngresso();
                case 2 -> g.getStanzinoPulizie();
                case 3 -> g.getSalaSorveglianza();
                case 4 -> g.getCorridoio();
                case 6 -> g.getIngCaveau();
                default -> g.getIngresso();
            };
            g.getPlayer().setCurrentRoom(r);
        } catch (Exception ignored) {}

        var inventory = g.getPlayer().getInventory().getObjects();
        inventory.clear();
        if (invPart != null && !invPart.isBlank()) {
            for (String raw : invPart.split(",")) {
                String name = raw == null ? "" : raw.trim();
                if (name.isEmpty()) continue;
                AdvObject o = new AdvObject(900, name, name);
                o.setPickupable(true);
                g.getPlayer().getInventory().add(o);
            }
        }

        try {
            if (scorePart != null) {
                int s = Integer.parseInt(scorePart);
                int curScore = g.getScore();
                g.addScore(s - curScore); // porta lo score a s
            }
        } catch (NumberFormatException ignored) {}

        Room cur = g.getPlayer().getCurrentRoom();
        if (cur != null) {
            System.out.println("== " + cur.getName() + " ==");
            String desc = cur.getDescription();
            if (desc != null && !desc.isBlank()) System.out.println(desc);
        }

        refreshView(); 
    }

    public void saveLastToFile() {
        File f = getSaveFile();
        try (Writer w = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8)) {
            w.write(serialize());
        } catch (IOException e) {
            System.err.println("[Salvataggio] " + e.getMessage());
        }
    }

    public boolean loadLastFromFile() {
        File f = getSaveFile();
        if (!f.exists()) return false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String data = br.readLine();
            loadFromSerialized(data);
            return true;
        } catch (IOException e) {
            System.err.println("[Caricamento] " + e.getMessage());
            return false;
        }
    }


    private void redirectSystemOutTo(Consumer<String> consumer) {
        PrintStream ps = new PrintStream(new SwingConsoleOutputStream(consumer), true, StandardCharsets.UTF_8);
        System.setOut(ps);
        System.setErr(ps);
    }

    private static class SwingConsoleOutputStream extends OutputStream {
        private final Consumer<String> consumer;
        private final java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();

        SwingConsoleOutputStream(Consumer<String> consumer) { this.consumer = consumer; }

        @Override
        public synchronized void write(int b) {
            if (b == '\n') {
                flushLine();
            } else if (b != '\r') {
                buffer.write(b);
            }
        }

        @Override
        public synchronized void write(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) {
                write(b[i]);
            }
        }

        @Override
        public synchronized void flush() {
            if (buffer.size() > 0) flushLine();
        }

        private void flushLine() {
            String line;
            try {
                line = buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
            } finally {
                buffer.reset();
            }
            javax.swing.SwingUtilities.invokeLater(() -> consumer.accept(line));
        }
    }
}

