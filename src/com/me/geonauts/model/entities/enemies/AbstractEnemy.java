/**
 * 
 */
package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.heroes.Hero.State;

/**
 * @author joel
 *
 */
public abstract class AbstractEnemy extends Entity {
	// State stuff
	public enum State {
		DYING, ALIVE
	}
	
	protected State		state = State.ALIVE;
	private float		stateTime = 0;
	
	// Movement attributes
	protected float 	SPEED;
	protected float 	DAMP;
	public Vector2 		acceleration;
	public Vector2 		velocity;
	public Vector2		MAX_VEL;
	
	// Other attributes
	private int health;
	private int damage;
	
	
	
	public AbstractEnemy(Vector2 pos, Vector2 SIZE, int health, int damage) {
		super(pos, SIZE);
		
		MAX_VEL = new Vector2(SPEED, SPEED);
		DAMP = 0.85f;
		this.health = health;
		this.damage = damage;
		
		// Make new movement vectors
		acceleration = new Vector2(SPEED, 0);
		velocity = new Vector2();
		
	}
	
	public abstract void update(float delta);

	/** Getters and Setters */
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

	public float getDAMP() {
		return DAMP;
	}

}
