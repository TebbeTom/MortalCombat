package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    private Texture background;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private OrthographicCamera camera;

    private BitmapFont font;
    private boolean paused = false;

    private final int VIRTUAL_WIDTH = 1280;
    private final int VIRTUAL_HEIGHT = 720;

    @Override
    public void show() {
        background = new Texture("SunsetTempleBackground.png");
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                System.out.println("GameScreen wurde angeklickt.");
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        handleInput();

        if (!paused) {
            drawGame();
        } else {
            drawPauseMenu();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            toggleFullscreen();
        }
    }

    private void drawGame() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        spriteBatch.end();
    }

    private void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
            camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
            camera.update();
            viewport.update(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true);
        } else {
            DisplayMode displayMode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(displayMode);
            camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
            camera.update();
            viewport.update(displayMode.width, displayMode.height, true);
        }
    }

    private void drawPauseMenu() {
        ScreenUtils.clear(Color.DARK_GRAY); // Hintergrund abdunkeln
        viewport.apply();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Halbtransparentes Overlay
        spriteBatch.setColor(0, 0, 0, 0.5f);
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        spriteBatch.setColor(1, 1, 1, 1); // Reset

        // Textanzeige
        font.getData().setScale(2f);
        font.draw(spriteBatch, "Pausemenü", 500, 400);
        font.draw(spriteBatch, "Drücke ESC zum Fortsetzen", 420, 350);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Erlaube Seitenverhältnis-Korrektur nur im Fenster-Modus
        if (!Gdx.graphics.isFullscreen()) {
            float aspect = 16f / 9f;
            float currentAspect = (float) width / height;
            if (Math.abs(currentAspect - aspect) > 0.01f) {
                int correctedWidth = (int) (height * aspect);
                Gdx.graphics.setWindowedMode(correctedWidth, height);
                return; // `resize()` wird durch setWindowedMode erneut getriggert
            }
        }
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        spriteBatch.dispose();
        font.dispose();
    }
}
