
package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.example.mortal.Main;

public class MainMenuScreen implements Screen{

    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private StretchViewport viewport;

    private final int VIRTUAL_WIDTH = 1280;
    private final int VIRTUAL_HEIGHT = 720;


    private OrthographicCamera camera;

    private Main game;

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
        font = new BitmapFont();
        stage = new Stage(viewport, batch);

        atlas = new TextureAtlas(Gdx.files.internal("craftacular-ui.atlas"));
        skin  = new Skin(Gdx.files.internal("craftacular-ui.json"), atlas);

        Table table = new Table();
        table.setFillParent(true);
        TextButton startButton = new TextButton("START", skin);
        TextButton exitButton  = new TextButton("EXIT", skin);
        table.add(startButton).pad(20).row();
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
                game.switchScreen(new MapSelectScreen(game));
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
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
        batch.dispose();
        font.dispose();
        stage.dispose();  // CHANGED: dispose stage
        skin.dispose();   // CHANGED: dispose skin
        atlas.dispose();  // CHANGED: dispose atlas
    }
}
