package com.mystec.geonauts.model.entities.missiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mystec.geonauts.model.entities.Entity;
import com.mystec.geonauts.model.entities.heroes.Hero;

public abstract class EnemyMissile extends Entity {
	// State stuff
	public enum State {
		DYING, ALIVE
	}
	
	public State		state = State.ALIVE;
	protected float		stateTime = 0;
	
	/** Movement attributes */
	protected float 	DAMP = 0.85f;
	public 	Vector2 	acceleration;
	public	Vector2 	velocity;
	public 	Vector2		MAX_VEL;
	protected int 		DIRECTION;
	
	protected Hero 		target;
	protected int damage;
	
	/** Used to determine if missile needs to be removed */
	protected boolean alive = true;
		
	
	
	
	/**
	 * 
	 * @param pos
	 * @param target
	 */
	public EnemyMissile(Vector2 pos, Vector2 SIZE, Hero target, float SPEED, int damage) {
		super(pos, SIZE);
		this.target = target;
		this.damage = damage;
		
		// Make new movement vectors
		//this.SPEED = (float) (rand.nextDouble() * SPEED);
		MAX_VEL = new Vector2(SPEED*2f, SPEED*2f);
		acceleration = new Vector2(SPEED, 0);
		
		
		// Set initial velocity
		Vector2 V = target.position.cpy().sub(position);
		// Unit vector = V / magnitude of V
		Vector2 unitVec = V.div(V.len());	
		velocity = unitVec.scl(SPEED);
		
		//acceleration = unitVec.scl(SPEED);
		
		// Get difference in x and y from target to position
		float x_diff = position.x - target.position.x;
		float y_diff = position.y - target.position.y;
		angle = (float) Math.toDegrees( Math.atan(y_diff / x_diff) );
	}	
	
	/**
	 * Update the Missile's position, and check for collision with target
	 * @param delta
	 */
	public abstract void update(float delta); 
	
	
	public abstract TextureRegion[] getFrames(); 
	
	
	public boolean isAlive() {
		return alive;
	}
	public Hero getTarget() {
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
