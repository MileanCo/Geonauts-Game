package com.me.geonauts.model.entities.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.controller.HeroController;

public class ShieldPack extends Powerup {
	public static Vector2 SIZE = new Vector2((50f/64f), (48f/60f));
	
	public static TextureRegion frame;
	
	private int SHIELD = 150;

	
	public ShieldPack(Vector2 pos, Vector2 velocity, HeroController hc) {
		super(pos, velocity, SIZE, hc);
		TEXT_FLOAT_VALUE = "+" + SHIELD + " Shield";
		TEXT_FLOAT_COLOR = new Color(0.2f, 0.2f, 1, 1);
	}

	@Override
	public void doAction() {
		heroC.activateShield(SHIELD);
		alive = false;
	}

	@Override
	public TextureRegion getKeyFrame() {
		return frame;
	}
}
