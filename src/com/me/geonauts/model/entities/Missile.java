package com.me.geonauts.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;
import com.me.geonauts.model.entities.enemies.AbstractEnemy.State;

public class Missile extends Entity {
	// State stuff
	public enum State {
		DYING, ALIVE
	}
	
	public State		state = State.ALIVE;
	private float		stateTime = 0;
	

	
	public static Vector2 SIZE = new Vector2((22f/64f), (26/60f));
	private static float SPEED = 12f;
	protected float 	DAMP;
	public Vector2 		acceleration;
	public Vector2 		velocity;
	public Vector2		MAX_VEL;
	private int DIRECTION;
	
	private AbstractEnemy target;
	private int damage;
	
	/** Used to determine if missile needs to be removed */
	private boolean alive = true;
	
	public static TextureRegion[] frames;
	
	/**
	 * 
	 * @param pos
	 * @param target
	 */
	public Missile(Vector2 pos, AbstractEnemy target, int damage) {
		super(pos, SIZE);
		this.target = target;
		this.damage = damage;
		
		// Make new movement vectors
		DAMP = 0.85f;
		//this.SPEED = (float) (rand.nextDouble() * SPEED);
		MAX_VEL = new Vector2(SPEED*2f, SPEED*2f);
		acceleration = new Vector2(SPEED, 0);
		
		// Set initial velocity
		Vector2 V = target.position.cpy().sub(position);
		// Unit vector = V / magnitude of V
		velocity = V.div(V.len());
		
		
	}	
	
	/**
	 * Update the Missile's position, and check for collision with target
	 * @param delta
	 */
	public void update(float delta) {
		// Target - Position
		Vector2 V = target.position.cpy().sub(position);
		// Unit vector = V / magnitude of V
		Vector2 unitVec = V.div(V.len());
		
		// Get difference in x and y from target to position
		float x_diff = target.position.cpy().x - position.x;
		float y_diff = target.position.cpy().y - position.y;
		
		// If the target is infront of the missile, update acceleration with unit vec
		if (x_diff > 0) {
			//if (x_diff < 1) 
				velocity = unitVec.scl(SPEED);
			// else 
				// Set acceleration to the unit vector * speed
			//	acceleration = unitVec.scl(SPEED);
			
			// Update direction based on x_diff
			if (x_diff < 0) DIRECTION = -1;
			else DIRECTION = 1;
			
			// Angle = sin^-1 (opposite / Hypotenuse)
			angle = (float) Math.toDegrees( Math.atan(y_diff / x_diff) );
		}

		
	}
	
	
	public TextureRegion[] getFrames() {
		return frames;
	}
	
	public boolean isAlive() {
		return alive;
	}
	public AbstractEnemy getTarget() {
		return target;
	}
	public int getDamage() {
		return damage;
	}
	public int getDIRECTION() {
		return DIRECTION;
	}

	public float getDAMP() {
		return DAMP;
	}

}
