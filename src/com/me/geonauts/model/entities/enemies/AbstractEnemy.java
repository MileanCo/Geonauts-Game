/**
 * 
 */
package com.me.geonauts.model.entities.enemies;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.Target;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.missiles.EnemyMissile;

/**
 * @author joel
 *
 */
public abstract class AbstractEnemy extends Entity {
	protected static Random rand = new Random();
	// State stuff
	public enum State {
		DYING, FLYING, FALLING
	}
	
	public State		state = State.FALLING;
	protected float		stateTime = 0;
	
	// Movement attributes
	public float 	SPEED;
	protected float 	DAMP;
	public Vector2 		acceleration;
	public Vector2 		velocity;
	public Vector2		MAX_VEL;
	public Vector2 		direction = new Vector2(-1, -1);
	
	// Other attributes
	protected int value;
	public int health;
	protected int damage;
	protected float reloadTime = -1; // -1 means they can't shoot. Overwrite to make it shoot.
	public boolean alive = true;	
	
	// Targetting stuff
	protected Target target;
	
	public AbstractEnemy(Vector2 pos, Vector2 SIZE, float SPEED, int health, int damage) {
		super(pos, SIZE);
		
		
		// Make new movement vectors
		DAMP = 0.85f;
		this.SPEED = SPEED;
		MAX_VEL = new Vector2(SPEED, SPEED * 2f);
		acceleration = new Vector2(SPEED, 0);
		velocity = new Vector2();
		
		// Other attributes
		this.health = health;
		this.damage = damage;
	}
	
	public abstract void update(float delta);
	
	public abstract EnemyMissile newMissile(Vector2 pos, Hero target);

	/** Getters and Setters */
	public float getStateTime() {
		return stateTime;
	}
	public int getDamage() {
		return damage;
	}
	public float getDAMP() {
		return DAMP;
	}
	public float getReloadTime() {
		return reloadTime;
	}
	// All Enemies must implement the getFrames() method to return the proper images 
	public abstract TextureRegion[] getFrames ();
	
	public int getValue() {
		return value;
	}
	public int getScoreValue() {
		return value * 10;
	}

	public boolean isAlive() {
		return alive;
	}
}
