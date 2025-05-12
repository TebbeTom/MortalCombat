package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SaveLoadManager {
    private static final String SAVE_FILE = "settings.txt";

    public static void save(Main game) {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        String data = game.selectedMap + ";" + game.player1Char + ";" + game.player2Char;
        file.writeString(data, false);
    }

    public static void load(Main game) {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        if (!file.exists()) return;
        String[] parts = file.readString().split(";");
        if (parts.length >= 3) {
            game.selectedMap = parts[0];
            game.player1Char = parts[1];
            game.player2Char = parts[2];
        }
    }

}