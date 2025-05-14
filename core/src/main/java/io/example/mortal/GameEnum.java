// Datei: GameMap.java
package io.example.mortal;

public enum GameEnum {
    RAMPART_SNOW("Rampart Snow", "backgrounds/RampartSnow.png", "sounds/backgroundMusic/RampartSnow.mp3"),
    LANTERN_FEST("Lantern Fest", "backgrounds/LanternFest.png","sounds/backgroundMusic/LanternFest.mp3"),
    TEAHOUSE_NIGHT("Teahouse Night", "backgrounds/TeahouseNight.png", "sounds/backgroundMusic/TeahouseNight.mp3"),
    WUSHI_NIGHT("Wu-Shi Night", "backgrounds/WuShiNight.png", "sounds/backgroundMusic/WuShiNight.mp3"),
    LIVING_FOREST("Living Forest", "backgrounds/LivingForest.png", "sounds/backgroundMusic/LivingForest.mp3");

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
