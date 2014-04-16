/**
 * 
 */
package com.me.geonauts.model.entities.heroes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.Chunk;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.Target;
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
	public State		state = State.FALLING;
	private float		stateTime = 0;
	private long		timeDied = 0;
	
	// Movement attributes
	private float 		SPEED_INCREMENT = 1.0003f;
	protected float 	SPEED;
	protected float 	DAMP;
	public Vector2 		acceleration;
	public Vector2 		velocity;
	public Vector2		MAX_VEL;

	// Rotation stuff
	public float 		ROTATION_SPEED;
	protected float 	PITCH;

	// Other attributes
	public int health;
	public boolean grounded;
	
	protected List<Target> targets;
	private int MAX_TARGETS = 2;
	protected float reloadTime;

	
	/**
	 * Creates a new Hero that is an Entity.
	 * @param position
	 * @param SIZE
	 */
	public Hero(Vector2 position, Vector2 SIZE, float ROTATION_SPEED, float PITCH, float SPEED, int health) {
		super(position, SIZE);
		this.health = health;
		targets = new ArrayList<Target>();
		
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
		
		if (health <= 0) state = State.DYING;
		
		
		// If dying, stop movement and record time.
		if (state == State.DYING) {
			//velocity.x /= 2;
			//velocity.y = 0;
			//acceleration.y = 0;
			if (timeDied == 0) {
				timeDied = System.currentTimeMillis();
			}
		} else {
			timeDied = 0;
		}
		
		
		// Make sure hero doesn't go above screen.
		if (position.y > Chunk.HEIGHT - SIZE.y) {
			state = State.FALLING;
			angle -= ROTATION_SPEED;
		}
		
		// If the Hero isn't on the ground, UPDATE ANGLE AND ACCELERATION
		if (! grounded) {
			// Update angle based State
			// Make hero go straight first couple meters
			if (position.x > WorldRenderer.WIDTH/2) { 
				if (state == State.FLYING) 
					angle += ROTATION_SPEED;
				else if ( state == State.FALLING || state == State.DYING)
					angle -= ROTATION_SPEED / 3;
			}
				
			// Make sure angle isn't too big.
			if (angle > PITCH) angle = PITCH;
			else if (angle < -PITCH + 7) angle = -PITCH + 7;
			
			// Set acceleration
			acceleration.x = SPEED;
			acceleration.y = (float) (SPEED * angle);
		}
		
		// Increase speed
		MAX_VEL.x *= SPEED_INCREMENT;
		MAX_VEL.y *= SPEED_INCREMENT;
		
		// System.out.println(velocity.y);
		//System.out.println(ROTATION_SPEED * delta);
		
		//System.out.println(angle);
	}
	
	public void addTarget(Target e) {
		if (targets.size() < MAX_TARGETS) {
			targets.add(e);
		} else {
			targets.remove(0);
			targets.add(0, e);
		}
	}
	public void removeTarget(Target e) {
		//if (targets.contains(e) {
			targets.remove(e);
		//} else {
			
		//}
	}
	public List<Target> getTargets() {
		return targets;
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
	public long getTimeDied() {
		return timeDied;
	}
	public float getReloadTime() {
		return reloadTime;
	}
	
	
	// All Heros must implement the getFrames() method to return the proper images 
	public abstract TextureRegion[] getFrames ();
	
}
	