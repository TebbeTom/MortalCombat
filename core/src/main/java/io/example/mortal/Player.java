package io.example.mortal;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public Vector2 position;
	public Vector2 speed = new Vector2(100f, 200f);;
    public float health;
	public float maxHealth;
	public boolean isMovingLeft = false;
	public boolean isMovingRight = false;
	public float gravity = -20f;
	public boolean isJumping = false;
	public boolean isDucking = false;
	public float width = 600f;


    public Player(Vector2 startPosition, float health) {
        this.position = startPosition;
        this.health = health;
		this.maxHealth = health;
    }


	public void update(float delta) {
		position.x += (isMovingLeft ? 1 : 0) * speed.x * delta;
		position.x -= (isMovingRight? 1 : 0) * speed.x * delta;
		position.x = MathUtils.clamp(position.x, halfWidth, screenWidth - halfWidth);

		speed.y += gravity;
		position.y += (isMovingLeft ? 1 : 0) * speed.y * delta;
		position.y -= (isMovingRight? 1 : 0) * speed.y * delta;
		isJumping = position.y > 0;
	}

    public void jump() {
		if (isJumping)
			return;
		
	}

	public void duck() {
		isDucking = true;
	}

	public void punch() {

	}

	public void kick() {

	}
}