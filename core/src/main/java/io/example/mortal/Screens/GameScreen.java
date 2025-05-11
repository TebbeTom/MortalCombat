package io.example.mortal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.example.mortal.Main;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private Texture background;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Main game;
    private Sprite player1Sprite;
    private Sprite player2Sprite;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        background = new Texture("theBackground.jpeg");
        viewport = new FitViewport(900, 450); 
        spriteBatch = new SpriteBatch();
        player1Sprite = new Sprite(new Texture(null))
    }

    @Override
    public void render(float delta) {
        input();
        logic(delta);
        draw();
    }

    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            game.player1.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            game.player1.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            game.player1.duck();
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            game.player1.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.E))
            game.player1.punch();
        if (Gdx.input.isKeyPressed(Input.Keys.F))
            game.player1.kick();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            game.togglePause();

        if (Gdx.input.isKeyPressed(Input.Keys.O))
            game.player2.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.K))
            game.player2.setMovingLeft(true);
        if (Gdx.input.isKeyPressed(Input.Keys.L ))
            game.player2.duck();
        if (Gdx.input.isKeyPressed(Input.Keys.SEMICOLON))
            game.player2.setMovingRight(true);
        if (Gdx.input.isKeyPressed(Input.Keys.P))
            game.player2.punch();
        if (Gdx.input.isKeyPressed(Input.Keys.F))
            game.player2.kick();
        if (Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE))
            game.togglePause();
        
    }

    private void logic(float delta) {
        game.player1.update(delta);
        //game.player2.update(delta);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, 900, 450);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}