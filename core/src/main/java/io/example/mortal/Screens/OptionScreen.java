package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import io.example.mortal.Main;
import io.example.mortal.SaveLoadManager;

public class OptionScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Slider volumeSlider;

    public static final String KEYBINDS_TEXT_P1 =
        "PLAYER 1:\n" +
        "Jump: W\n" +
        "Drop: S\n" +
        "Move Left: A\n" +
        "Move Right: D\n" +
        "Punch: SPACE\n";

    public static final String KEYBINDS_TEXT_P2 =
        "PLAYER 2:\n" +
        "Jump: NUMPAD_8\n" +
        "Move Left: NUMPAD_4\n" +
        "Drop: NUMPAD_5\n" +
        "Move Right: NUMPAD_6\n" +
        "Punch: NUMPAD_ENTER\n";

    public OptionScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("craftacular-ui.json"), new com.badlogic.gdx.graphics.g2d.TextureAtlas(Gdx.files.internal("craftacular-ui.atlas")));

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        Label volumeLabel = new Label("Change Volume", labelStyle);

        float initialVolume = (game.menuMusic != null) ? game.menuMusic.getVolume() : 0.5f;
        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(initialVolume);

        volumeSlider.addListener(event -> {
        if (event.getTarget() instanceof Slider) {
            float volume = ((Slider) event.getTarget()).getValue();
            game.setMusicVolume(volume);
            SaveLoadManager.save(game);
        }
        return false;
        });

        Label keybindsLabelP1 = new Label(KEYBINDS_TEXT_P1, skin);
        Label keybindsLabelP2 = new Label(KEYBINDS_TEXT_P2, skin);
        keybindsLabelP1.setWrap(true); // Falls Text umbrechen soll
        keybindsLabelP1.setFontScale(0.8f); // Optional, falls zu groß
        keybindsLabelP1.setAlignment(Align.center);
        keybindsLabelP2.setWrap(true); // Falls Text umbrechen soll
        keybindsLabelP2.setFontScale(0.8f); // Optional, falls zu groß
        keybindsLabelP2.setAlignment(Align.center);

        // Tabelle erweitern
        table.add(keybindsLabelP1).width(500).pad(20);
        table.add(keybindsLabelP2).width(500).pad(20).row();

        // Zurück zum Hauptmenü
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClickEffect();
                game.switchScreen(new MainMenuScreen(game)); // Zurück zum Hauptmenü
            }
        });

        table.add(volumeLabel).colspan(2).padBottom(20).row();
        table.add(volumeSlider).colspan(2).width(backButton.getWidth()).pad(10).row();
        table.add(backButton).colspan(2).pad(10).row();

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
    }

    @Override
    public void resume() {
    }
}
