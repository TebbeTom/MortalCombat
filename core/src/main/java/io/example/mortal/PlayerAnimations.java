package io.example.mortal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimations {
	public Animation<TextureRegion> standardAnim;
	public Animation<TextureRegion> punchAnim;
	public Animation<TextureRegion> kickAnim;
	public Animation<TextureRegion> duckAnim;
	private TextureRegion[] animationFrames;
	
	public PlayerAnimations() {
		animationFrames = new TextureRegion[1];
		animationFrames[0] = new TextureRegion(new Texture("Martial Hero/Sprites/Idle.png"), 75, 70, 50, 55);
		standardAnim = new Animation<TextureRegion>(1f, animationFrames);
	}
}
