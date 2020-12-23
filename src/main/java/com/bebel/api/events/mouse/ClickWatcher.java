package com.bebel.api.events.mouse;

import com.bebel.api.elements.basique.EventableElement;
import com.bebel.api.events.keyboard.KeyInput;

import static com.bebel.api.events.mouse.MouseInputType.DOWN;
import static com.bebel.api.events.mouse.MouseInputType.UP;

/**
 * Classe permettant de detecter les clicks
 * Un click est un evenement TOUCHDOWN et TOUCHUP sur la meme couche et du même bouton de souris dans un laps de temps de 200ms
 */
public class ClickWatcher {
    public static final int TIME_FOR_CLICK = 200;
    public static final int TIME_BETWEEN_CLICK = 300;
    protected long tick;
    private long lastClick;

    private int lastClickNb;
    protected int button;
    protected EventableElement layer;

    /**
     * Initialise le watcher lors d'un touch fromDown
     *
     * @param button
     * @param hitLayer
     */
    public void init(final int button, final EventableElement hitLayer) {
        this.layer = hitLayer;
        this.button = button;
        this.tick = System.currentTimeMillis();
    }

    /**
     * Verifie qu'un click a bien eu lieu lors d'un touch fromUp
     *
     * @param button
     * @param hitLayer
     * @return
     */
    public boolean check(final int button, final EventableElement hitLayer) {
        return button == this.button &&
                hitLayer == this.layer &&
                System.currentTimeMillis() - this.tick <= TIME_FOR_CLICK;
    }

    /**
     * Reset le watcher
     */
    public void clear() {
        this.lastClickNb = 0;
        this.layer = null;
        this.button = -1;
        this.tick = -1;
        this.lastClick = -1;
    }

    /**
     * Effectue le processus de verification d'un click
     *
     * @param type
     * @param mouse
     * @param hitLayer
     */
    public int checkClick(final MouseInputType type, final MouseInput mouse, final EventableElement hitLayer) {
        return checkClick(type, mouse.button, hitLayer);
    }

    /**
     * Effectue le processus de verification d'un click clavier
     *
     * @param type
     * @param keyboard
     */
    public int checkClick(final MouseInputType type, final KeyInput keyboard) {
        return checkClick(type, keyboard.key, null);
    }

    /**
     * Effectue le processus de verification d'un click
     *
     * @param type
     * @param button
     * @param hitLayer
     */
    public int checkClick(final MouseInputType type, final int button, final EventableElement hitLayer) {
        if (type == DOWN) this.init(button, hitLayer);
        else if (type == UP) {
            if (this.check(button, hitLayer)) {
                if (System.currentTimeMillis() - lastClick <= TIME_BETWEEN_CLICK)
                    lastClickNb++;
                else lastClickNb = 1;

                lastClick = System.currentTimeMillis();
                return lastClickNb;
            } else this.clear();
        }
        return 0;
    }
}