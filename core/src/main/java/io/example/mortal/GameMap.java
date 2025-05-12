// Datei: GameMap.java
package io.example.mortal;

public enum GameMap {
    RAMPART_SNOW("RampartSnow", "RampartSnow.png"),
    LANTERN_FEST("LanternFest", "LanternFest.png"),
    TEAHOUSE_NIGHT("TeahouseNight", "TeahouseNight.png"),
    WUSHI_NIGHT("WuShiNight", "WuShiNight.png");

    public final String displayName;
    public final String texturePath;

    GameMap(String displayName, String texturePath) {
        this.displayName = displayName;
        this.texturePath = texturePath;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
