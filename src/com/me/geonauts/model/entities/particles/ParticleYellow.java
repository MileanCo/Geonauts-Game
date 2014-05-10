package com.me.geonauts.model.entities.particles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ParticleYellow extends Particle {
	public static TextureRegion frame;

	public ParticleYellow(Vector2 pos, Vector2 velocity) {
		super(pos, velocity);
	}

	public TextureRegion getKeyFrame() {
		return frame;
	}
}
