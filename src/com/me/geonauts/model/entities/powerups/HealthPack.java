package com.me.geonauts.model.entities.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.heroes.Hero;

public class HealthPack extends Powerup {
	public static Vector2 SIZE = new Vector2((50f/64f), (48f/60f));
	
	public static TextureRegion frame;
	
	private int HEALTH = 200;

	
	public HealthPack(Vector2 pos, Vector2 velocity, Hero h) {
		super(pos, velocity, SIZE, h);
		TEXT_FLOAT_VALUE = HEALTH;
		TEXT_FLOAT_COLOR = new Color(1, 0, 0, 1);
	}

	@Override
	public void doAction() {
		hero.health += HEALTH;
		alive = false;
	}

	@Override
	public TextureRegion getKeyFrame() {
		return frame;
	}
}
