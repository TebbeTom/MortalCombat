package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
	public float range = 365f;
	public int strength = 10;
	private float startHitTime = 0.3f;
	private float endHitTime = 0.8f;
	public boolean isAnimInHitZone = false;

	private boolean isMovingLeft = false;
	private boolean wasMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isJumping = false;
	private boolean isDucking = false;
	private boolean wasDucking = false;
	private boolean isPunching = false;
	private boolean isTakingDamage = false;
	public boolean isFinishedDying = false;
	public boolean isDying = false;
	public boolean isPunchDead = true;
	private boolean wasIdel = false;

	private PlayerAnimations playerAnimations = new PlayerAnimations();
	public CharacterType characterType;
	public AnimationType animType = AnimationType.IDLE;
	public Animation<TextureRegion> animation;
	public float currentAnimationTime = 0f;
	public float stateTime = 0f;

	private Sound soundPunch;
    private Sound soundHit;
    private Sound soundDeath;
    private Sound soundJump;


    public Player(Vector2 startPosition, CharacterType characterType, int health, float scaleFactor) {
        this.position = startPosition;
		this.characterType = characterType;
        this.health = health;
		this.maxHealth = health;

		this.animation = playerAnimations.getAnimation(characterType, animType);

		switch (characterType) {
            case MARTIAL_HERO:
                soundPunch = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/HeroAttack.mp3"));
                soundHit   = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/HeroHit.mp3"));
                soundDeath = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/HeroDeath.mp3"));
                soundJump  = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/HeroJump.mp3"));
                break;
            case FIGHTER_MAN:
                soundPunch = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/ManAttack.mp3"));
                soundHit   = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/ManHit.mp3"));
                soundDeath = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/ManDeath.mp3"));
                soundJump  = Gdx.audio.newSound(Gdx.files.internal("sounds/playerSounds/ManJump.mp3"));
                break;
        }
    }
	
		public boolean getWasMovingLeft() {
			return wasMovingLeft;
		}

	public void setMovingLeft(boolean value) {
		if (isPunching || isTakingDamage || isDying)
			return;
		if (animType != AnimationType.RUN && !isJumping)
			changeAnim(AnimationType.RUN);
		isMovingLeft = value;
	}

	public void setMovingRight(boolean value) {
		if (isPunching || isTakingDamage || isDying)
			return;
		if (animType != AnimationType.RUN && !isJumping)
			changeAnim(AnimationType.RUN);
		isMovingRight = value;
	}

	public void update(float delta) {
		currentAnimationTime += delta;

		if (isDying && !isFinishedDying) {
			if (currentAnimationTime > 0.6f) {
				isFinishedDying = true;
				// Dead and animation over—could notify GameScreen or pause
				//invoke Death Screen
			}
			return;
		}
		stateTime += delta;

		position.x -= (isMovingLeft ? 1 : 0) * speed.x * delta;
		position.x += (isMovingRight? 1 : 0) * speed.x * delta;
		
		speed.y += gravity;
		position.y += speed.y * delta;
		
		isJumping = position.y > -240f;
        if (!isJumping) speed.y = 0f;

		position.x = MathUtils.clamp(position.x, -240f, 860f);
		position.y = MathUtils.clamp(position.y, -240f, 450f);

		if(isPunching)
			handleActivePunch(delta);

		if (isTakingDamage && animation.isAnimationFinished(currentAnimationTime))
			isTakingDamage = false;
		
		if (isIdle() && animType != AnimationType.IDLE) { // ^ == XOR
			changeAnim(AnimationType.IDLE);
		}

		wasIdel = isIdle();

		wasMovingLeft = isMovingLeft;
		if (!(isTakingDamage && isJumping)) {
			isMovingLeft = false;
			isMovingRight = false;
		}
		wasDucking = isDucking;
		isDucking = false;
	}

    public void jump() {
		if (isJumping || isTakingDamage)
			return;
		speed.y = 900f;
		isJumping = true;
		changeAnim(AnimationType.JUMP);
		soundJump.play();
	}

	public void duck() {
		if(!(isJumping || isPunching))
			isDucking = true;
	}

	private boolean isIdle() {
		if (
			(isMovingLeft ^ isMovingRight) ||
			isPunching ||
			isDying || 
			isTakingDamage ||
			isJumping
		){ return false;}
		return true;
	}

	public void punch() {
		if (isPunching)
			return;
		isPunching = true;
		isPunchDead = false;
		changeAnim(AnimationType.ATTACK);
		soundPunch.play();
	}

	public void damage(int amount) {
        if (isDying || wasDucking || isTakingDamage) return;
		if (wasIdel)
			amount /= 5;
        health -= amount;
        if (health <= 0) {
            health = 0;
            isDying = true;
            changeAnim(AnimationType.DEATH);               // CHANGED: Death Animation
			soundDeath.play();
        } else {
			isTakingDamage = true;
			isPunching = false;
            changeAnim(AnimationType.HIT);                 // CHANGED: Hit Animation
			soundHit.play();
        }
	}

	private void handleActivePunch(float delta) {
		if (currentAnimationTime > startHitTime && currentAnimationTime < endHitTime)
			isAnimInHitZone = true;
		else
			isAnimInHitZone = false;
		if(animation.isAnimationFinished(currentAnimationTime)){
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

	public boolean isDeathAnimationDone() {
    	return animType == AnimationType.DEATH && animation.isAnimationFinished(stateTime);
	}
}