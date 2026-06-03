package com.mycompany.thediamondheist.ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/** Schermata di caricamento pre partita. */
public class LoadingScreen extends JDialog {

    private final JProgressBar bar = new JProgressBar();

    public LoadingScreen(Window owner) {
        super(owner, "Caricamento...", ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel card = new JPanel(new BorderLayout(12,12)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(22,22,28), 0,getHeight(), new Color(38,38,52)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80,80,100), 1),
                BorderFactory.createEmptyBorder(16,16,16,16)
        ));

        JLabel title = new JLabel("Preparazione partita...");
        title.setForeground(new Color(230,230,238));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JLabel subtitle = new JLabel("Caricamento risorse");
        subtitle.setForeground(new Color(200,200,210));
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 14f));

        bar.setIndeterminate(true);
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(340, 22));

        JPanel texts = new JPanel(new GridLayout(0,1,0,4));
        texts.setOpaque(false);
        texts.add(title);
        texts.add(subtitle);

        card.add(texts, BorderLayout.NORTH);
        card.add(bar, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
        pack();
        setSize(new Dimension(420, 140));
        setLocationRelativeTo(owner);
    }

    public void showAndRun(Runnable loader, int minDurationMs) {
        final long start = System.nanoTime();
        SwingWorker<Void,Void> w = new SwingWorker<>() {
            @Override protected Void doInBackground() {
                try {
                    if (loader != null) loader.run();
                } catch (Throwable ignored) {}
                return null;
            }
            @Override protected void done() {
                long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                long remain = Math.max(0, minDurationMs - elapsedMs);
                Timer t = new Timer((int)remain, e -> dispose());
                t.setRepeats(false);
                t.start();
            }
        };
        w.execute();
        setVisible(true);
    }
}
