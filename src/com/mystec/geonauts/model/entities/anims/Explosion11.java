package com.mystec.geonauts.model.entities.anims;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Explosion11 extends AbstractAnimation {

	private static Vector2 SIZE = new Vector2((120/64f), (120/60f));
	
	public static Animation anim;
	
	public Explosion11(Vector2 pos, float scl) {
		super(pos, SIZE.cpy().scl(scl));
		
	}


	@Override
	public Animation getAnimation() {
		return anim;
	}
	
	
	

}
