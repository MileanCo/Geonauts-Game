/**
 * 
 */
package com.mystec.geonauts.view;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mystec.geonauts.model.ParallaxLayer;
import com.mystec.geonauts.model.World;

public class ParallaxBackground {

	private ParallaxLayer[] layers;
	private Camera camera;
	private SpriteBatch batch;
	private WorldRenderer worldRenderer;
	private float speed;

	public ParallaxLayer[] getLayers() {
		return layers;
	}

	/**
	 * @param layers - The background layers
	 * @param width - The screenWith
	 * @param height - The screenHeight
	 * @param speed - A Vector2 attribute to point out the x and y speed
	 */
	public ParallaxBackground(ParallaxLayer[] layers, float width, float height, float speed, WorldRenderer wr) {
		this.layers = layers;
		camera = new OrthographicCamera(width, height);
		batch = new SpriteBatch();
		worldRenderer = wr;
		this.speed = speed;	
	}

	/**
	 * Draw the parallax layer in the background.
	 * 
	 * @param delta
	 */
	public void render(float delta) {
		// Get velocity of hero. If hero stops, scrolling will stop.
		Vector2 velocity = worldRenderer.getWorld().getHero().velocity.cpy().scl(speed * delta);
		// Move the camera by the veloctiy of the hero. Convert from UNITS/sec to PIXELS/sec
		camera.position.add(velocity.x * worldRenderer.getPPUX(), 
				velocity.y * worldRenderer.getPPUY(), 0);
		
		// Render all the layers in the background
		for (ParallaxLayer layer : layers) {
			batch.setProjectionMatrix(camera.projection);
			batch.begin();
			// Determine current X of this layer. If currentX is far enough to the left, it will 
			// render another background img @ end of current img.
			float currentX = -camera.position.x * layer.parallaxRatio.x % (layer.region.getRegionWidth() + layer.padding.x);

			if (velocity.x < 0)
				currentX += -(layer.region.getRegionWidth() + layer.padding.x);
			do {
				float currentY = 0;//-camera.position.y * layer.parallaxRatio.y % (layer.region.getRegionHeight() + layer.padding.y);

				layer.update(delta);
				
				//if (velocity.y < 0)
				//	currentY += -(layer.region.getRegionHeight() + layer.padding.y);
				if (layer.rotate) {
					batch.draw(layer.region, // region
								-this.camera.viewportWidth / 2 + currentX + layer.startPosition.x, // x
								-this.camera.viewportHeight / 2 + currentY + layer.startPosition.y, // y
								layer.region.getRegionWidth() / 2, //originX
								layer.region.getRegionHeight() / 2, //originX
								layer.region.getRegionWidth(), // width
								layer.region.getRegionHeight(), // height
								layer.scale, //scaleX
								layer.scale, //scaleY
								layer.angle); //rotation
				} else {
					batch.draw(layer.region, // region
							-this.camera.viewportWidth / 2 + currentX + layer.startPosition.x, // x
							-this.camera.viewportHeight / 2 + currentY + layer.startPosition.y, // y
							0, //layer.region.getRegionWidth() / 2, //originX
							0, //layer.region.getRegionHeight() / 2, //originX
							layer.region.getRegionWidth(), // width
							layer.region.getRegionHeight(), // height
							layer.scale, //scaleX
							layer.scale, //scaleY
							layer.angle); //rotation
				}
				
			
				//System.out.println(layer.region.getRegionHeight() * layer.scale);
				currentX += (layer.region.getRegionWidth() + layer.padding.x);

				
			} while (currentX < camera.viewportWidth);
			batch.end();
		}
		camera.update();
	}

	/**
	 * Set the size of the screen, camera, and scale layer images.
	 * @param screenW
	 * @param screenH
	 */
	public void setSize(float screenW, float screenH) {
		camera = new OrthographicCamera(screenW, screenH);
		
		// Change the scale of each image based on new screen.
		// TO-DO: there needs to be higher res images for 
		// bigger screens. Scaling it doesnt make use of the bigger resolution.
		for (ParallaxLayer layer : layers) {
			//System.out.println(screenH + " " + layer.region.getRegionHeight());
			if (layer.fill)
				if (layer.region.getRegionHeight() < screenH) 
					layer.scale = screenH / layer.region.getRegionHeight();
				else 
					layer.scale = 1;
			else
				layer.scale = 1;
		}
	}
}
