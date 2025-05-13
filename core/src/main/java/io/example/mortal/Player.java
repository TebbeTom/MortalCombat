package io.example.mortal;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.example.mortal.PlayerAnimations.AnimationType;
import io.example.mortal.PlayerAnimations.CharacterType;

public class Player {
    public Vector2 position;
	private Vector2 speed = new Vector2(500f, 0);
    public int health;
	public float maxHealth;
	private float gravity = -40f;
	private float width;
	public float range = 40f;
	public int strength = 10;
	private float startHitTime = 0.3f;
	private float endHitTime = 0.8f;
	public boolean isAnimInHitZone = false;

	private boolean isMovingLeft = false;
	private boolean wasMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isJumping = false;
	private boolean isDucking = false;
	private boolean isPunching = false;

	private PlayerAnimations playerAnimations = new PlayerAnimations();
	public CharacterType characterType;
	public AnimationType animType = AnimationType.IDLE;
	public Animation<TextureRegion> animation;
	public float currentAnimationTime = 0f;
	public boolean isPunchDead = true;


    public Player(Vector2 startPosition, CharacterType characterType, int health, float scaleFactor) {
        this.position = startPosition;
		this.characterType = characterType;
        this.health = health;
		this.maxHealth = health;
		this.width = 200f;

		this.animation = playerAnimations.getAnimation(characterType, animType);
    }

	public void setMovingLeft(boolean value) {
		if (isPunching)
			return;
		if (animType != AnimationType.RUN)
			changeAnim(AnimationType.RUN);
		isMovingLeft = value;
	}

	public boolean getWasMovingLeft() {
		return wasMovingLeft;
	}

	public void setMovingRight(boolean value) {
		if (isPunching)
			return;
		if (animType != AnimationType.RUN)
			changeAnim(AnimationType.RUN);
		isMovingRight = value;
	}

	public void update(float delta) {
		currentAnimationTime += delta;
		position.x -= (isMovingLeft ? 1 : 0) * speed.x * delta;
		position.x += (isMovingRight? 1 : 0) * speed.x * delta;
		
		speed.y += gravity;
		position.y += speed.y * delta;
		
		isJumping = position.y > -290;
		if(!isJumping)
			speed.y = 0;

		position.x = MathUtils.clamp(position.x, -280, 650);
		position.y = MathUtils.clamp(position.y, -290, 450);
		
		if (isIdle() && animType != AnimationType.IDLE) { // ^ == XOR
			changeAnim(AnimationType.IDLE);
		}
		wasMovingLeft = isMovingLeft;
		isMovingLeft = false;
		isMovingRight = false;
		isDucking = false;
		if(isPunching)
			handleActivePunch(delta);
	}

    public void jump() {
		if (isJumping)
			return;
		speed.y = 900f;
		isJumping = true;
	}

	public void duck() {
		if(!(isJumping || isPunching))
			isDucking = true;
	}

	private boolean isIdle() {
		if (
			isDucking ||
			(isMovingLeft ^ isMovingRight) ||
			isPunching
		){ return false;}
		return true;
	}

	public void punch() {
		if (isPunching)
			return;
		isPunching = true;
		isPunchDead = false;
		changeAnim(AnimationType.ATTACK);
	}

	public void damage(int amount) {
		if(!isDucking)
			health -= amount;
		if (health < 0)
			health = 0;
	}

	private void handleActivePunch(float delta) {
		if (currentAnimationTime > startHitTime && currentAnimationTime < endHitTime)
			isAnimInHitZone = true;
		else
			isAnimInHitZone = false;
		if(animation.isAnimationFinished(currentAnimationTime)){
			changeAnim(AnimationType.IDLE);
			isPunching = false;
			isPunchDead = true;
		}
	}

	private void changeAnim(AnimationType animType) {
		this.currentAnimationTime = 0f;
		this.animType = animType;
		this.animation = playerAnimations.getAnimation(characterType, animType);
	}

	public TextureRegion getKeyframe() {
		boolean isLooping = (animType == AnimationType.IDLE || animType == AnimationType.RUN);
		return animation.getKeyFrame(currentAnimationTime, isLooping);
	}
}