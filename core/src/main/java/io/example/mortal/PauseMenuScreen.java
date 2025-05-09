// PauseMenuScreen.java
package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Pausen-Menü: zentrierter Text + zwei Buttons unterhalb
 */
public class PauseMenuScreen implements Screen {
    private final Main game;
    private OrthographicCamera cam;
    private FitViewport vp;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture homeBtn, quitBtn;
    private static final int BTN_SIZE = 64;

    public PauseMenuScreen(Main game) { this.game=game; }

    @Override
    public void show() {
        cam = new OrthographicCamera();
        cam.setToOrtho(false,1280,720);
        vp = new FitViewport(1280,720,cam);
        batch = new SpriteBatch();
        font = new BitmapFont(); font.getData().setScale(2f);
        homeBtn = new Texture("HomeButton.png");
        quitBtn = new Texture("QuitButton.png");
        // Input für Buttons
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override public boolean touchDown(int x,int y,int p,int b){
                float wx = x * (1280f/Gdx.graphics.getWidth());
                float wy = (Gdx.graphics.getHeight()-y) * (720f/Gdx.graphics.getHeight());
                // Home oben links
                if(wx<BTN_SIZE+20 && wy>720-20-BTN_SIZE) game.switchScreen(new MainMenuScreen(game));
                // Quit oben rechts
                if(wx>1280-20-BTN_SIZE && wy>720-20-BTN_SIZE) Gdx.app.exit();
                return true;
            }

            @Override
            public boolean keyDown(int key) {
            if (key == Input.Keys.ESCAPE) {
                // Zurück ins Spiel
                game.switchScreen(new GameScreen(game));
                return true;
            }
            return false;
        }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);
        vp.apply();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        // Pausen-Text
        font.draw(batch,"Pausemenü",520,450);
        font.draw(batch,"Drücke ESC zum Fortsetzen",440,400);
        // Buttons
        batch.draw(homeBtn,20,720-20-BTN_SIZE,BTN_SIZE,BTN_SIZE);
        batch.draw(quitBtn,1280-20-BTN_SIZE,720-20-BTN_SIZE,BTN_SIZE,BTN_SIZE);
        batch.end();
    }

    @Override public void resize(int w,int h){ vp.update(w,h,true);}    
    @Override public void pause(){}  @Override public void resume(){}  
    @Override public void hide(){}   
    @Override public void dispose(){batch.dispose();font.dispose();homeBtn.dispose();quitBtn.dispose();}
}
