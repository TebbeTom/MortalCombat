package io.example.mortal;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimations {

    public enum CharacterType {
        MARTIAL_HERO, SAMURAI
    }

    public enum AnimationType {
        IDLE, RUN, ATTACK, HURT
    }

    private final HashMap<CharacterType, HashMap<AnimationType, Animation<TextureRegion>>> animations;

    public PlayerAnimations() {
        animations = new HashMap<>();

        // Martial Hero hat 8, 8, 6, 4 Frames
        animations.put(CharacterType.MARTIAL_HERO, loadAnimations(
                "MartialHero", "MartialHero", 
                8, 8, 6, 4
        ));

        // Samurai hat 10, 16, 8, 4 Frames
        animations.put(CharacterType.SAMURAI, loadAnimations(
                "Samurai", "Samurai", 
                10, 16, 8, 4
        ));
    }

    private HashMap<AnimationType, Animation<TextureRegion>> loadAnimations(
            String folderName,
            String filePrefix,
            int idleCount,
            int runCount,
            int attackCount,
            int hurtCount
    ) {
        HashMap<AnimationType, Animation<TextureRegion>> animMap = new HashMap<>();

        animMap.put(AnimationType.IDLE, loadAnimation("Spieler/" + folderName + "/Idle/", filePrefix + "Idle", idleCount));
        animMap.put(AnimationType.RUN, loadAnimation("Spieler/" + folderName + "/Run/", filePrefix + "Run", runCount));
        animMap.put(AnimationType.ATTACK, loadAnimation("Spieler/" + folderName + "/Attack/", filePrefix + "Attack", attackCount));
        animMap.put(AnimationType.HURT, loadAnimation("Spieler/" + folderName + "/Hurt/", filePrefix + "Hurt", hurtCount));

        return animMap;
    }

    private Animation<TextureRegion> loadAnimation(String path, String prefix, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = path + String.format("%s%03d.png", prefix, i);
            frames[i] = new TextureRegion(new Texture(filename));
        }
        return new Animation<>(0.1f, frames);
    }

    public Animation<TextureRegion> getAnimation(CharacterType type, AnimationType animType) {
        return animations.get(type).get(animType);
    }
}