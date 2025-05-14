package io.example.mortal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.example.mortal.PlayerAnimations.AnimationType;
import io.example.mortal.PlayerAnimations.CharacterType;


/**
 * Represents a player character in the game with animation, movement,
 * and combat capabilities.
 */
public class Player {
	// --- Public State ---
	public Vector2 position;
	public int health;
	public float maxHealth;
	public float range = 365f;
	public int strength = 10;
	public boolean isAnimInHitZone = false;
	public boolean isFinishedDying = false;
	public boolean isDying = false;
	public boolean isPunchDead = true;
	public CharacterType characterType;
	public AnimationType animType = AnimationType.IDLE;
	public Animation<TextureRegion> animation;
	public float currentAnimationTime = 0f;
	public float stateTime = 0f;

	// --- Private Movement/State ---
	private Vector2 speed = new Vector2(500f, 0);
	private float gravity = -40f;
	private float startHitTime = 0.3f;
	private float endHitTime = 0.8f;
	private boolean isMovingLeft = false;
	private boolean wasMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isDropping = false;
	private boolean isJumping = false;
	private boolean isDucking = false;
	private boolean wasDucking = false;
	private boolean isPunching = false;
	private boolean isTakingDamage = false;
	private boolean wasIdle = false;

	// --- Animation & Audio ---
	private PlayerAnimations playerAnimations = new PlayerAnimations();
	private Sound soundPunch, soundHit, soundDeath, soundJump;

	// --- Constructor ---
	/**
	 * Constructs a new player instance with given position, character type, and health.
	 * @param startPosition the initial position of the player
	 * @param characterType the character type used to load animations and sounds
	 * @param health the starting health value of the player
	 */
	public Player(Vector2 startPosition, CharacterType characterType, int health) {
		this.position = startPosition;
		this.characterType = characterType;
		this.health = health;
		this.maxHealth = health;
		this.animation = playerAnimations.getAnimation(characterType, animType);
		loadSounds(characterType);
	}

	// Loads sound files based on the character type
	private void loadSounds(CharacterType characterType) {
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

	// --- Input Control ---
	public void setMovingLeft(boolean value) {
		if (isMovingLocked()) return;
		if (animType != AnimationType.RUN && !isJumping)
			changeAnim(AnimationType.RUN);
		isMovingLeft = value;
	}

	public void setMovingRight(boolean value) {
		if (isMovingLocked()) return;
		if (animType != AnimationType.RUN && !isJumping)
			changeAnim(AnimationType.RUN);
		isMovingRight = value;
	}

	public void drop() {
		isPunching = false;
		isPunchDead = true;
		isDropping = true;
	}

	public void jump() {
		if (isJumping || isTakingDamage) return;
		speed.y = 1500f;
		isJumping = true;
		if (!isPunching)
			changeAnim(AnimationType.JUMP);
		soundJump.play();
	}

	public void duck() {
		if (!isJumping && !isPunching)
			isDucking = true;
	}

	public void punch() {
		if (isPunching) return;
		isPunching = true;
		isPunchDead = false;
		changeAnim(AnimationType.ATTACK);
		soundPunch.play();
	}

	/**
	 * Applies a damage amount to the player and triggers appropriate animations and sounds.
	 * @param amount the damage to apply
	 */
	public void damage(int amount) {
		if (isDying || wasDucking || isTakingDamage) return;

		if (wasIdle) amount /= 2;
		health -= amount;

		if (health <= 0) {
			health = 0;
			isDying = true;
			changeAnim(AnimationType.DEATH);
			soundDeath.play();
		} else {
			isTakingDamage = true;
			isPunching = false;
			changeAnim(AnimationType.HIT);
			soundHit.play();
		}
	}

	// --- Core Update Loop ---
	/**
	 * Updates the player's state, including movement, animation, and interactions.
	 * @param delta the time in seconds since the last update
	 */
	public void update(float delta) {
		currentAnimationTime += delta;

		if (handleDeathState(delta)) return;

		stateTime += delta;

		handleMovement(delta);
		handleJumpPhysics(delta);
		clampPosition();

		if (isPunching) handleActivePunch(delta);

		if (isTakingDamage && animation.isAnimationFinished(currentAnimationTime))
			isTakingDamage = false;

		if (isIdle() && animType != AnimationType.IDLE)
			changeAnim(AnimationType.IDLE);

		updateStateFlags();
	}

	private void handleMovement(float delta) {
		if (isMovingLeft)  position.x -= speed.x * delta;
		if (isMovingRight) position.x += speed.x * delta;
	}

	private void handleJumpPhysics(float delta) {
		speed.y += gravity;
		if (isDropping) {
			speed.y = Math.min(-1800, speed.y);
			isDropping = false;
		}
		position.y += speed.y * delta;
		isJumping = position.y > -240f;
		if (!isJumping){
			speed.y = 0f;
		}
	}

	private void clampPosition() {
		position.x = MathUtils.clamp(position.x, -280f, 910f);
		position.y = MathUtils.clamp(position.y, -240f, 450f);
	}

	private boolean handleDeathState(float delta) {
		if (isDying) {
			if (!isFinishedDying && currentAnimationTime > 0.8f)
				isFinishedDying = true; // death animation has finished
			return true;
		}
		return false;
	}

	private void updateStateFlags() {
		wasIdle = isIdle();
		wasMovingLeft = isMovingLeft;

		if (!(isTakingDamage && isJumping)) {
			isMovingLeft = false;
			isMovingRight = false;
		}

		wasDucking = isDucking;
		isDucking = false;
	}

	private void handleActivePunch(float delta) {
		isAnimInHitZone = currentAnimationTime > startHitTime && currentAnimationTime < endHitTime;
		if (animation.isAnimationFinished(currentAnimationTime)) {
			isPunching = false;
			isPunchDead = true;
		}
	}

	private boolean isIdle() {
		return !(isMovingLeft ^ isMovingRight) &&
			   !isPunching &&
			   !isDying &&
			   !isTakingDamage &&
			   !isJumping;
	}

	private boolean isMovingLocked() {
		return isPunching || isTakingDamage || isDying;
	}

	private void changeAnim(AnimationType animType) {
		this.currentAnimationTime = 0f;
		this.animType = animType;
		this.animation = playerAnimations.getAnimation(characterType, animType);
	}

	// --- Getters ---
	/**
	 * Returns the current keyframe for the player's animation.
	 * @return the current texture region based on animation time and type
	 */
	public TextureRegion getKeyframe() {
		boolean isLooping = (animType == AnimationType.IDLE || animType == AnimationType.RUN);
		return animation.getKeyFrame(currentAnimationTime, isLooping);
	}

	/**
	 * Returns whether the player was previously moving left.
	 * @return true if the player moved left in the last frame
	 */
	public boolean getWasMovingLeft() {
		return wasMovingLeft;
	}
}
