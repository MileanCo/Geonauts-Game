package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Turret extends AbstractEnemy {

	public static final float SPEED = 0.0f;	// unit per second
	private static Vector2 SIZE = new Vector2(1, 1);
	private static int health = 50;
	private static int damage = 5;
	private static int value = 10;
	
	// Textures
	public static TextureRegion[] enemyFrames;
	
	public Turret(Vector2 pos) {
		super(pos, SIZE, SPEED, health, damage);


	}
	
	

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public TextureRegion[] getFrames() {
		return enemyFrames;
	}



	@Override
	public int getValue() {
		return value;
	}

}
