package com.mystec.geonauts.model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mystec.geonauts.model.entities.heroes.Hero;
import com.mystec.geonauts.model.entities.missiles.EnemyMissile;

public class BlueMob extends AbstractEnemy {

	
	// Rotation stuff
	public float ROTATION_SPEED = 0.4f; // angles per second??
	private float PITCH;
	

	public static final float SPEED = 2f;	// unit per second
	public final int DIRECTION = -1;
	private static Vector2 SIZE = new Vector2((39/64f), (39/60f));
	public static int health = 35;
	public static int damage = 15;
	private static int value = 15;
	
	private float lastStateTime = 0;
	private static final float STATE_CHANGE_TIME = 1;
	
	// Textures
	public static TextureRegion[] enemyFrames;
	
	// 
	protected Hero hero;
	
	/**
	 * 
	 * @param pos
	 * @param SIZE
	 */
	public BlueMob(Vector2 pos, Hero hero) {
		super(pos, SIZE, SPEED, health, damage);
		PITCH = rand.nextInt(32-15) + 15;
		ROTATION_SPEED = ROTATION_SPEED * (float)rand.nextDouble();
		
		//velocity = new Vector2(SPEED, 0);
		this.hero = hero;
		//System.out.println(ROTATION_SPEED);

	}


	@Override
	public void update(float delta) {		
		stateTime += delta;

		// Update state based on hero position
		// Hero is above mob
		if (position.y > hero.position.y ) {
			state = State.FALLING;
		} else if (position.y < hero.position.y ) {
			state = State.FLYING;
		}
	
		// Update angle based State
		if (state == State.FLYING) 
			angle -= ROTATION_SPEED;
		else if ( state == State.FALLING || state == State.DYING)
			angle += ROTATION_SPEED;
			
		// Make sure angle isn't too big.
		if (angle > PITCH) angle = PITCH;
		else if (angle < -PITCH) angle = -PITCH;
		
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
		// TODO Auto-generated method stub
		return null;
	}

	
}
