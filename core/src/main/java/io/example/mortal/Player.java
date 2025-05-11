package io.example.mortal;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public Vector2 position;
	private Vector2 speed = new Vector2(500f, 0);;
    public float health;
	public float maxHealth;
	private float gravity = -40f;
	private float width = 50f;

	private boolean isMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isJumping = false;
	private boolean isDucking = false;
	private boolean isPunching = false;
	private boolean isKicking = false;

	private PlayerAnimations playerAnimations = new PlayerAnimations();
	public Animation<TextureRegion> animation = playerAnimations.standardAnim;
	public float currentAnimationTime = 0f;
	public boolean isPunchDead = true;
	public boolean isKickDead = true;


    public Player(Vector2 startPosition, float health) {
        this.position = startPosition;
        this.health = health;
		this.maxHealth = health;
    }

	public void setMovingLeft(boolean value) {
		isMovingLeft = value;
	}

	public void setMovingRight(boolean value) {
		isMovingRight = value;
	}

	public void update(float delta) {
		position.x -= (isMovingLeft ? 1 : 0) * speed.x * delta;
		position.x += (isMovingRight? 1 : 0) * speed.x * delta;
		
		speed.y += gravity;
		position.y += speed.y * delta;
		
		isJumping = position.y > 0;
		if(!isJumping)
			speed.y = 0;

		position.x = MathUtils.clamp(position.x, width/2, 500);
		position.y = MathUtils.clamp(position.y, 0, 450);
		
		isMovingLeft = false;
		isMovingRight = false;
		if(isPunching)
		handleActivePunch(delta);
		if(isKicking)
			handleActiveKick(delta);
	}

    public void jump() {
		if (isJumping)
			return;
		speed.y = 900f;
		isJumping = true;
	}

	public void duck() {
		isDucking = true;
	}

	public void punch() {
		if (isPunching || isKicking)
			return;
		isPunching = true;
		isPunchDead = false;
	}

	public void kick() {
		if (isKicking || isPunching)
			return;
		isKicking = true;
		isKickDead = false;
	}

	public void damage(int amount) {
		if(isDucking)
		health -= amount;
		if (health < 0)
			health = 0;
	}

	private void handleActivePunch(float delta) {
		currentAnimationTime += delta;
		if(animation.isAnimationFinished(currentAnimationTime)){
			currentAnimationTime = 0f;
			isPunching = false;
			isPunchDead = true;
		}
	}

	private void handleActiveKick(float delta) {
		currentAnimationTime += delta;
		if(animation.isAnimationFinished(currentAnimationTime))
			currentAnimationTime = 0f;
			isKicking = false;
			isKickDead = true;
	}

	public TextureRegion getKeyframe() {
		return animation.getKeyFrame(currentAnimationTime, true);
	}
}