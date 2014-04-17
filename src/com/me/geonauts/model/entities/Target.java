package com.me.geonauts.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;

public class Target extends Entity {
	
	private float		stateTime = 0;
	private static final Vector2 SIZE = new Vector2((40/64f), (40/60f));
	public static final float ROTATION_SPEED = 3f; // angles per second??
	
	// Textures
	public static TextureRegion[] frames;
	
	private AbstractEnemy enemy;
	
	public Target(AbstractEnemy enemy) {
		super(enemy.position, SIZE);
		
		this.enemy = enemy;
	}
	
	public void update(float delta) {
		stateTime += delta;
		position = enemy.position;
		
		// Constantly rotate the entity
		angle += ROTATION_SPEED;
		if (angle > 360) angle = 0;
		
	}

	public TextureRegion[] getFrames() {
		return frames;
	}

	public AbstractEnemy getEnemy() {
		return enemy;
	}
	
}
