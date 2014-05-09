package com.me.geonauts.model.entities.anims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.heroes.Hero;

public class Coin extends AbstractAnimation {

	private static Vector2 SIZE = new Vector2(1.5f*(32/64f), 1.5f*(32/60f));
	
	public static Animation anim;
	private Hero hero;
	
	public static final int value = 20;
	
	public Coin(Vector2 pos, float scl, Hero h) {
		super(pos, SIZE.cpy().scl(scl));		
		this.hero = h;
		coin = true;
	}


	@Override
	public Animation getAnimation() {
		return anim;
	}
	
	
	@Override
	public void update(float delta) {
		stateTime += delta;
		
		// Check if coin overlaps hero
		if (this.getBounds().overlaps(hero.getBounds()) && isAlive() ) {
			hero.money += value;
			hero.score += value * 5;
			hero.coinsCollected++;
			alive = false;
		}
	}


	@Override
	public TextureRegion getKeyFrame() {
		return anim.getKeyFrame(stateTime, true);
	}
}
