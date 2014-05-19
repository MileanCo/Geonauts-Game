package com.me.geonauts.model.entities.missiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.missiles.Missile.State;

public class PurpleEnemyLaser extends EnemyMissile {	
	
	public static Vector2 SIZE = new Vector2((70f/64f), (39/60f));
	private static float SPEED = 9f;

	public static TextureRegion[] frames;
	
	/**
	 * 
	 * @param pos
	 * @param target
	 */
	public PurpleEnemyLaser(Vector2 pos, Hero target, int damage) {
		super(pos, SIZE,  target, SPEED, damage);
	}
	
	
	/**
	 * Update the Missile's position, and check for collision with target
	 * @param delta
	 */
	@Override
	public void update(float delta) {
	}
	
	@Override
	public TextureRegion[] getFrames() {
		return frames;
	}
}
