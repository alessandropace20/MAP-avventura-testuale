package com.mycompany.thediamondheist.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

/**
 * Carica immagini da classpath (/images).
 */

public final class ImageLoader {

    private static final Map<String, ImageIcon> CACHE = new ConcurrentHashMap<>();

    private ImageLoader() {}

    public static ImageIcon load(String fileName, int targetW, int targetH) {
        String key = fileName + "@" + targetW + "x" + targetH;
        return CACHE.computeIfAbsent(key, k -> {
            BufferedImage src = tryLoad(fileName);
            if (src == null) {
                src = placeholder(targetW, targetH);
            }
            Image scaled = src.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        });
    }

    private static BufferedImage tryLoad(String fileName) {
        try {
           
            URL url = ImageLoader.class.getResource("/images/" + fileName);
            if (url != null) return ImageIO.read(url);
            
            File f = new File("resources/images/" + fileName);
            if (f.exists()) return ImageIO.read(f);
        } catch (Exception ignored) {}
        return null;
    }

    private static BufferedImage placeholder(int w, int h) {
        if (w <= 0) w = 800;
        if (h <= 0) h = 600;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(24,24,24));
        g.fillRect(0,0,w,h);
        g.setColor(new Color(200,200,200));
        g.drawRect(1,1,w-3,h-3);
        g.drawString("Nessuna immagine", 16, 24);
        g.dispose();
        return img;
    }

   
    public static void clearCache() { CACHE.clear(); }
}
        