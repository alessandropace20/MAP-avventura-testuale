/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.util;

/**
 * Servizio che prova a prendere un piccolo "suggerimento" online.
 * Usa un endpoint pubblico di test (jsonplaceholder) e restituisce il campo "title".
 * In caso di problemi, ritorna null e il gioco continua normale.
 */

public final class RestHintService {

    private RestHintService() {}
    
    public static String fetchOnlineHint() {
        
        String json = HttpRest.get("https://jsonplaceholder.typicode.com/todos/1", 2500);
        if (json == null || json.isEmpty()) return null;

      
        String key = "\"title\":";
        int i = json.indexOf(key);
        if (i < 0) return null;
        int start = json.indexOf('"', i + key.length());
        if (start < 0) return null;
        int end = json.indexOf('"', start + 1);
        if (end < 0) return null;
        String title = json.substring(start + 1, end).trim();
        return title.isEmpty() ? null : title;
    }
}

