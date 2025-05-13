
package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.example.mortal.Main;

public class MainMenuScreen implements Screen{

    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private StretchViewport viewport;

    private final int VIRTUAL_WIDTH = 1280;
    private final int VIRTUAL_HEIGHT = 720;


    private OrthographicCamera camera;

    private Main game;

    private Array<Texture> videoFrames;
    private float frameTime;
    private int currentFrameIndex;

    public MainMenuScreen(Main game){
        this.game = game;
    }

    @Override
    public void show() {
        
        camera = new OrthographicCamera();

        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

        atlas = new TextureAtlas(Gdx.files.internal("craftacular-ui.atlas"));
        skin  = new Skin(Gdx.files.internal("craftacular-ui.json"), atlas);

        videoFrames = new Array<Texture>();
        loadVideoFrames();

        Table table = new Table();
        table.setFillParent(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("title");

        Label headerLabel = new Label("Sterblicher Kampf", labelStyle);
        table.add(headerLabel).pad(20).padBottom(50).row();

        TextButton startButton = new TextButton("START", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton  = new TextButton("EXIT", skin);


        table.add(startButton).pad(20).row();
        table.add(optionsButton).pad(10).row();
        table.add(exitButton).pad(20);
        stage.addActor(table);

        InputMultiplexer im = new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(im);

        // CHANGED: button listeners
        startButton.addListener(new ClickListener() {
            @Override public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.playClickEffect();
                game.switchScreen(new MapSelectScreen(game));
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClickEffect();
                game.switchScreen(new OptionScreen(game)); // Zum Optionsbildschirm wechseln
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.playClickEffect();
                Gdx.app.exit();
            }
        });
        
    }

    // Lade alle Frames des Videos in ein Array
    private void loadVideoFrames() {
        for (int i = 1; i <= 50; i++) {  // Angenommen, du hast 30 Frames im Ordner 'Fish'
            String framePath = "Fish/" + i + ".png";  // Frames sollten als frame1.png, frame2.png, ... benannt sein
            videoFrames.add(new Texture(Gdx.files.internal(framePath)));
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Abspielen des "Videos" aus den Frames
        frameTime += delta;
        if (frameTime >= 0.1f) { // 10 FPS für das Video (jede 0.1 Sekunden ein neuer Frame)
            currentFrameIndex = (currentFrameIndex + 1) % videoFrames.size; // Schleife durch die Frames
            frameTime = 0;
        }

        // Zeichne den aktuellen Frame des "Videos"
        batch.begin();
        batch.setColor(1f, 1f, 1f, 0.8f);  // Setze den Alpha-Wert auf 0.7 (dunkel, aber noch sichtbar)
        batch.draw(videoFrames.get(currentFrameIndex), 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (atlas != null) atlas.dispose();
        for (Texture frame : videoFrames) {
            frame.dispose(); // Entsorge alle Texturen, wenn sie nicht mehr benötigt werden
        }
    }
}
