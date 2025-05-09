// Main.java
package io.example.mortal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Screen;

/**
 * Hauptklasse, verwaltet Screen-Wechsel und Modus (Fenster/Vollbild).
 */
public class Main extends Game {
    @Override
    public void create() {
        // Beginne im Hauptmenü
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        // Vollbild nur für GameScreen und PauseMenuScreen
        if (screen instanceof GameScreen || screen instanceof PauseMenuScreen) {
            DisplayMode dm = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(dm);
            Gdx.graphics.setResizable(false);
        } else {
            // Hauptmenü: Fenster-Modus mit Resize
            Gdx.graphics.setResizable(true);
            Gdx.graphics.setWindowedMode(1280, 720);
        }
    }

    /**
     * Utility zum sauberen Wechseln und Disposen.
     */
    public void switchScreen(Screen newScreen) {
        Screen old = getScreen();
        setScreen(newScreen);
        if (old != null) old.dispose();
    }
}