// Datei: GameMap.java
package io.example.mortal;

public enum GameEnum {
    RAMPART_SNOW("RampartSnow", "RampartSnow.png", "RampartSnow.mp3"),
    LANTERN_FEST("LanternFest", "LanternFest.png","LanternFest.mp3"),
    TEAHOUSE_NIGHT("TeahouseNight", "TeahouseNight.png", "TeahouseNight.mp3"),
    WUSHI_NIGHT("WuShiNight", "WuShiNight.png", "WuShiNight.mp3"),
    LIVING_FOREST("LivingForest", "LivingForest.png", "LivingForest.mp3");

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
