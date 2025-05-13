package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.example.mortal.GameEnum;
import io.example.mortal.Main;

/**
 * Screen zur Auswahl der Map vor Spielstart.
 */
public class MapSelectScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private OrthographicCamera camera;
    private StretchViewport viewport;

    private final GameEnum[] maps = GameEnum.values();

    public MapSelectScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(1280, 720, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage = new Stage(viewport, game.batch);

        skin = new Skin(
            Gdx.files.internal("craftacular-ui.json"),
            new com.badlogic.gdx.graphics.g2d.TextureAtlas(
                Gdx.files.internal("craftacular-ui.atlas"))
        );

        Table table = new Table();
        table.setFillParent(true);

        // Buttons für jede Map
        for (final GameEnum map : maps) {
            TextButton btn = new TextButton(map.displayName, skin);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.playClickEffect();
                    game.selectedMap = map;
                    game.switchScreen(new GameScreen(game));
                }
        });
    table.add(btn).pad(15).row();
}


        // Zurück zum Hauptmenü
        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClickEffect();
                game.switchScreen(new io.example.mortal.Screens.MainMenuScreen(game));
            }
        });
        table.add(backBtn).pad(15);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}