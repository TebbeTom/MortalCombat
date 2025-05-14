package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SaveLoadManager {
    private static final String SAVE_FILE = "settings.txt";


    public static void save(Main game) {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        // Speichern der Lautstärke mit der Musik
        float volume = game.menuMusic != null ? game.menuMusic.getVolume() : 0.5f;  // Standardwert 0.5f falls null
        String data = game.selectedMap.name() + ";" + game.player1Char + ";" + game.player2Char + ";" + volume;
        file.writeString(data, false);
    }

    public static void load(Main game) {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        if (!file.exists()) return;
        String[] parts = file.readString().split(";");

        if (parts.length >= 3) {
            try {
                game.selectedMap = MapEnum.valueOf(parts[0]); // ✅ String → Enum
            } catch (IllegalArgumentException e) {
                game.selectedMap = MapEnum.RAMPART_SNOW; // Fallback
            }

            game.player1Char = parts[1];
            game.player2Char = parts[2];

            if (parts.length >= 4) {
            try {
                float volume = Float.parseFloat(parts[3]);
                game.setMusicVolume(volume); // Setze die Lautstärke direkt
            } catch (NumberFormatException e) {
                game.setMusicVolume(0.1f); // Fallback-Wert für Lautstärke, falls Parsing fehlschlägt
            }
        } else {
            // Falls keine Lautstärke gespeichert wurde, setze sie auf den Standardwert (0.5f)
            game.setMusicVolume(0.1f);
        }
        
        }


    }
}
