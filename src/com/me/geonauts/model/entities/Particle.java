package com.me.geonauts.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Particle extends Entity {

	public static Vector2 SIZE = new Vector2(6/64f, 6/60f);
	private Vector2 velocity;
	private int LIFE = 135;

	private boolean alive = true;
	
	public Particle(Vector2 pos, Vector2 velocity) {
		super(pos, SIZE);
		this.velocity = velocity;
	}
	
	
	public void update(float delta) {
		position.add(velocity.scl(delta));
		velocity.scl(1/delta);
		
		LIFE --;
		if (LIFE < 0 ) {
			alive = false;
		}
	}


	public abstract TextureRegion getKeyFrame();


	public boolean isAlive() {
		return alive;
	}

}
