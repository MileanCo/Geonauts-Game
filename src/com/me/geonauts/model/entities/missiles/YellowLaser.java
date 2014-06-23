package com.me.geonauts.model.entities.missiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;

public class YellowLaser extends Missile {	
	
	public static Vector2 SIZE = new Vector2((22f/64f), (26/60f));
	public static float SPEED;
	
	public static TextureRegion[] frames;
	
	private Vector2 unitVelocity = new Vector2();
	
	/**
	 * 
	 * @param pos
	 * @param target
	 */
	public YellowLaser(Vector2 pos, AbstractEnemy target, int damage) {
		super(pos, SIZE, target, SPEED, damage);		
	}	
	
	/**
	 * Update the Missile's position, and check for collision with target
	 * @param delta
	 */
	@Override
	public void update(float delta) {		
		// Get difference in x and y from target to position
		float x_diff = target.position.cpy().x - position.x;// + target.SIZE.x/2f;
		float y_diff = target.position.cpy().y - position.y;// - target.SIZE.y/2f;
		
		// If target is dead, Missile is considered 'floating'
		if (! target.isAlive() ) {
			state = State.FLOATING;
		}
		
		// If the target is infront of the missile, update acceleration with unit vec
		if (x_diff > 0) {
			// Target - Position
			Vector2 V = target.getCenterPosition().sub(position);
			// Unit vector = V / magnitude of V
			unitVelocity = V.div(V.len());
			
			//if (x_diff < 1) 
			// else 
				// Set acceleration to the unit vector * speed
			//	acceleration = unitVec.scl(SPEED);
			
			// Update direction based on x_diff
			if (x_diff < 0) DIRECTION = -1;
			else DIRECTION = 1;
			
			// Angle = sin^-1 (opposite / Hypotenuse)
			angle = (float) Math.toDegrees( Math.atan(y_diff / x_diff) );
		}
		// Do this everytime, since after x_diff < 0, it stays constant.
		velocity = unitVelocity.cpy().scl(SPEED);

		
	}

	
	@Override
	public TextureRegion[] getFrames() {
		return frames;
	}
}
