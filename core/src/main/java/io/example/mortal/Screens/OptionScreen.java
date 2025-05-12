package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.example.mortal.Main;
import io.example.mortal.SaveLoadManager;

public class OptionScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Slider volumeSlider;

    public OptionScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("craftacular-ui.json"), new com.badlogic.gdx.graphics.g2d.TextureAtlas(Gdx.files.internal("craftacular-ui.atlas")));

        Table table = new Table();
        table.setFillParent(true);

        float initialVolume = (game.menuMusic != null) ? game.menuMusic.getVolume() : 0.5f;
        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin); // 0.0 bis 1.0 für Lautstärke
        volumeSlider.setValue(initialVolume);

        volumeSlider.addListener(event -> {
        if (event.getTarget() instanceof Slider) {
            float volume = ((Slider) event.getTarget()).getValue();
            game.setMusicVolume(volume); // Lautstärke aktualisieren
            SaveLoadManager.save(game);  // Lautstärke in die settings.txt speichern
        }
        return false;
        });

        // Zurück zum Hauptmenü
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.switchScreen(new MainMenuScreen(game)); // Zurück zum Hauptmenü
            }
        });

        table.add(volumeSlider).pad(10).row();
        table.add(backButton).pad(10).row();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) {
        stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }
}
