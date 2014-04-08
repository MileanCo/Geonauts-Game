/**
 * 
 */
package com.me.geonauts.model.entities.heroes;

import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.Chunk;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.enums.HeroType;
import com.me.geonauts.view.WorldRenderer;

/**
 * @author joel
 *
 */
public abstract class Hero extends Entity {
	// State stuff
	public enum State {
		FLYING, FALLING, DYING
	}	
	protected State		state = State.FALLING;
	private float		stateTime = 0;
	private HeroType 	type;
	private long		timeDied = 0;
	
	// Movement attributes
	protected float 	SPEED;
	protected float 	DAMP;
	public Vector2 		acceleration;
	public Vector2 		velocity;
	public Vector2		MAX_VEL;

	
	// Rotation stuff
	public float 		ROTATION_SPEED;
	protected float 	PITCH;

	/**
	 * Creates a new Hero that is an Entity.
	 * @param position
	 * @param SIZE
	 */
	public Hero(Vector2 position, Vector2 SIZE, HeroType type, float ROTATION_SPEED, float PITCH, float SPEED ) {
		super(position, SIZE);
		this.type = type;
		
		// Set movement constants
		this.ROTATION_SPEED = ROTATION_SPEED;
		this.PITCH = PITCH;	
		this.SPEED = SPEED;
		MAX_VEL = new Vector2(SPEED, SPEED);
		DAMP = 0.85f;
		
		// Make new movement vectors
		acceleration = new Vector2(SPEED, 0);
		velocity = new Vector2();
		
		
	}

	/**
	 * Update method to change hero's state.
	 * @param delta
	 */
	public void update(float delta) {
		// Add delta to the stateTime, used by animations.
		stateTime += delta;
		
		// Make sure hero doesn't go above screen.
		if (position.y > Chunk.HEIGHT - SIZE.y) {
			state = State.FALLING;
			angle -= ROTATION_SPEED;
		}
		//System.out.println(position.toString());
		
		// Update angle based State
		if (state == State.FLYING) {
			angle += ROTATION_SPEED;
		} else if ( state == State.FALLING) {
			angle -= ROTATION_SPEED / 3;
		}
		
		// Make sure angle isn't too big.
		if (angle > PITCH) angle = PITCH;
		else if (angle < -PITCH + 7) angle = -PITCH + 7;
		
		// Set acceleration
		acceleration.x = SPEED;
		acceleration.y = (float) (SPEED * angle);
		
		// If dying, stop movement and record time.
		if (state == State.DYING) {
			velocity.x = 0;
			velocity.y = 0;
			acceleration.y = 0;
			if (timeDied == 0) {
				timeDied = System.currentTimeMillis();
			}
		} else {
			timeDied = 0;
		}

		
		// System.out.println(velocity.y);
		//System.out.println(ROTATION_SPEED * delta);
		
		//System.out.println(angle);
	}
	
	public Vector2 getAcceleration() {
		return acceleration;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public State getState() {
		return state;
	}
	
	public void setState(State newState) {
		this.state = newState;
	}

	public float getStateTime() {
		return stateTime;
	}
	public float getCamOffsetPosX() {
		return position.x - WorldRenderer.CAM_OFFSET + 1;
	}

	public float getDAMP() {
		return DAMP;
	}

	public Vector2 getMAX_VEL() {
		return MAX_VEL;
	}
	
	public HeroType getType() {
		return type;
	}
	public long getTimeDied() {
		return timeDied;
	}
	
}
	