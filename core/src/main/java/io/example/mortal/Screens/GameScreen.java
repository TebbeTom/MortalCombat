package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.InputAdapter;
import io.example.mortal.Player;






import io.example.mortal.Main;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private Texture background;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private BitmapFont font;
    
    private final int VIRTUAL_WIDTH = 1280;
    private final int VIRTUAL_HEIGHT = 720;

    private Main game;
    private Sprite player1Sprite;
    private Sprite player2Sprite;

    private boolean isPaused = false;

    public GameScreen(Main game) {
        this.game = game;
        game.player1 = new Player(new Vector2(100f, 0f), 100);
        game.player2 = new Player(new Vector2(500f, 0f), 100);
    } 

    @Override
    public void show() {
        background = new Texture("TeahouseNight.png");
        spriteBatch = new SpriteBatch();
        player1Sprite = new Sprite(game.player1.getKeyframe());
        
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
        input();
        logic(delta);
        draw();
    }

    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            game.player1.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            game.player1.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            game.player1.duck();
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            game.player1.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.E))
            game.player1.punch();
        if (Gdx.input.isKeyPressed(Input.Keys.F))
            game.player1.kick();

        if (Gdx.input.isKeyPressed(Input.Keys.O))
            game.player2.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.K))
            game.player2.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.L ))
            game.player2.duck();
        if (Gdx.input.isKeyPressed(Input.Keys.SEMICOLON))
            game.player2.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.P))
            game.player2.punch();
        if (Gdx.input.isKeyPressed(Input.Keys.F))
            game.player2.kick();
        if (Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE))
            isPaused = !isPaused;

        if (Gdx.input.isKeyPressed(Input.Keys.G))
            toggleFullscreen();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            drawPauseMenu();
    }

    private void logic(float delta) {
        game.player1.update(delta);
        syncSprite(game.player1, player1Sprite);
        //game.player2.update(delta);
    }

    private void syncSprite(Player player, Sprite sprite) {
        sprite.setX(player.position.x);
        sprite.setY(player.position.y);
        sprite.setOrigin(25f, 0f);
        sprite.setScale(6f);

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        player1Sprite.draw(spriteBatch);
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
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}