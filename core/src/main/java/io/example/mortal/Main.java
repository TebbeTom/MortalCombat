package io.example.mortal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;

import io.example.mortal.Screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public Player player1 = new Player(new Vector2(100f, 0f), 100);

    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }

    public void togglePause() {
        //setScreen(new PauseScreen(this));
    }
}