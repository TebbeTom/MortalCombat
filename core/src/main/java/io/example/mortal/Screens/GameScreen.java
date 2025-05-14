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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import io.example.mortal.Player;
import io.example.mortal.SaveLoadManager;
import io.example.mortal.PlayerAnimations.AnimationType;
import io.example.mortal.PlayerAnimations.CharacterType;
import io.example.mortal.Main;

public class GameScreen implements Screen {
    // Constants for layout and visuals
    private static final int VIRTUAL_WIDTH = 1280;
    private static final int VIRTUAL_HEIGHT = 720;
    private static final float HEALTH_BAR_WIDTH = 300f;
    private static final float HEALTH_BAR_HEIGHT = 30f;
    private static final float HEALTH_BAR_Y = VIRTUAL_HEIGHT - 100f;
    private static final float HEALTH_BAR_MARGIN = 20f;
    private static final float KO_DURATION = 2.5f;
    
    // Colors for UI
    private static final Color HEALTH_BG_BORDER = Color.BLACK;
    private static final Color HEALTH_BG_FILL = new Color(0.2f, 0.2f, 0.2f, 1f);
    private static final Color HEALTH_P1_COLOR = new Color(1f, 0f, 0f, 0.8f);
    private static final Color HEALTH_P2_COLOR = new Color(0f, 1f, 0f, 0.8f);
    
    // Game state and visuals
    private final Main game;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    
    private Texture background;
    private Texture healthBarBackground, healthBarFill;
    private Sprite player1Sprite, player2Sprite;
    private float playerScaleFactor = 4f;

    private Stage pauseStage;
    private Skin skin;
    private TextureAtlas skinAtlas;
    private Music mapMusic;
    private BitmapFont koFont;
    
    private boolean isPaused = true;
    private boolean isFirstFrame = true;
    private boolean isAllAlive = true;
    private float koTimer = 0f;

    // Debugging
    private int debugHpP1 = 100;
    private int debugHpP2 = 100;
    
    /**
     * Constructs the GameScreen with initialized players and stops the menu music.
     *
     * @param game The main game instance containing player and map data.
     */
    public GameScreen(Main game) {
        this.game = game;
        this.game.player1 = new Player(new Vector2(100f, -80f), CharacterType.FIGHTER_MAN, 100);
        this.game.player2 = new Player(new Vector2(500f, -80f), CharacterType.MARTIAL_HERO, 100);
        this.game.stopMenuMusic();
    }

    /**
     * Called when this screen becomes the current screen for a Game.
     * Sets up rendering, camera, UI elements, input, and loads assets.
     */
    @Override
    public void show() {
        setupCameraAndViewport();
        loadAssets();
        setupPlayerSprites();
        createPauseMenu();
        setupInputHandling();
        configureKOFont();
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    private void setupCameraAndViewport() {
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
    }

    private void loadAssets() {
        spriteBatch = new SpriteBatch();
        background = new Texture(game.selectedMap.texturePath);

        mapMusic = Gdx.audio.newMusic(Gdx.files.internal(game.mortalKombat
            ? "sounds/backgroundMusic/mortal.mp3"
            : game.selectedMap.musicFile));
        mapMusic.setLooping(true);
        mapMusic.setVolume(game.centralVolume);
        mapMusic.play();

        healthBarBackground = createHealthBarBackground();
        healthBarFill = create1x1Texture(Color.WHITE);
    }

    /**
     * Creates the background texture for the health bar using a Pixmap.
     *
     * @return The generated health bar background texture.
     */
    private Texture createHealthBarBackground() {
        Pixmap pixmap = new Pixmap((int) HEALTH_BAR_WIDTH, (int) HEALTH_BAR_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(HEALTH_BG_BORDER);
        pixmap.fill();
        pixmap.setColor(HEALTH_BG_FILL);
        pixmap.fillRectangle(2, 2, (int) HEALTH_BAR_WIDTH - 4, (int) HEALTH_BAR_HEIGHT - 4);
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    /**
     * Creates a 1x1 texture with the specified color.
     *
     * @param color The color to fill the texture.
     * @return The generated texture.
     */
    private Texture create1x1Texture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    private void setupPlayerSprites() {
        player1Sprite = createPlayerSprite(game.player1);
        player2Sprite = createPlayerSprite(game.player2);
    }

    private Sprite createPlayerSprite(Player player) {
        Sprite sprite = new Sprite(player.getKeyframe());
        sprite.setOrigin(25f, 0f);
        sprite.setScale(playerScaleFactor);
        return sprite;
    }

    private void createPauseMenu() {
        pauseStage = new Stage(viewport, spriteBatch);
        skinAtlas = new TextureAtlas(Gdx.files.internal("craftacular-ui.atlas"));
        skin = new Skin(Gdx.files.internal("craftacular-ui.json"), skinAtlas);

        Table table = new Table();
        table.setFillParent(true);

        addPauseMenuButton(table, "Resume", () -> resumeGame());
        addPauseMenuButton(table, "Main Menu", () -> exitToMainMenu());
        addPauseMenuButton(table, "Exit Game", () -> Gdx.app.exit());

        pauseStage.addActor(table);
    }

    /**
     * Adds a button to the pause menu with a specified action.
     *
     * @param table   The table layout to add the button to.
     * @param text    The label text of the button.
     * @param onClick The action to execute when clicked.
     */
    private void addPauseMenuButton(Table table, String text, Runnable onClick) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isPaused) return;
                game.playClickEffect();
                isPaused = false;
                onClick.run();
            }
        });
        table.add(button).pad(10).row();
    }

    /**
     * Resumes gameplay by resetting the input processor.
     */
    private void resumeGame() {
        Gdx.input.setInputProcessor(defaultProcessor());
    }

    private void exitToMainMenu() {
        SaveLoadManager.save(game);
        game.startMenuMusic();
        Gdx.graphics.setWindowedMode(1280, 720);
        game.switchScreen(new MainMenuScreen(game));
    }

    /**
     * Sets up input handling, including pause toggling on ESC key.
     */
    private void setupInputHandling() {
        InputMultiplexer im = new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    isPaused = !isPaused;
                    return true;
                }
                return false;
            }
        }, pauseStage);
        Gdx.input.setInputProcessor(im);
    }

    private void configureKOFont() {
        koFont = skin.getFont("title");
        koFont.getData().setScale(2f);
    }

    /**
     * Returns the default input processor used during gameplay or when resumed.
     *
     * @return The configured InputProcessor.
     */
    private InputProcessor defaultProcessor() {
        return new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    isPaused = !isPaused;
                    Gdx.input.setInputProcessor(isPaused ? pauseStage : defaultProcessor());
                    return true;
                }
                return false;
            }
        }, pauseStage);
    }

    /**
     * Called when the game should render itself.
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        if (!isPaused && isAllAlive) {
            handleInput();
            updateLogic(delta);
        }

        if (handleKOState(delta))
            return;
        drawFrame();
        updateDebugHealthDisplay();
        if (isFirstFrame) {
            isPaused = false;
            isFirstFrame = false;
        }
    }

    /**
     * Processes user input and updates player actions.
     */
    private void handleInput() {
        if (!isAllAlive) return;
        // Input mapping for Player 1
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) game.player1.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) game.player1.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) game.player1.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) game.player1.punch();
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) game.player1.duck();
        // Input mapping for Player 2
        if (Gdx.input.isKeyPressed(Input.Keys.L)) game.player2.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE)) game.player2.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) game.player2.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.M)) game.player2.duck();
        if (Gdx.input.isKeyPressed(Input.Keys.P)) game.player2.punch();
    }

    /**
     * Updates player logic, checks for damage events, and syncs sprites.
     *
     * @param delta Time since the last frame.
     */
    private void updateLogic(float delta) {
        game.player1.update(delta);
        game.player2.update(delta);
        applyDamageIfInRange(game.player1, game.player2);
        syncSpriteToPlayer(game.player1, player1Sprite);
        syncSpriteToPlayer(game.player2, player2Sprite);
    }


    /**
     * Checks if either player is in range to apply damage and resolves it.
     *
     * @param p1 Attacking player.
     * @param p2 Target player.
     */
    private void applyDamageIfInRange(Player p1, Player p2) {
        if (p1.range >= Math.abs(p1.position.x - p2.position.x) && !p1.isPunchDead && p1.isAnimInHitZone) {
            p2.damage(p1.strength);
            p1.isPunchDead = true;
        }
        if (p2.range >= Math.abs(p2.position.x - p1.position.x) && !p2.isPunchDead && p2.isAnimInHitZone) {
            p1.damage(p2.strength);
            p2.isPunchDead = true;
        }
    }

    /**
     * Synchronizes a player's sprite position, frame, and flip state.
     *
     * @param player The player whose state is being synced.
     * @param sprite The sprite associated with the player.
     */
    private void syncSpriteToPlayer(Player player, Sprite sprite) {
        float otherX = (player == game.player1 ? game.player2.position.x : game.player1.position.x);
        sprite.setX(player.position.x);
        sprite.setY(player.position.y);
        sprite.setRegion(player.getKeyframe());
        boolean flip = player.animType == AnimationType.RUN ? player.getWasMovingLeft() : otherX < player.position.x;
        sprite.setFlip(flip, false);
        sprite.setScale(playerScaleFactor);
    }

    /**
     * Handles the KO state and screen transition after a KO event.
     *
     * @param delta Time since last frame.
     * @return True if KO screen transition occurred, false otherwise.
     */
    private boolean handleKOState(float delta) {
        if (game.player1.isDying || game.player2.isDying) mapMusic.stop();
        if ((game.player1.isFinishedDying || game.player2.isFinishedDying) && isAllAlive) {
            isAllAlive = false;
            koTimer = 0f;
        }
        if (!isAllAlive) {
            koTimer += delta;
            if (koTimer >= 1f + KO_DURATION) {
                String winner = game.player1.isFinishedDying ? game.player2Char : game.player1Char;
                game.switchScreen(new KOScreen(game, winner));
                return true;
            }
        }
        return false;
    }


	
    /**
     * Draws the entire frame, including background, players, UI, and overlays.
     */
    private void drawFrame() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply(true);
        spriteBatch.begin();
        drawBackgroundAndPlayers();
        drawUI();
        drawKOOverlay();
        spriteBatch.end();
        if (isPaused) {
            ScreenUtils.clear(0f, 0f, 0f, 0.7f);
            pauseStage.draw();
        }
    }

    private void drawBackgroundAndPlayers() {
        spriteBatch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        player1Sprite.draw(spriteBatch);
        player2Sprite.draw(spriteBatch);
    }

    private void drawUI() {
        BitmapFont uiFont = skin.getFont("font");
        uiFont.draw(spriteBatch, game.player1Char, 20, VIRTUAL_HEIGHT - 20);
        GlyphLayout layout = new GlyphLayout(uiFont, game.player2Char);
        uiFont.draw(spriteBatch, layout, VIRTUAL_WIDTH - layout.width - 20, VIRTUAL_HEIGHT - 20);
        drawHealthBar(spriteBatch, game.player1.health, 100, HEALTH_BAR_MARGIN, HEALTH_BAR_Y, HEALTH_P1_COLOR);
        drawHealthBar(spriteBatch, game.player2.health, 100, VIRTUAL_WIDTH - HEALTH_BAR_MARGIN - HEALTH_BAR_WIDTH, HEALTH_BAR_Y, HEALTH_P2_COLOR);
    }

    private void drawKOOverlay() {
        if (!isAllAlive) {
            spriteBatch.setColor(0, 0, 0, Math.min(koTimer / KO_DURATION, 1f));
            spriteBatch.draw(healthBarFill, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
            spriteBatch.setColor(Color.WHITE);
            GlyphLayout koLayout = new GlyphLayout(koFont, "K.O.");
            koFont.setColor(Color.RED);
            koFont.draw(spriteBatch, koLayout, (VIRTUAL_WIDTH - koLayout.width) / 2f, (VIRTUAL_HEIGHT + koLayout.height) / 2f);
        }
    }

    /**
     * Draws a health bar with the specified parameters.
     *
     * @param batch        The SpriteBatch used for drawing.
     * @param currentHealth The player's current health.
     * @param maxHealth     The player's maximum health.
     * @param x             The x-position of the health bar.
     * @param y             The y-position of the health bar.
     * @param fillColor     The color used to fill the health bar.
     */
    private void drawHealthBar(SpriteBatch batch, int currentHealth, int maxHealth, float x, float y, Color fillColor) {
        float ratio = Math.max(currentHealth / (float) maxHealth, 0f);
        float innerWidth = (HEALTH_BAR_WIDTH - 4) * ratio;
        batch.setColor(Color.WHITE);
        batch.draw(healthBarBackground, x, y);
        batch.setColor(fillColor);
        batch.draw(healthBarFill, x + 2, y + 2, innerWidth, HEALTH_BAR_HEIGHT - 4);
        batch.setColor(Color.WHITE);
    }

    /**
     * Prints updated health values to the console if they have changed.
     */
    private void updateDebugHealthDisplay() {
        if (game.player1.health != debugHpP1 || game.player2.health != debugHpP2) {
            debugHpP1 = game.player1.health;
            debugHpP2 = game.player2.health;
            System.out.printf("HP P1: %d\nHP P2: %d%n", debugHpP1, debugHpP2);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        pauseStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        mapMusic.pause();
    }

    @Override
    public void resume() {
        mapMusic.play();
    }

    @Override
    public void hide() {
        mapMusic.stop();
    }

    @Override
    public void dispose() {
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