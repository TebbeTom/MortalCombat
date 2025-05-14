package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import io.example.mortal.PlayerAnimations.AnimationType;
import io.example.mortal.PlayerAnimations.CharacterType;
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
    private final static int VIRTUAL_HEIGHT = 720;

    private Main game;
    private Sprite player1Sprite;
    private Sprite player2Sprite;
    private float playerScaleFactor = 4f;

    private static final float HEALTH_BAR_WIDTH = 300f;
    private static final float HEALTH_BAR_HEIGHT = 30f;
    private static final float HEALTH_BAR_Y = (float)VIRTUAL_HEIGHT - 100f;     
    private static final float HEALTH_BAR_MARGIN = 20f;
    private static final Color HEALTH_BG_BORDER = Color.BLACK;     
    private static final Color HEALTH_BG_FILL = new Color(0.2f, 0.2f, 0.2f, 1f); 
    private static final Color HEALTH_P1_COLOR = new Color(1f, 0f, 0f, 0.8f);
    private static final Color HEALTH_P2_COLOR = new Color(0f, 1f, 0f, 0.8f);
    private Texture healthBarBackground;
    private Texture healthBarFill;

    private int debugHpP1 = 100;
    private int debugHpP2 = 100;

    private boolean isPaused = false;

    private Music mapMusic;

    private boolean isAllAlive = true;
    private float koTimer = 0f;
    private static final float KO_DURATION = 2.5f;
    private BitmapFont koFont;



    public GameScreen(Main game) {
        this.game = game;
        game.player1 = new Player(new Vector2(100f, -80f), CharacterType.FIGHTER_MAN, 100, playerScaleFactor);
        game.player2 = new Player(new Vector2(500f, -80f), CharacterType.MARTIAL_HERO, 100, playerScaleFactor);
        
        game.stopMenuMusic();
    } 
    
    @Override
    public void show() {

        if (mapMusic != null && mapMusic.isPlaying()) {
            mapMusic.stop();
        }

        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        if(!(this.game.mortalKombat)){
            mapMusic = Gdx.audio.newMusic(Gdx.files.internal(game.selectedMap.musicFile));
        }else{
            mapMusic = Gdx.audio.newMusic(Gdx.files.internal("mortal.mp3"));
        }
        mapMusic.setLooping(true);
        mapMusic.setVolume(game.centralVolume);
        mapMusic.play();

        spriteBatch = new SpriteBatch();

        background = new Texture(game.selectedMap.texturePath);

        Pixmap bgPixmap = new Pixmap((int)HEALTH_BAR_WIDTH, (int)HEALTH_BAR_HEIGHT, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(HEALTH_BG_BORDER);
        bgPixmap.fill();
        bgPixmap.setColor(HEALTH_BG_FILL);
        bgPixmap.fillRectangle(2, 2, (int)HEALTH_BAR_WIDTH - 4, (int)HEALTH_BAR_HEIGHT - 4);
        healthBarBackground = new Texture(bgPixmap);
        bgPixmap.dispose();

        Pixmap fillPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        fillPixmap.setColor(Color.WHITE);
        fillPixmap.fill();
        healthBarFill = new Texture(fillPixmap);
        fillPixmap.dispose();
        
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
        
        koFont = skin.getFont("title");
        koFont.getData().setScale(2f);

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
        if (!isPaused && isAllAlive) {
            input();
            logic(delta);
        }

        if (game.player1.isDying || game.player2.isDying)
            mapMusic.stop();

        if ((game.player1.isFinishedDying || game.player2.isFinishedDying) && isAllAlive){
            isAllAlive = false;
            koTimer = 0f;
        }


        if (!isAllAlive) {
            koTimer += delta;

            // KO_DURATION ist Fadedauer, 2 Sekunden extra Verzögerung vorher:
            if (koTimer >= 1f + KO_DURATION) {
                String winner = game.player1.isFinishedDying ? game.player2Char : game.player1Char;
                game.switchScreen(new KOScreen(game, winner));
                return;
            }
}

        draw();
        //todo: remove following

        if (game.player1.health != debugHpP1 || game.player2.health != debugHpP2){
            debugHpP1 = game.player1.health;
            debugHpP2 = game.player2.health;
            System.out.println(String.format("HP P1: %d\nHP P2: %d", debugHpP1, debugHpP2) );

        }
    }

    private void input() {
        if (!isAllAlive) return;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            game.player1.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            game.player1.setMovingLeft(true);
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
        if (Gdx.input.isKeyPressed(Input.Keys.P))
            game.player2.punch();
    }

    private void logic(float delta) {
        game.player1.update(delta);
        game.player2.update(delta);
        dealDamageIfNeeded(game.player1, game.player2);
        syncSprite(game.player1, player1Sprite);
        syncSprite(game.player2, player2Sprite);
        //game.player2.update(delta);
    }

    private void dealDamageIfNeeded(Player p1, Player p2) {
        if (p1.range >= (Math.abs(p1.position.x - p2.position.x)))
            if (!p1.isPunchDead &&
            p1.isAnimInHitZone){
                p2.damage(p1.strength);
                p1.isPunchDead = true;
            }

            
        if (p2.range >= (Math.abs(p2.position.x - p1.position.x)))
            if (!p2.isPunchDead && p2.isAnimInHitZone){
                p1.damage(p2.strength);
                p2.isPunchDead = true;
            }
    }

    private void syncSprite(Player player, Sprite sprite) {
        float otherPlayerX = player.position.x == game.player1.position.x ? game.player2.position.x : game.player1.position.x;
        sprite.setX(player.position.x);
        sprite.setY(player.position.y);
        sprite.setRegion(player.getKeyframe());
        if (player.animType == AnimationType.RUN){
            if (player.getWasMovingLeft())
                sprite.setFlip(true, false);
        } else
            sprite.setFlip(otherPlayerX < player.position.x, false);
        sprite.setScale(playerScaleFactor);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        player1Sprite.draw(spriteBatch);
        player2Sprite.draw(spriteBatch);

        BitmapFont uiFont = skin.getFont("font");
        GlyphLayout layout = new GlyphLayout();
        uiFont.draw(spriteBatch, game.player1Char, 20, VIRTUAL_HEIGHT - 20);

        layout.setText(uiFont, game.player2Char);
        float name2Width = layout.width;
        uiFont.draw(spriteBatch, game.player2Char,VIRTUAL_WIDTH - name2Width - 20,VIRTUAL_HEIGHT - 20);
    
        drawHealthBar(spriteBatch, game.player1.health, 100, HEALTH_BAR_MARGIN, HEALTH_BAR_Y, HEALTH_P1_COLOR);
        drawHealthBar(spriteBatch, game.player2.health, 100, VIRTUAL_WIDTH - HEALTH_BAR_MARGIN - HEALTH_BAR_WIDTH, HEALTH_BAR_Y, HEALTH_P2_COLOR);

        
        spriteBatch.setColor(0, 0, 0, Math.min(koTimer / KO_DURATION, 1f));
        spriteBatch.draw(healthBarFill, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        spriteBatch.setColor(Color.WHITE); // Reset Farbe
        
        spriteBatch.end();

        if (isPaused) {
            ScreenUtils.clear(0f, 0f, 0f, 0.7f);
            pauseStage.draw();
        }

        if (!isAllAlive) {
            spriteBatch.begin();
    
            // Schwarzer semi-transparenter Overlay
            spriteBatch.setColor(0, 0, 0, Math.min(koTimer / KO_DURATION, 1f));
            spriteBatch.draw(healthBarFill, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
            spriteBatch.setColor(Color.WHITE); // Reset Farbe

            // K.O.-Text
            GlyphLayout koLayout = new GlyphLayout(koFont, "K.O.");
            koFont.setColor(Color.RED);
            koFont.draw(spriteBatch, koLayout,
                (VIRTUAL_WIDTH - koLayout.width) / 2f,
                (VIRTUAL_HEIGHT + koLayout.height) / 2f);

            spriteBatch.end();
        }
    }

    private void drawHealthBar(SpriteBatch batch, int currentHealth, int maxHealth, float x, float y, Color fillColor) {
        float ratio = Math.max(currentHealth / (float)maxHealth, 0f);
        float innerWidth = (HEALTH_BAR_WIDTH - 4) * ratio;

        // Hintergrund inkl. Rand
        batch.setColor(Color.WHITE);
        batch.draw(healthBarBackground, x, y);

        // Füllung (gescaled nach ratio)
        batch.setColor(fillColor);
        batch.draw(healthBarFill, x + 2, y + 2, innerWidth, HEALTH_BAR_HEIGHT - 4);

        batch.setColor(Color.WHITE); // RESET
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
        healthBarBackground.dispose();
        healthBarFill.dispose(); 
    }
}