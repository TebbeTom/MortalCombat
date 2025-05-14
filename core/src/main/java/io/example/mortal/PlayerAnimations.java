package io.example.mortal;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimations {

    public enum CharacterType {
        MARTIAL_HERO, FIGHTER_MAN
    }

    public enum AnimationType {
        IDLE, RUN, ATTACK, HURT, JUMP, HIT, DEATH
    }

    private final HashMap<CharacterType, HashMap<AnimationType, Animation<TextureRegion>>> animations;

    public PlayerAnimations() {
        animations = new HashMap<>();

        // Martial Hero hat 8, 8, 6, 4 Frames
        animations.put(CharacterType.MARTIAL_HERO, loadAnimations(
                "MartialHero",
                8,   // idleCount
                8,   // runCount
                4,   // attackCount
                2,   // jumpCount
                4,   // hitCount
                6    // deathCount
        ));

        // Samurai hat 10, 16, 8, 4 Frames
        animations.put(CharacterType.FIGHTER_MAN, loadAnimations(
                "FighterMan",
                4,   // idleCount
                8,   // runCount
                4,   // attackCount
                2,   // jumpCount
                3,   // hitCount
                7    // deathCount
        ));
    }

    private HashMap<AnimationType, Animation<TextureRegion>> loadAnimations(
            String folderName,
            int idleCount,
            int runCount,
            int attackCount,
            int jumpCount,
            int hitCount,
            int deathCount
    ) {
        HashMap<AnimationType, Animation<TextureRegion>> animMap = new HashMap<>();

        animMap.put(AnimationType.IDLE, loadAnimation("Spieler/" + folderName + "/Idle/" + "Idle", idleCount));
        animMap.put(AnimationType.RUN, loadAnimation("Spieler/" + folderName + "/Run/" + "Run", runCount));
        animMap.put(AnimationType.ATTACK, loadAnimation("Spieler/" + folderName + "/Attack/" + "Attack", attackCount));
        animMap.put(AnimationType.JUMP, loadAnimation("Spieler/" + folderName + "/Jump/Jump", jumpCount));  // CHANGED: Jump ergänzt
        animMap.put(AnimationType.HIT, loadAnimation("Spieler/" + folderName + "/Hit/Hit", hitCount));      // CHANGED: Hit ergänzt
        animMap.put(AnimationType.DEATH, loadAnimation("Spieler/" + folderName + "/Death/Death", deathCount));


        return animMap;
    }

    private Animation<TextureRegion> loadAnimation(String path, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = path + String.format("%03d.png", i);
            frames[i] = new TextureRegion(new Texture(filename));
        }
        return new Animation<>(0.1f, frames);
    }

    public Animation<TextureRegion> getAnimation(CharacterType type, AnimationType animType) {
        return animations.get(type).get(animType);
    }
}