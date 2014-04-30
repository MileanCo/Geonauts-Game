package com.me.geonauts.model.entities.anims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Explosion06 extends AbstractAnimation {

	private static Vector2 SIZE = new Vector2((64/64f), (64/60f));
	
	public static Animation anim;
	
	public Explosion06(Vector2 pos, float scl) {
		super(pos, SIZE.cpy().scl(scl));
		
	}


	@Override
	public Animation getAnimation() {
		return anim;
	}
	
	
	

}
