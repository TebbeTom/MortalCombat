package io.example.mortal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimations {

    public Animation<TextureRegion> idleAnim;
    public Animation<TextureRegion> runAnim;
    public Animation<TextureRegion> attackAnim;
    public Animation<TextureRegion> hurtAnim;

    public PlayerAnimations() {
        idleAnim = loadAnimation("Spieler/Martial Hero/Idle/MartialHeroIdle", 8, 0.1f);
        runAnim = loadAnimation("Spieler/Martial Hero/Run/MartialHeroRun", 8, 0.1f);
        attackAnim = loadAnimation("Spieler/Martial Hero/Attack/MartialHeroAttack", 6, 0.08f);
        hurtAnim = loadAnimation("Spieler/Martial Hero/Hurt/MartialHeroHurt", 4, 0.12f);
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filePath = String.format("%s%03d.png", basePath, i);
            Texture texture = new Texture(filePath);
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(frameDuration, frames);
    }
}
