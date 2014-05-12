package com.me.geonauts.model.entities.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.controller.HeroController;
import com.me.geonauts.model.entities.Entity;

public abstract class Powerup extends Entity {
	
	public Vector2 velocity;
	protected float ROTATION_SPEED = 0.5f;
	
	public String TEXT_FLOAT_VALUE = null;
	public Color TEXT_FLOAT_COLOR = null;
	protected HeroController heroC;
	protected boolean alive = true;
	
	public Powerup(Vector2 pos, Vector2 velocity, Vector2 SIZE, HeroController h) {
		super(pos, SIZE);
		heroC = h;
		this.velocity = velocity;
		
	}
	
	public abstract void doAction () ;
	
	public void update(float delta) {
		// Move healthpack
		position.add(velocity.scl(delta));
		velocity.scl(1f/delta);
		
		// Move bounding box
		this.getBounds().x = this.position.x ;
		this.getBounds().y = this.position.y ;
		
		// Check if hit hero
		if (heroC.getBounds().overlaps(this.getBounds())) {
			doAction();
		}
		
		// Update angle
		angle += ROTATION_SPEED;
		if (angle > 360) {
			angle = 0;
		}
		
		
	}
	
	public abstract TextureRegion getKeyFrame();
	
	public boolean isAlive() {
		return alive;
	}
}
