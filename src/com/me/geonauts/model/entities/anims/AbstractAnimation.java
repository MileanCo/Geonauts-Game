package com.me.geonauts.model.entities.anims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.Entity;

public abstract class AbstractAnimation extends Entity {

	private float		stateTime = 0;
	
	public AbstractAnimation(Vector2 pos, Vector2 SIZE) {
		super(pos, SIZE);
	}
	
	
	public void update(float delta) {
		stateTime += delta;
	}
	
	
	public float getStateTime() {
		return stateTime;
	}
	
	public abstract Animation getAnimation();
	
	public boolean isAnimationFinished() {
		return getAnimation().isAnimationFinished(stateTime);
	}

}
