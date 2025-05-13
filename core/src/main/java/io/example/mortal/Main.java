package io.example.mortal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.example.mortal.Screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public Player player1;
    public Player player2;

    public GameEnum selectedMap = GameEnum.RAMPART_SNOW;
    public String player1Char = "Ninja";
    public String player2Char = "Robot";
    public SpriteBatch batch;

    public Music menuMusic;
    public float centralVolume = 0.5f;
    public static Sound clickEffect;

    @Override
    public void create() {
        batch = new SpriteBatch();

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu_music.mp3"));
        clickEffect = Gdx.audio.newSound(Gdx.files.internal("clickEffect.mp3"));

        menuMusic.setLooping(true);  // Menu Musik soll wiederholt werden
        
        SaveLoadManager.load(this);

        if (menuMusic.getVolume() == 0f) {
            setMusicVolume(0.5f);
        }

        startMenuMusic();

        setScreen(new MainMenuScreen(this));
    }

    public void playClickEffect() {
        if (clickEffect != null) {
            clickEffect.play();  // Spielt den Klick-Sound ab
        }
    }

    public void setMusicVolume(float volume) {
        if (menuMusic != null) {
            menuMusic.setVolume(volume);
        }
        this.centralVolume = volume;
    }

    public void switchScreen(Screen newer){
        Screen oldScreen = getScreen();     
        setScreen(newer);
        if (oldScreen != null) oldScreen.dispose();
    }

    // Methode, um die Musik zu starten (Main Menu Musik)
    public void startMenuMusic() {
        if (menuMusic != null && !menuMusic.isPlaying()) {
            menuMusic.play();
        }
}


    // Methode, um die Musik zu stoppen (Main Menu Musik)
    public void stopMenuMusic() {
        if (menuMusic != null && menuMusic.isPlaying()) {
            menuMusic.stop();
        }
}

}