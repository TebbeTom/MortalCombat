
package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.example.mortal.Screens.GameScreen;

public class MainMenuScreen implements Screen{

    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private Rectangle startButtonBounds;
    private Rectangle exitButtonBounds;

    private Main game;

    public MainMenuScreen(Main game){
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Standard-Arial-artige BitmapFont
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); // Bildschirmgröße

        // Buttons als Rechtecke definieren
        startButtonBounds = new Rectangle(300, 250, 200, 50);
        exitButtonBounds = new Rectangle(300, 150, 200, 50);

        // Input Processor setzen
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPos = new Vector3(screenX, screenY, 0);
                camera.unproject(touchPos);

                if (startButtonBounds.contains(touchPos.x, touchPos.y)) {

                    System.out.println("Spiel starten!");
                    game.switchScreen(new GameScreen(game));


                } else if (exitButtonBounds.contains(touchPos.x, touchPos.y)) {
                    Gdx.app.exit();
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Hintergrundrechtecke für Buttons (einfach visuell)
        font.draw(batch, "START", startButtonBounds.x + 60, startButtonBounds.y + 35);
        font.draw(batch, "EXIT", exitButtonBounds.x + 70, exitButtonBounds.y + 35);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        Graphics.DisplayMode dm = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setWindowedMode(dm.width / 2, dm.height / 2);
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
    }
}
