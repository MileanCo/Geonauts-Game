package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.math.Vector2;

public class Turret extends AbstractEnemy {

	private Vector2 SIZE = new Vector2(1, 1);
	private static int health = 50;
	private static int damage = 5;
	
	public Turret(Vector2 pos, Vector2 SIZE) {
		super(pos, SIZE, health, damage);


	}
	
	

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}
