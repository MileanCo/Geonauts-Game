package com.me.geonauts.model.entities.enemies;

import com.badlogic.gdx.math.Vector2;

public class Dwain extends AbstractEnemy {

	private Vector2 SIZE = new Vector2((74/64), (71/60));
	private static int health = 50;
	private static int damage = 10;
	
	/**
	 * 
	 * @param pos
	 * @param SIZE
	 */
	public Dwain(Vector2 pos, Vector2 SIZE) {
		super(pos, SIZE, health, damage);


	}


	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
	
}
