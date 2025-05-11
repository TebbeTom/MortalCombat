package io.example.mortal;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public Player player1;
    public Player player2;

    
    Map<String, Integer> resolution;

    @Override
    public void create() {
        resolution = new HashMap<>();
        resolution.put("width", 1280);
        resolution.put("height", 720);
    

        setScreen(new MainMenuScreen(this));
    }

    public Map<String, Integer> getResolution() {
        return resolution;
    }

    public void switchScreen(Screen newer){
        Screen oldScreen = getScreen();     
        setScreen(newer);
        if (oldScreen != null) oldScreen.dispose();
    }

    
}