package com.me.geonauts.controller;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.anims.ExplosionHit;
import com.me.geonauts.model.entities.missiles.Missile;
import com.me.geonauts.view.WorldRenderer;

public class MissileController {

	// Collidable blocks.
	private Array<Block> collidable = new Array<Block>();
	
	// Model objects
	private World world;
	private Missile missile;
	
	/**
	 * Constructor to make the Controller for Missile
	 * @param world
	 */
	public MissileController(World world, Missile m) {
		this.world = world;
		this.missile = m;
	}
	
	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	
	/**
	 * Update the Missile's position and check collisions
	 * @param delta
	 */
	public void update(float delta) {
		// Check if missile is off screen
		if (missile.position.x - world.getHero().getCamOffsetPosX() > WorldRenderer.CAMERA_WIDTH ||
				missile.position.x - world.getHero().getCamOffsetPosX() < 0 ||
				missile.position.y < 0 || 
				missile.position.y > WorldRenderer.CAMERA_HEIGHT ) {
			world.getMissileControllers().remove(this);			
			return;
		}
		
		// Convert acceleration to frame time
		missile.acceleration.scl(delta);

		// apply acceleration to change velocity
		missile.velocity.add(missile.acceleration.x, missile.acceleration.y);
		
		// checking collisions with the surrounding blocks depending on missile's velocity
		checkCollisions(delta);

		// apply damping to halt Missile nicely
		//Missile.velocity.scl(DAMP);
		//missile.velocity.scl(missile.getDAMP());
		
		
		// ensure terminal X velocity is not exceeded
		if (missile.velocity.x > missile.MAX_VEL.x) 
			missile.velocity.x = missile.MAX_VEL.x;
		
		// ensure terminal Y velocity
		if (missile.velocity.y >  missile.MAX_VEL.y) 
			missile.velocity.y = missile.MAX_VEL.y;
		else if (missile.velocity.y <  -missile.MAX_VEL.y) 
			missile.velocity.y = -missile.MAX_VEL.y;
		
		
		// simply updates the state time
		missile.update(delta);

	}

	/** Collision checking **/
	private void checkCollisions(float delta) {
		// scale velocity to frame units
		missile.velocity.scl(delta);
		
		// Check if missile collides with Target
		if (collisionWithTarget()) {
			missile.getTarget().health -= missile.getDamage();
			die();
			return;
		}
		
		// CHECK COLLISION WITH BLOCKS ---- >
		
		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle missileRect = rectPool.obtain();
		// set the rectangle to missile's bounding box
		missileRect.set(missile.getBounds().x, missile.getBounds().y,
				missile.getBounds().width, missile.getBounds().height);
		
		// we first check the movement on the horizontal X axis
		int startX, endX;
		int startY = (int) missile.getBounds().y;
		int endY = (int) (missile.getBounds().y + missile.getBounds().height);
		// if missile is heading left then we check if he collides with the block on
		// his left
		// we check the block on his right otherwise
		if (missile.velocity.x < 0) {
			startX = endX = (int) Math.floor(missile.getBounds().x + missile.velocity.x);
		} else {
			startX = endX = (int) Math.floor(missile.getBounds().x + missile.velocity.x + missile.getBounds().width );
		}
		
		// get the block(s) missile can collide with
		populateCollidableBlocks(startX, startY, endX, endY);

		// simulate Missile's movement on the X
		missileRect.x += missile.velocity.x;

		// clear collision boxes in world for debug
		world.getCollisionRects().clear();

		// if missile collides, set state to dying
		for (Block block : collidable) {
			if (block == null)  continue;
			if (missileRect.overlaps(block.getBounds())) {
				//System.out.println("Missile Collision @ " + block.position.toString());
				die();
				world.getCollisionRects().add(block.getBounds()); // for debug
				break;
			}
		}

		

		// reset the x position of the collision box to check Y /////////////////////////////////////////////////////////
		missileRect.x = missile.position.x;

		// the same thing but on the vertical Y axis
		startX = (int) missile.getBounds().x;
		endX = (int) (missile.getBounds().x + missile.getBounds().width);
		if (missile.velocity.y < 0) {
			startY = endY = (int) Math.floor(missile.getBounds().y + missile.velocity.y );
		} else {
			startY = endY = (int) Math.floor(missile.getBounds().y + missile.velocity.y + missile.getBounds().height );
		}

		populateCollidableBlocks(startX, startY, endX, endY);

		missileRect.y += missile.velocity.y;

		for (Block block : collidable) {
			if (block == null) 	continue;
			if (missileRect.overlaps(block.getBounds())) {
				//System.out.println("missile Collision @ " + block.position.toString());
				die();
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		// reset the collision box's position on Y
		missileRect.y = missile.position.y;

		// update missile's position
		missile.position.add(missile.velocity);
		missile.getBounds().x = missile.position.x;
		missile.getBounds().y = missile.position.y;

		// un-scale velocity (not in frame time)
		missile.velocity.scl(1 / delta);

	}
	
	
	private boolean collisionWithTarget() {
		// First check if target is alive
		if (! missile.getTarget().alive) 
			return false;
		
		// Get the Missile Rectangle 
		Rectangle missileRect = rectPool.obtain();
		missileRect.set(missile.getBounds().x, missile.getBounds().y,
				missile.getBounds().width, missile.getBounds().height);
		
		// Get the enemy rectangle
		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle targetRect = rectPool.obtain();
		// set the rectangle to enemy's bounding box
		targetRect.set(missile.getTarget().getBounds().x, missile.getTarget().getBounds().y,
				missile.getTarget().getBounds().width, missile.getTarget().getBounds().height);
		
		// Check for collision
		if (missileRect.overlaps(targetRect)) return true;
		else return false;
	}
	
	/** populate the collidable array with the blocks found in the enclosing coordinates **/
	private void populateCollidableBlocks(int startX, int startY, int endX,	int endY) {
		collidable.clear();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (x >= 0 && y >= 0) {
					collidable.add(world.getBlock(x, y) );
				}
			}
		}
	}
	
	private void die() {
		world.getMissileControllers().remove(this);
		world.getAnimations().add(new ExplosionHit(missile.position, 1));//missile.SIZE.x));
	}
	
	/**
	 * Get the frames that belong to this Missile. 
	 * @return
	 */
	public TextureRegion[] getFrames() {
		return missile.getFrames();
	}

	public Entity getMissileEntity() {
		return missile;
	}
	public Missile getMissile() {
		return missile;
	}
	
	
}
