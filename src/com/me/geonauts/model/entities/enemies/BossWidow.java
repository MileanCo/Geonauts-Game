package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.missiles.EnemyMissile;
import com.me.geonauts.model.entities.missiles.PurpleEnemyLaser;

public class BossWidow extends AbstractEnemy {

	// Rotation stuff
	public float ROTATION_SPEED = 1.25f; // angles per second??	

	public static Vector2 SIZE = new Vector2((300/64f), (300/60f));
	public static int health;
	public static int damage;
	
	private float lastStateTime = 0;
	private static final float STATE_CHANGE_TIME = 2;
	
	// Textures
	public static TextureRegion[] enemyFrames;
	
	public BossWidow(Vector2 pos) {
		super(pos, SIZE, 1.5f, health, damage);
		reloadTime = (rand.nextInt(20 - 10) + 10) / 10f; // seconds
		value = 1000;
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
		
		
		angle += ROTATION_SPEED;
		if (angle > 360) angle = 0;		
		
		// Set acceleration
		acceleration.x = SPEED * direction.x;
		acceleration.y = SPEED * direction.y * 2f;
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
		return new PurpleEnemyLaser(pos, target, damage);
	}
}