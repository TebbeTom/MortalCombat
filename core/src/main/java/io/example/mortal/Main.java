package io.example.mortal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.mortal.Screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public Player player1;
    public Player player2;

    public GameMap selectedMap = GameMap.RAMPART_SNOW;
    public String player1Char = "Ninja";
    public String player2Char = "Robot";
    public SpriteBatch batch;

    public Music menuMusic;

    @Override
    public void create() {
        batch = new SpriteBatch();

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("LocoGigante.mp3"));
        menuMusic.setLooping(true);  // Musik soll sich wiederholen
        menuMusic.play();

        SaveLoadManager.load(this);

        if (menuMusic.getVolume() == 0f) {
            setMusicVolume(0.5f);
        }

        setScreen(new MainMenuScreen(this));
    }

    public void setMusicVolume(float volume) {
        if (menuMusic != null) {
            menuMusic.setVolume(volume);
        }
    }

    public void switchScreen(Screen newer){
        Screen oldScreen = getScreen();     
        setScreen(newer);
        if (oldScreen != null) oldScreen.dispose();
    }

    public void stopMusic() {
        if (menuMusic.isPlaying()) {
            menuMusic.stop();
        }
    }
}