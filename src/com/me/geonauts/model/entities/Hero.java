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
	
	// Rotation stuff
	public static final float ROTATION_SPEED = 12f; // angles per second
	private static final float MAX_ANGLE = 30f;
	private float angle = 0f;
	
	private Vector2 	acceleration;
	private Vector2 	velocity;
	private Rectangle 	bounds = new Rectangle();
	
	private State		state = State.FALLING;
	private boolean		facingLeft = true;
	private float		stateTime = 0;

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
		
		// Update angle based on vertical accel
		if (acceleration.y > 0) { 
			// Hero going UP
			angle += 1.25 * ROTATION_SPEED * delta;
		} else {
			// Hero going DOWN
			angle -= ROTATION_SPEED * delta;
		}
		//System.out.println(ROTATION_SPEED * delta);
		
		// Make sure angle isn't too big.
		if (angle > MAX_ANGLE) angle = MAX_ANGLE;
		else if (angle < -MAX_ANGLE) angle = -MAX_ANGLE;
		
		System.out.println(angle);
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
	

	public float getAngle() {
		return angle;
	}
	
	
}
	