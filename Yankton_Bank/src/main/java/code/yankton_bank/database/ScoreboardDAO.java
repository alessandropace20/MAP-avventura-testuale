/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce il file della classifica.
 */
public class ScoreboardDAO {

    private final File file;

    public ScoreboardDAO() {
        File dir = new File(System.getProperty("user.home"), ".yanktonbank");
        if (!dir.exists()) dir.mkdirs();
        this.file = new File(dir, "scoreboard.txt");
    }

    public void append(String name, int score) {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8)) {
            w.write(name + ";" + score + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> loadAll() {
        List<String[]> list = new ArrayList<>();
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) list.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
