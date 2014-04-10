package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Dwain extends AbstractEnemy {


	public static final float SPEED = 1.5f;	// unit per second
	public final int DIRECTION = -1;
	private static Vector2 SIZE = new Vector2((74/64), (71/60));
	private static int health = 50;
	private static int damage = 10;
	
	// Textures
	public static TextureRegion[] enemyFrames;
	
	/**
	 * 
	 * @param pos
	 * @param SIZE
	 */
	public Dwain(Vector2 pos) {
		super(pos, SIZE, SPEED, health, damage);


	}


	@Override
	public void update(float delta) {		
		acceleration.x = SPEED * DIRECTION;
		
	}

	@Override
	public TextureRegion[] getFrames() {
		return enemyFrames;
	}

	
}
