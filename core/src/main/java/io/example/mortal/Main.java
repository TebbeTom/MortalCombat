package io.example.mortal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.mortal.Screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public Player player1;
    public Player player2;

    public String selectedMap = "RampartSnow";
    public String player1Char = "Ninja";
    public String player2Char = "Robot";
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        SaveLoadManager.load(this);
        setScreen(new MainMenuScreen(this));
    }

    public void switchScreen(Screen newer){
        Screen oldScreen = getScreen();     
        setScreen(newer);
        if (oldScreen != null) oldScreen.dispose();
    }

    
}