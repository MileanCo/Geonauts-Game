package com.me.geonauts.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ParallaxLayer {
	public TextureRegion region;
	public Vector2 parallaxRatio;
	public Vector2 startPosition;
	public Vector2 padding;
	public float scale;
	public boolean fill;

	/**
	 * Create a new layer in the background.
	 * 
	 * @param region
	 * @param parallaxRatio
	 * @param padding
	 */
	public ParallaxLayer(TextureRegion region, Vector2 parallaxRatio, Vector2 padding, boolean fill) {
		this(region, parallaxRatio, new Vector2(0, 0), padding, fill);
		
	}

	/**
	 * @param region  the TextureRegion to draw , this can be any width/height
	 * @param parallaxRatio the relative speed of x,y
	 *            {@link ParallaxBackground#ParallaxBackground(ParallaxLayer[], float, float, Vector2)}
	 * @param startPosition the init position of x,y
	 * @param padding the padding of the region at x,y
	 */
	public ParallaxLayer(TextureRegion region, Vector2 parallaxRatio, Vector2 startPosition, Vector2 padding, boolean fill) {
		this.region = region;
		this.parallaxRatio = parallaxRatio;
		this.startPosition = startPosition;
		this.padding = padding;
		this.fill = fill;
	}
}