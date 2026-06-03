package com.mycompany.thediamondheist.parser;

import com.mycompany.thediamondheist.type.*;

import java.util.List;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser dei comandi testuali.
 * - Riceve input dell’utente come stringa
 * - Analizza e produce un ParserOutput con il verbo e gli oggetti coinvolti
 */
public class Parser {

    private final List<Command> commands;
    private final Player player;

    private final Set<String> stopwords = new HashSet<>();

    public Parser(List<Command> commands, Player player) {
        this.commands = commands;
        this.player = player;
        loadStopwords(); 
    }

    public ParserOutput parse(String raw) {
        if (raw == null) return new ParserOutput(new Command("", CommandType.UNKNOWN), null);

        String input = raw.trim().toLowerCase(Locale.ITALIAN);

        input = input.replaceFirst("^vai\\s+", "");

        String cleanedForType = removeStopwordsFrom(input);
        if (cleanedForType == null || cleanedForType.isBlank()) {
            cleanedForType = input;
        }

        CommandType type = guessType(cleanedForType);

        String nounRaw = extractNoun(input, type);

        String noun = normalizeNoun(nounRaw);

        AdvObject obj = null;
        if (noun != null && !noun.isBlank()) {
            Room cur = (player != null) ? player.getCurrentRoom() : null;
            if (cur != null && cur.getObjects() != null) {
                for (AdvObject o : cur.getObjects()) {
                    if (o != null && o.getName() != null && o.getName().equalsIgnoreCase(noun)) {
                        obj = o; break;
                    }
                }
            }
            if (obj == null && player != null && player.getInventory() != null) {
                try {
                    obj = player.getInventory().getByName(noun);
                } catch (Exception ignored) {}
            }
        }

        Command matched = null;
        if (commands != null) {
            for (Command c : commands) {
                if (c != null && c.getType() == type) { matched = c; break; }
            }
        }
        if (matched == null) matched = new Command(input, type);

        return new ParserOutput(matched, obj);
    }

    private CommandType guessType(String input) {
        if (input == null || input.isBlank()) return CommandType.UNKNOWN;

        switch (input) {
            case "nord":  return CommandType.NORTH;
            case "sud":   return CommandType.SOUTH;
            case "est":   return CommandType.EAST;
            case "ovest": return CommandType.WEST;
        }

        if (input.equals("salva") || input.equals("save"))   return CommandType.SAVE;
        if (input.equals("carica") || input.equals("load"))  return CommandType.LOAD;

        if (input.startsWith("guarda"))      return CommandType.LOOK;
        if (input.startsWith("prendi"))      return CommandType.PICK_UP;
        if (input.startsWith("usa"))         return CommandType.USE;
        if (input.startsWith("apri"))        return CommandType.OPEN;
        if (input.startsWith("inventario"))  return CommandType.INVENTORY;
        if (input.startsWith("aiuto"))       return CommandType.HELP;
        if (input.startsWith("fine") || input.startsWith("esci") || input.startsWith("quit"))
            return CommandType.END;

        return CommandType.UNKNOWN;
    }

    private String extractNoun(String input, CommandType t) {
        if (input == null) return null;

        switch (t) {
            case LOOK:
                if (input.equals("guarda")) return null;
                return input.replaceFirst("^guarda\\s+", "");
            case PICK_UP:
                return input.replaceFirst("^prendi\\s+", "");
            case USE:
                return input.replaceFirst("^usa\\s+", "");
            case OPEN:
                return input.replaceFirst("^apri\\s+", "");
            case SAVE:
            case LOAD:
            case INVENTORY:
            case HELP:
            case END:
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
            default:
                return null;
        }
    }


    private void loadStopwords() {
        try {
            InputStream is = Parser.class.getResourceAsStream("/stopwords.txt");
            if (is == null) {
                System.err.println("[Parser] stopwords.txt non trovato nel classpath (atteso in src/main/resources).");
                return;
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String w = line.trim().toLowerCase(Locale.ITALIAN);
                    if (!w.isBlank()) stopwords.add(w);
                }
            }
            System.out.println("[Parser] Stopwords caricate: " + stopwords.size());
        } catch (Exception e) {
            System.err.println("[Parser] Stopwords non caricate: " + e.getMessage());
        }
    }

   
    private String removeStopwordsFrom(String s) {
        if (s == null || s.isBlank() || stopwords.isEmpty()) return s;
        String[] toks = s.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String t : toks) {
            String w = t.trim().toLowerCase(Locale.ITALIAN);
            if (!w.isEmpty() && !stopwords.contains(w)) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(w);
            }
        }
        return (sb.length() == 0) ? s.trim() : sb.toString();
    }

    /** Normalizza il nome oggetto rimuovendo stopwords e restituendo il primo token rilevante. */
    private String normalizeNoun(String s) {
        if (s == null) return null;
        if (stopwords.isEmpty()) return s.trim();
        String[] toks = s.trim().split("\\s+");
        for (String t : toks) {
            String w = t.trim().toLowerCase(Locale.ITALIAN);
            if (!w.isEmpty() && !stopwords.contains(w)) {
                return w; // primo token utile
            }
        }
        return s.trim();
    }
}
