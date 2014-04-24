package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.missiles.EnemyMissile;
import com.me.geonauts.model.entities.missiles.GreenEnemyLaser;

public class Dwain extends AbstractEnemy {
	
	// Rotation stuff
	public float ROTATION_SPEED = 0.3f; // angles per second??
	private float PITCH;
	

	public static final float SPEED = 0.5f;	// unit per second
	public final int DIRECTION = -1;
	private static Vector2 SIZE = new Vector2((74/64f), (71/60f));
	public static int health = 55;
	public static int damage = 15;
	private static int value = 20;
	
	private float lastStateTime = 0;
	private static final float STATE_CHANGE_TIME = 2;
	
	// Textures
	public static TextureRegion[] enemyFrames;
	
	/**
	 * 
	 * @param pos
	 * @param SIZE
	 */
	public Dwain(Vector2 pos) {
		super(pos, SIZE, SPEED, health, damage);
		PITCH = rand.nextInt(25-16) + 16;
		reloadTime = (rand.nextInt(10 - 7) + 7) / 10f;
		//ROTATION_SPEED = ROTATION_SPEED * (float)rand.nextDouble();
	
		//System.out.println(ROTATION_SPEED);

	}


	@Override
	public void update(float delta) {		
		stateTime += delta;
		// Change state from FLYING to FALLING
		//if (stateTime - lastStateTime > STATE_CHANGE_TIME) {
		//	if (state == State.FLYING) state = State.FALLING;
		//	else if (state == State.FALLING) state = State.FLYING;
		//	lastStateTime = stateTime;
		//}
		
		
		// Update angle based State
		if (state == State.FLYING) 
			angle -= ROTATION_SPEED;
		else if ( state == State.FALLING || state == State.DYING)
			angle += ROTATION_SPEED;
	
			
		// Make sure angle isn't too big.
		int r = rand.nextInt(10 - 0) + 0;
		if (angle > (PITCH - r)) { 
			angle = PITCH - r;
			if (state == State.FLYING) state = State.FALLING;
			else if (state == State.FALLING) state = State.FLYING;
		}
		else if (angle < (-PITCH + r)) {
			angle = -PITCH + r;
			if (state == State.FLYING) state = State.FALLING;
			else if (state == State.FALLING) state = State.FLYING;
		}
		
		// Set acceleration
		acceleration.x = SPEED * DIRECTION;
		acceleration.y = (float) (SPEED * angle) * DIRECTION;
		
	}

	@Override
	public TextureRegion[] getFrames() {
		return enemyFrames;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public EnemyMissile newMissile(Vector2 pos, Hero target) {
		return new GreenEnemyLaser(pos, target, damage);
	}
	
}
