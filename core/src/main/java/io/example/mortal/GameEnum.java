// Datei: GameMap.java
package io.example.mortal;

public enum GameEnum {
    RAMPART_SNOW("Rampart Snow", "RampartSnow.png", "RampartSnow.mp3"),
    LANTERN_FEST("Lantern Fest", "LanternFest.png","LanternFest.mp3"),
    TEAHOUSE_NIGHT("Teahouse Night", "TeahouseNight.png", "TeahouseNight.mp3"),
    WUSHI_NIGHT("Wu-Shi Night", "WuShiNight.png", "WuShiNight.mp3"),
    LIVING_FOREST("Living Forest", "LivingForest.png", "LivingForest.mp3");

    public final String displayName;
    public final String texturePath;
    public final String musicFile;

    GameEnum(String displayName, String texturePath, String musicFile) {
        this.displayName = displayName;
        this.texturePath = texturePath;
        this.musicFile = musicFile;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
