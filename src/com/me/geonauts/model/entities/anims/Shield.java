package com.me.geonauts.model.entities.anims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.controller.HeroController;
import com.me.geonauts.model.entities.heroes.Hero;

public class Shield extends AbstractAnimation {

	private static Vector2 SIZE = new Vector2((75/64f), (75/60f));
	
	public static Animation anim;
	private Hero hero;
	private double SHIELD_TIME = 10; // seconds
	
	
	public Shield(Vector2 pos, float scl, Hero h) {
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
		SHIELD_TIME -= delta;
		
		// Deactivate shield if time is out, hero's shield is below 0, or hero is grounded
		if (SHIELD_TIME <= 0  || hero.shield <= 0 || hero.grounded) {
			alive = false;
			hero.deactivateShield();
		}
		
		// Center position
		position.x = hero.position.x - SIZE.x/2f + hero.SIZE.x /2f;
		position.y = hero.position.y - SIZE.y/2f + hero.SIZE.y /2f;
		
		
	}


	@Override
	public TextureRegion getKeyFrame() {
		return anim.getKeyFrame(stateTime, true);
	}
}
