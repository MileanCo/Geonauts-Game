package com.me.geonauts.model.entities.heroes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Echo extends Hero {
	// Speed of the hero
	public static final float SPEED = 2.8f;	// unit per second
	
	// Rotation stuff
	public static final float ROTATION_SPEED = 0.74f; // angles per second??
	private static final float PITCH = 22f; 
	
	// Other Attributes
	private static int health = 225;
	private static final Vector2 SIZE = new Vector2(62/64f, 52/60f); 
			//new Vector2(1.64f/1.5f, (4/3f)/1.5f); //textureWidth / ppuX
	
	// Textures
	public static Animation anim;
	
	/**
	 * Create a new Hero of type Sage.
	 * @param position
	 */
	public Echo(Vector2 position) {
		super(position, SIZE, ROTATION_SPEED, PITCH, SPEED, health);
	}

	@Override
	public TextureRegion getKeyFrame() {
		return anim.getKeyFrame(stateTime, true);
	}
}
