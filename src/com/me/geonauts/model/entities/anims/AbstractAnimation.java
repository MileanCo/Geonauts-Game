package com.me.geonauts.model.entities.anims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.Entity;

public abstract class AbstractAnimation extends Entity {

	protected float		stateTime = 0;
	
	protected boolean alive = true;
	
	protected boolean coin = false;
	
	public AbstractAnimation(Vector2 pos, Vector2 SIZE) {
		super(pos, SIZE);
	}
	
	
	public void update(float delta) {
		stateTime += delta;
		
		if (getAnimation().isAnimationFinished(stateTime))
			alive = false;
		
	}
	
	
	public float getStateTime() {
		return stateTime;
	}
	
	public abstract Animation getAnimation();
	
	public boolean isAlive() {
		return alive;
	}

	public abstract TextureRegion getKeyFrame();


	public boolean isCoin() {
		return coin;
	}
	
}
