// MainMenuScreen.java
package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Hauptmenü: frei resizable, F toggelt Vollbild.
 */
public class MainMenuScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Rectangle startBtn, exitBtn;
    private static final int BTN_W = 200, BTN_H = 50;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Button-Positionen initial (werden in resize() angepasst)
        startBtn = new Rectangle();
        exitBtn = new Rectangle();
        updateButtonBounds(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int p, int btn) {
                Vector3 touch = new Vector3(x, y, 0);
                camera.unproject(touch);
                if (startBtn.contains(touch.x, touch.y)) {
                    game.switchScreen(new GameScreen(game));
                } else if (exitBtn.contains(touch.x, touch.y)) {
                    Gdx.app.exit();
                }
                return true;
            }

            @Override
            public boolean keyDown(int key) {
                if (key == Input.Keys.F) {
                    if (Gdx.graphics.isFullscreen()) {
                        Gdx.graphics.setWindowedMode(1280, 720);
                    } else {
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f,0.1f,0.15f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Buttons zeichnen (Platzhalter Rechtecke und Text)
        font.draw(batch, "START", startBtn.x + 60, startBtn.y + 35);
        font.draw(batch, "EXIT", exitBtn.x + 70, exitBtn.y + 35);
        batch.end();
    }

    @Override
    public void resize(int w, int h) {
        camera.setToOrtho(false, w, h);
        camera.update();
        updateButtonBounds(w, h);
    }

    private void updateButtonBounds(int w, int h) {
        // Zentriert vertikal und horizontal
        startBtn.set(w/2 - BTN_W/2, h/2 + 30, BTN_W, BTN_H);
        exitBtn.set(w/2 - BTN_W/2, h/2 - BTN_H - 30, BTN_W, BTN_H);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose(); font.dispose();
    }
}