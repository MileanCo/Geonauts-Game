/**
 * 
 */
package com.me.geonauts.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.view.WorldRenderer;

/**
 * @author joel
 *
 */
public class Hero extends Entity {

	public enum State {
		FLYING, FALLING, DYING
	}
	
	public static final float SPEED = 3f;	// unit per second
	public static final float SIZE = 1f; // whole unit
	
	Vector2 	acceleration;
	Vector2 	velocity;
	Rectangle 	bounds = new Rectangle();
	
	State		state = State.FALLING;
	boolean		facingLeft = true;
	float		stateTime = 0;

	public Hero(Vector2 position) {
		super(position, SIZE);
		acceleration = new Vector2(SPEED, 0);
		velocity = new Vector2();
		
	}

	/**
	 * Update method to change hero's state.
	 * @param delta
	 */
	public void update(float delta) {
		// bounds.x = position.x;
		// bounds.y = position.y;
		// position.add(velocity.tmp().mul(delta)); 
		stateTime += delta;
		//System.out.println(acceleration.x);
	}
	
	
	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
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
	

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
}
	