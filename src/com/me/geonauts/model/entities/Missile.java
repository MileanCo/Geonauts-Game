package com.me.geonauts.model.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;

public class Missile extends Entity {

	private static float SPEED = 12f;
	public static Vector2 SIZE = new Vector2((22f/64f), (26/60f));
	
	public static TextureRegion[] frames;
	
	private AbstractEnemy target;
	private int damage;
	
	/** Used to determine if missile needs to be removed */
	private boolean alive = true;
	
	/**
	 * 
	 * @param pos
	 * @param target
	 */
	public Missile(Vector2 pos, AbstractEnemy target, int damage) {
		super(pos, SIZE);
		this.target = target;
		this.damage = damage;
		
		
	}	
	
	/**
	 * Update the Missile's position, and check for collision with target
	 * @param delta
	 */
	public void update(float delta) {
		// Target - Position
		Vector2 V = target.position.cpy().sub(position);
		// Unit vector = V / magnitude of V
		Vector2 unitVec = V.div(V.len());
		
		// Update missile position
		position.add(unitVec.scl(SPEED * delta));
		bounds.x = position.x;
		bounds.y = position.y;
		
		
		// Check if missile collides with target
		if (collision()) {
			target.health -= damage;	
			alive = false;
		}
	}
	
	private boolean collision() {
		// Get the Missile Rectangle 
		Rectangle missileRect = rectPool.obtain();
		missileRect.set(this.getBounds().x, this.getBounds().y,
				this.getBounds().width, this.getBounds().height);
		
		// Get the enemy rectangle
		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle enemyRect = rectPool.obtain();
		// set the rectangle to enemy's bounding box
		enemyRect.set(target.getBounds().x, target.getBounds().y,
				target.getBounds().width, target.getBounds().height);
		
		// Check for collision
		if (missileRect.overlaps(enemyRect)) return true;
		else return false;
	}
	
	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	public TextureRegion[] getFrames() {
		return frames;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	/**
	public void draw (SpriteBatch sb) {
		System.out.println("Asdf");
		//WorldRenderer
		sb.draw(frames[0], position.x * 64, position.y * 60,
				SIZE.x * 64 / 2, SIZE.y * 60 / 2);//WorldRenderer.drawEntityTween(sb, this);
	}
	*/

}
