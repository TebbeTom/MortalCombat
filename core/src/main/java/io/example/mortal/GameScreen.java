// GameScreen.java
package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Hauptspiel-Screen: immer Vollbild, ESC -> PauseMenuScreen
 */
public class GameScreen implements Screen {
    private final Main game;
    private OrthographicCamera cam;
    private FitViewport vp;
    private SpriteBatch batch;
    private Texture bg;
    private boolean paused;
    private PauseMenuScreen pauseScreen;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Kamera + Viewport für 16:9 Design
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 1280, 720);
        vp = new FitViewport(1280, 720, cam);
        batch = new SpriteBatch();
        bg = new Texture("SunsetTempleBackground.png");
        pauseScreen = new PauseMenuScreen(game);
        paused = false;
        // Eingabe für ESC
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int key) {
                if (key == Input.Keys.ESCAPE) {
                    paused = !paused;
                    if (paused) game.switchScreen(pauseScreen);
                    else game.switchScreen(GameScreen.this);
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            ScreenUtils.clear(Color.BLACK);
            vp.apply();
            batch.setProjectionMatrix(cam.combined);
            batch.begin();
            batch.draw(bg, 0, 0, cam.viewportWidth, cam.viewportHeight);
            batch.end();
        }
    }

    @Override public void resize(int w,int h) { vp.update(w,h,true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { batch.dispose(); bg.dispose(); }
}
