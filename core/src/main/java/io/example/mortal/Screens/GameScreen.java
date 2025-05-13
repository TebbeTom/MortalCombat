package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.InputProcessor;

import io.example.mortal.Player;
import io.example.mortal.SaveLoadManager;
import io.example.mortal.Main;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private Texture background;
    private SpriteBatch spriteBatch;
    private StretchViewport viewport;
    private OrthographicCamera camera;

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

    private Music mapMusic;


    public GameScreen(Main game) {
        this.game = game;
        game.player1 = new Player(new Vector2(100f, 0f), 100, playerScaleFactor);
        game.player2 = new Player(new Vector2(500f, 0f), 100, playerScaleFactor);

        game.stopMenuMusic();  // Stoppt die Menümusik, wenn der GameScreen gestartet wird
    } 
    
    @Override
    public void show() {

        if (mapMusic != null && mapMusic.isPlaying()) {
            mapMusic.stop();
        }

        mapMusic = Gdx.audio.newMusic(Gdx.files.internal(game.selectedMap.musicFile));
        mapMusic.setLooping(true);
        mapMusic.setVolume(game.centralVolume);
        mapMusic.play();

        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        spriteBatch = new SpriteBatch();

        background = new Texture(game.selectedMap.texturePath);
        
        player1Sprite = new Sprite(game.player1.getKeyframe());
        player2Sprite = new Sprite(game.player2.getKeyframe());
        player1Sprite.setOrigin(25f, 0f);
        player1Sprite.setScale(playerScaleFactor);
        player2Sprite.setOrigin(25f, 0f);
        player2Sprite.setScale(playerScaleFactor);

        pauseStage = new Stage(viewport, spriteBatch);
        skinAtlas = new TextureAtlas(Gdx.files.internal("craftacular-ui.atlas"));
        skin = new Skin(Gdx.files.internal("craftacular-ui.json"), skinAtlas);

        Table table = new Table();
        table.setFillParent(true);

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isPaused) return;
                game.playClickEffect();
                isPaused = false;
                Gdx.input.setInputProcessor(defaultProcessor());
            }
        });
        table.add(resumeButton).pad(10).row();

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isPaused) return;
                game.playClickEffect();
                isPaused = false;
                SaveLoadManager.save(game);
                game.startMenuMusic();
                Gdx.graphics.setWindowedMode(1280, 720);
                game.switchScreen(new MainMenuScreen(game));
            }
        });
        table.add(mainMenuButton).pad(10).row();

        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isPaused) return;
                    game.playClickEffect();
                    isPaused = false;
                    Gdx.app.exit();
            }
        });
        table.add(exitButton).pad(10);
        pauseStage.addActor(table);

        Gdx.input.setInputProcessor(defaultProcessor());

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

        DisplayMode dm = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(dm);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

    }

    private InputProcessor defaultProcessor() {
        return new InputMultiplexer(
            new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {
                    if (keycode == Input.Keys.ESCAPE) {
                        isPaused = !isPaused;
                        Gdx.input.setInputProcessor(isPaused ? pauseStage : defaultProcessor());
                        return true;
                    }
                    return false;
                }
            },
            pauseStage
        );
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            input();
            logic(delta);
        }
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
    }

    private void logic(float delta) {
        game.player1.update(delta);
        game.player2.update(delta);
        syncSprite(game.player1, player1Sprite);
        syncSprite(game.player2, player2Sprite);
    }

    private void syncSprite(Player player, Sprite sprite) {
        sprite.setX(player.position.x);
        sprite.setY(player.position.y);
        sprite.setRegion(player.getKeyframe());
        sprite.setScale(playerScaleFactor);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        player1Sprite.draw(spriteBatch);
        player2Sprite.draw(spriteBatch);
        spriteBatch.end();

        if (isPaused) {
            ScreenUtils.clear(0f, 0f, 0f, 0.7f);
            pauseStage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        pauseStage.getViewport().update(width, height, true); // CHANGED
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
        mapMusic.pause();
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
        mapMusic.play();
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
        mapMusic.stop();
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        SaveLoadManager.save(game);
        spriteBatch.dispose();
        background.dispose();
        pauseStage.dispose();
        skin.dispose();
        skinAtlas.dispose();
        mapMusic.dispose(); 
    }
}