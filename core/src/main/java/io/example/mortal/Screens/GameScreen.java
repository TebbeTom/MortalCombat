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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;

import io.example.mortal.Player;






import io.example.mortal.Main;
import io.example.mortal.MainMenuScreen;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private Texture background;
    private SpriteBatch spriteBatch;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Stage pauseStage;
    private Skin skin;
    private TextureAtlas skinAtlas;                        // CHANGED: atlas for skin

    
    private final int VIRTUAL_WIDTH = 1280;
    private final int VIRTUAL_HEIGHT = 720;

    private Main game;
    private Sprite player1Sprite;
    private Sprite player2Sprite;
    private float playerScaleFactor = 6f;

    private boolean isPaused = false;

    public GameScreen(Main game) {
        this.game = game;
        game.player1 = new Player(new Vector2(100f, 0f), 100, playerScaleFactor);
        game.player2 = new Player(new Vector2(500f, 0f), 100, playerScaleFactor);
    } 

    @Override
    public void show() {
        background = new Texture("RampartSnow.png");
        spriteBatch = new SpriteBatch();
        player1Sprite = new Sprite(game.player1.getKeyframe());
        player2Sprite = new Sprite(game.player2.getKeyframe());
        player1Sprite.setOrigin(25f, 0f);
        player1Sprite.setScale(playerScaleFactor);
        player2Sprite.setOrigin(25f, 0f);
        player2Sprite.setScale(playerScaleFactor);


        
        font = new BitmapFont();

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        pauseStage = new Stage(viewport, spriteBatch);
        // Ensure you have uiskin.atlas and uiskin.json in assets
        skinAtlas = new TextureAtlas(Gdx.files.internal("craftacular-ui.atlas"));        // CHANGED: load atlas
        skin = new Skin(Gdx.files.internal("craftacular-ui.json"), skinAtlas);          // CHANGED: pass atlas to skin

        Table table = new Table();
        table.setFillParent(true);
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        TextButton exitButton = new TextButton("Exit Game", skin);
        table.add(resumeButton).pad(10).row();
        table.add(mainMenuButton).pad(10).row();
        table.add(exitButton).pad(10);
        pauseStage.addActor(table);

        // CHANGED: Button listeners
        resumeButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                isPaused = false;                             // unpause on resume
            }
        });
        mainMenuButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));      // switch to MainMenu
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();                               // exit application
            }
        });

        // CHANGED: InputMultiplexer to handle game and pauseStage
        InputMultiplexer im = new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    isPaused = !isPaused;
                    return true;
                }
                return false;
            }
        }, pauseStage, new InputAdapter());
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        input();
        logic(delta);
        draw();
    }

    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            game.player1.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            game.player1.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            game.player1.kick();
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            game.player1.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            game.player1.punch();
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            game.player1.duck();

        if (Gdx.input.isKeyPressed(Input.Keys.L))
            game.player2.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE))
            game.player2.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
            game.player2.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.M ))
            game.player2.duck();
        if (Gdx.input.isKeyPressed(Input.Keys.SEMICOLON))
            game.player2.kick();
        if (Gdx.input.isKeyPressed(Input.Keys.P))
            game.player2.punch();
        if (Gdx.input.isKeyPressed(Input.Keys.G))
            toggleFullscreen();
    }

    private void logic(float delta) {
        game.player1.update(delta);
        game.player2.update(delta);
        syncSprite(game.player1, player1Sprite);
        syncSprite(game.player2, player2Sprite);
        //game.player2.update(delta);
    }

    private void syncSprite(Player player, Sprite sprite) {
        sprite.setX(player.position.x);
        sprite.setY(player.position.y);
        sprite.setScale(playerScaleFactor);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        spriteBatch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        player1Sprite.draw(spriteBatch);
        player2Sprite.draw(spriteBatch);
        spriteBatch.end();

        if (isPaused) {
            // CHANGED: Draw translucent overlay
            ScreenUtils.clear(0f, 0f, 0f, 0.5f);
            pauseStage.draw();
        }
    }

    private void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen()) {

            Gdx.graphics.setWindowedMode(1280, 720);
            camera.setToOrtho(false, 1280, 720);
            viewport.update(1280, 720, true);

        } else {

            DisplayMode displayMode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(displayMode);
            camera.setToOrtho(false, 1280, 720);
            viewport.update(displayMode.width, displayMode.height, true);
        }
        camera.update();
    }

    private void drawPauseMenu() {
        ScreenUtils.clear(Color.DARK_GRAY); // Hintergrund abdunkeln
        viewport.apply();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Halbtransparentes Overlay
        spriteBatch.setColor(0, 0, 0, 0.5f);
        spriteBatch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
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
        pauseStage.getViewport().update(width, height, true); // CHANGED
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
        spriteBatch.dispose();  // ADDED: dispose batch
        background.dispose();
        pauseStage.dispose();   // CHANGED: dispose stage
        skin.dispose();
        skinAtlas.dispose(); 
    }
}