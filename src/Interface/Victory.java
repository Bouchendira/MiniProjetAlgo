package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Victory extends JPanel {
    // Attributs pour gérer l'animation et la visibilité
    private float alpha = 0f;
    private Timer fadeTimer;
    private boolean isVisible = false;

    // Constantes pour le design
    private static final int PANEL_WIDTH = 400;
    private static final int PANEL_HEIGHT = 200;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BORDER_PADDING = 10;
    private static final float ANIMATION_STEP = 0.05f;

    public Victory() {
        // Initialisation du panneau
        setOpaque(false);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    /**
     * Démarre l'animation de fondu en entrée du panneau de victoire
     */
    public void startAnimation() {
        isVisible = true;
        alpha = 0f;
        
        // Création et configuration du timer pour l'animation
        ActionListener animationAction = e -> {
            alpha += ANIMATION_STEP;
            if (alpha >= 1f) {
                alpha = 1f;
                ((Timer)e.getSource()).stop();
            }
            repaint();
        };
        
        fadeTimer = new Timer(20, animationAction);
        fadeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isVisible) return;

        Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Configuration du rendu pour une meilleure qualité
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Application de la transparence pour l'animation
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Dessin du fond semi-transparent
            drawBackground(g2d);
            
            // Dessin de la bordure dorée
            drawBorder(g2d);
            
            // Dessin du texte principal et du sous-titre
            drawTexts(g2d);
            
            // Dessin du bouton
            drawButton(g2d);
            
        } finally {
            g2d.dispose();
        }
    }

    /**
     * Dessine le fond semi-transparent
     */
    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Dessine la bordure dorée
     */
    private void drawBorder(Graphics2D g2d) {
        g2d.setColor(new Color(218, 165, 32));
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawRect(BORDER_PADDING, BORDER_PADDING, 
                    getWidth() - 2 * BORDER_PADDING, 
                    getHeight() - 2 * BORDER_PADDING);
    }

    /**
     * Dessine les textes (titre et sous-titre)
     */
    private void drawTexts(Graphics2D g2d) {
        // Texte principal "VICTOIRE!"
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.setColor(new Color(255, 223, 0));
        drawCenteredString(g2d, "VICTOIRE !", getHeight() / 2 - 20);

        // Sous-titre
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(Color.WHITE);
        drawCenteredString(g2d, "Vous avez trouvé la sortie !", getHeight() / 2 + 20);
    }

    /**
     * Dessine le bouton Rejouer
     */
    private void drawButton(Graphics2D g2d) {
        int buttonX = (getWidth() - BUTTON_WIDTH) / 2;
        int buttonY = getHeight() - 70;

        // Fond du bouton
        g2d.setColor(new Color(50, 205, 50));
        g2d.fillRoundRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, 15, 15);

        // Texte du bouton
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        String buttonText = "Rejouer";
        int textX = buttonX + (BUTTON_WIDTH - fm.stringWidth(buttonText)) / 2;
        int textY = buttonY + ((BUTTON_HEIGHT + fm.getAscent()) / 2);
        g2d.drawString(buttonText, textX, textY);
    }

    /**
     * Utilitaire pour centrer un texte horizontalement
     */
    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }

    /**
     * Vérifie si un point est dans la zone du bouton
     */
    public boolean isPointInButton(Point p) {
        int buttonX = (getWidth() - BUTTON_WIDTH) / 2;
        int buttonY = getHeight() - 40;
        return new Rectangle(buttonX, buttonY, BUTTON_WIDTH+50, BUTTON_HEIGHT+50).contains(p);
    }
}