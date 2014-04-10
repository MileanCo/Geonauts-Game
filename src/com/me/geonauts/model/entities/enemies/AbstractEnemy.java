/**
 * 
 */
package com.me.geonauts.model.entities.enemies;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.Entity;

/**
 * @author joel
 *
 */
public abstract class AbstractEnemy extends Entity {
	private Random rand = new Random();
	// State stuff
	public enum State {
		DYING, ALIVE
	}
	
	public State		state = State.ALIVE;
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
	
	
	
	public AbstractEnemy(Vector2 pos, Vector2 SIZE, float SPEED, int health, int damage) {
		super(pos, SIZE);		
		
		// Make new movement vectors
		DAMP = 0.85f;
		this.SPEED = (float) (rand.nextDouble() * SPEED);
		MAX_VEL = new Vector2(SPEED*1.5f, SPEED);
		acceleration = new Vector2(SPEED, 0);
		velocity = new Vector2();
		
		// Other attributes
		this.health = health;
		this.damage = damage;
	}
	
	public abstract void update(float delta);

	/** Getters and Setters */
	public float getStateTime() {
		return stateTime;
	}

	public float getDAMP() {
		return DAMP;
	}

	// All Enemies must implement the getFrames() method to return the proper images 
	public abstract TextureRegion[] getFrames ();
}
