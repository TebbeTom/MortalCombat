package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.example.mortal.Main;
import io.example.mortal.SaveLoadManager;

public class NameSelectScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private OrthographicCamera camera;
    private StretchViewport viewport;

    private TextField name1Field;
    private TextField name2Field;

    public Music easterEgg;
    public boolean eggLock = false;
    public NameSelectScreen(Main game) {
        this.game = game;
        easterEgg = Gdx.audio.newMusic(Gdx.files.internal("easterEggName.mp3"));
        easterEgg.setLooping(false);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(1280, 720, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(
            Gdx.files.internal("craftacular-ui.json"),
            new com.badlogic.gdx.graphics.g2d.TextureAtlas(
                Gdx.files.internal("craftacular-ui.atlas"))
        );

        Table table = new Table();
        // --- gelöscht: method chaining führt zu Fehler
        // table.setFillParent(true).center().pad(20);
        // +++ neu: einzeln aufrufen
        table.setFillParent(true);
        table.center();
        table.pad(20);

        // Felder nebeneinander
        name1Field = new TextField("", skin);
        name1Field.setMessageText("Player 1");
        name2Field = new TextField("", skin);
        name2Field.setMessageText("Player 2");

        table.add(name1Field).width(250).padRight(30);
        table.add(name2Field).width(250).row();
        table.row().padTop(30);

        TextButton confirmBtn = new TextButton("Confirm", skin);
        confirmBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (eggLock){
                 return;
                }
                game.playClickEffect();
                String n1 = name1Field.getText().trim();
                String n2 = name2Field.getText().trim();

                if (n1.isEmpty()) n1 = "  Player 1";
                if (n2.isEmpty()) n2 = "  Player 2";

                game.player1Char = n1;
                game.player2Char = n2;
                SaveLoadManager.save(game);

                if (n1.equals("Jannik") || n2.equals("Jannik")) {
                    eggLock = true;
                    easterEgg.play();

                    // Sounddauer (in Sekunden) — musst du kennen!
                    float soundDuration = 2f;

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            game.switchScreen(new GameScreen(game));
                        }
                    }, soundDuration); // Nach Sounddauer ausführen
                } else {
                    if (n1.equals("Mortal") && n2.equals("Kombat")) {
                        game.mortalKombat = true;
                    }
                }
                game.switchScreen(new GameScreen(game));

            }
        });
        table.add(confirmBtn).colspan(2).width(200).padTop(15).row();

        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (eggLock){
                 return;
                }
                game.playClickEffect();
                game.switchScreen(new io.example.mortal.Screens.MapSelectScreen(game));
            }
        });
        table.add(backBtn).colspan(2).width(200).padTop(15);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.12f, 0.12f, 0.18f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
