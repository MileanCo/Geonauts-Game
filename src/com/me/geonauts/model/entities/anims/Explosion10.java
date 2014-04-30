package com.me.geonauts.model.entities.anims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Explosion10 extends AbstractAnimation {

	private static Vector2 SIZE = new Vector2((128/64f), (128/60f));
	
	public static Animation anim;
	
	public Explosion10(Vector2 pos, float scl) {
		super(pos, SIZE.cpy().scl(scl));
		
	}


	@Override
	public Animation getAnimation() {
		return anim;
	}
	
	
	

}
