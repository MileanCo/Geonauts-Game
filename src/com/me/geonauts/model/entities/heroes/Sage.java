package com.me.geonauts.model.entities.heroes;

import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.enums.BlockType;
import com.me.geonauts.model.enums.HeroType;

public class Sage extends Hero {
	// Speed of the hero
	public static final float SPEED = 4.0f;	// unit per second
	
	// Rotation stuff
	public static final float ROTATION_SPEED = 0.75f; // angles per second??
	private static final float PITCH = 20f; 
	
	private static final Vector2 SIZE = new Vector2(1.64f, (4/3f)); //textureWidth / ppuX, 
	
	/**
	 * Create a new Hero of type Sage.
	 * @param position
	 */
	public Sage(Vector2 position) {
		super(position, SIZE, HeroType.SAGE, ROTATION_SPEED, PITCH, SPEED);		
	}
	
	
	

	
	
}
