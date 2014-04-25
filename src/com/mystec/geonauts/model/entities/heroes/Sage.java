package com.mystec.geonauts.model.entities.heroes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Sage extends Hero {
	// Speed of the hero
	public static final float SPEED = 3.0f;	// unit per second
	
	// Rotation stuff
	public static final float ROTATION_SPEED = 1f; // angles per second??
	private static final float PITCH = 20f; 
	
	// Other Attributes
	private static int health = 200;
	private static final Vector2 SIZE = new Vector2(1.64f/1.5f, (4/3f)/1.5f); //textureWidth / ppuX
	
	// Textures
	public static TextureRegion[] heroFrames;
	
	/**
	 * Create a new Hero of type Sage.
	 * @param position
	 */
	public Sage(Vector2 position) {
		super(position, SIZE, ROTATION_SPEED, PITCH, SPEED, health);
	}

	@Override
	public TextureRegion[] getFrames() {
		return heroFrames;
	}	
}
