/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *Utilizzata per salvare i punteggi (usata dagli observer/UI).
 */

public final class ScoreboardService {
    private static final Path FILE = Paths.get("scores.txt");

    private ScoreboardService() {}

    public static void saveScore(String playerName, int score) {
        try {
            if   (FILE.getParent() != null) {
                Files.createDirectories(FILE.getParent());
            }
            String name = (playerName == null || playerName.isBlank()) ? "Player" : playerName.trim();
            String line = name + ";" + score + System.lineSeparator();
            Files.writeString(FILE, line, StandardCharsets.UTF_8,
                    Files.exists(FILE) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (IOException ignored) { }
    }

    public static List<String> loadTopScores(int max) {
        if (!Files.exists(FILE)) return List.of();
        try {
            return Files.readAllLines(FILE, StandardCharsets.UTF_8).stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> {
                        String[] p = s.split(";", 2);
                        String name = p.length > 0 ? p[0] : "Player";
                        int sc = 0;
                        if (p.length > 1) try { sc = Integer.parseInt(p[1]); } catch (NumberFormatException ignored) {}
                        return new AbstractMap.SimpleEntry<>(name, sc);
                    })
                    .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                    .limit(max)
                    .map(e -> e.getKey() + " — " + e.getValue())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }
}

