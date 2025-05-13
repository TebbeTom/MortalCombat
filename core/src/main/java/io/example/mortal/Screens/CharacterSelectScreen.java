//Diese Klasse wird nicht benutzt, wäre aber ein Te

/* 
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
import io.example.mortal.Main;

public class CharacterSelectScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private final String[] chars = {"Ninja", "Robot", "Samurai"};
    private int choiceIndex = 0;

    public CharacterSelectScreen(Main game) {
        this.game = game;
    }

    @Override public void show() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(1280, 720, camera);
        stage = new Stage(viewport, game.batch);
        skin = new Skin(Gdx.files.internal("craftacular-ui.json"), new com.badlogic.gdx.graphics.g2d.TextureAtlas(
            Gdx.files.internal("craftacular-ui.atlas")));

        Table table = new Table();
        table.setFillParent(true);

        for (final String ch : chars) {
            TextButton btn = new TextButton(ch, skin);
            btn.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) {
                    if (choiceIndex == 0) {
                        game.player1Char = ch;
                        choiceIndex++;
                        // indicate next is enemy
                    } else {
                        game.player2Char = ch;
                        game.switchScreen(new GameScreen(game));
                    }
                }
            });
            table.add(btn).pad(10).row();
        }

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}
    */