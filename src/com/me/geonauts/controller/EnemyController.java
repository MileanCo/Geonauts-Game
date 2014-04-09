package com.me.geonauts.controller;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;

public class EnemyController {

	// Collidable blocks.
	private Array<Block> collidable = new Array<Block>();
	
	// Model objects
	private World world;
	private AbstractEnemy enemy;
	
	/**
	 * Constructor to make the Controller for enemy
	 * @param world
	 */
	public EnemyController(World world, AbstractEnemy e) {
		this.world = world;
		this.enemy = e;
	}
	
	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	
	
	
	/** The main update method **/
	public void update(float delta) {
		// Convert acceleration to frame time
		enemy.getAcceleration().scl(delta);

		// apply acceleration to change velocity
		enemy.getVelocity().add(enemy.getAcceleration().x, enemy.getAcceleration().y);
		
		//System.out.println(a_y);
		// checking collisions with the surrounding blocks depending on enemy's
		// velocity
		checkCollisionWithBlocks(delta);

		// apply damping to halt enemy nicely
		//enemy.getVelocity().scl(DAMP);
		enemy.getVelocity().y *= enemy.getDAMP();
		
		// ensure terminal X velocity is not exceeded
		if (enemy.getVelocity().x > enemy.MAX_VEL.x) 
			enemy.getVelocity().x = enemy.MAX_VEL.x;
		
		// ensure terminal Y velocity
		if (enemy.getVelocity().y >  enemy.MAX_VEL.y) 
			enemy.getVelocity().y = enemy.MAX_VEL.y;
		else if (enemy.getVelocity().y <  -enemy.MAX_VEL.y) 
			enemy.getVelocity().y = -enemy.MAX_VEL.y;
				
		
		// simply updates the state time
		enemy.update(delta);

	}

	/** Collision checking **/
	private void checkCollisionWithBlocks(float delta) {
		// scale velocity to frame units
		enemy.getVelocity().scl(delta);

		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle enemyRect = rectPool.obtain();
		// set the rectangle to enemy's bounding box
		enemyRect.set(enemy.getBounds().x, enemy.getBounds().y,
				enemy.getBounds().width, enemy.getBounds().height);

		// we first check the movement on the horizontal X axis
		int startX, endX;
		int startY = (int) enemy.getBounds().y;
		int endY = (int) (enemy.getBounds().y + enemy.getBounds().height);
		// if enemy is heading left then we check if he collides with the block on
		// his left
		// we check the block on his right otherwise
		if (enemy.getVelocity().x < 0) {
			startX = endX = (int) Math.floor(enemy.getBounds().x + enemy.getVelocity().x);
		} else {
			startX = endX = (int) Math.floor(enemy.getBounds().x + enemy.getVelocity().x + enemy.getBounds().width );
		}
		
		// get the block(s) enemy can collide with
		populateCollidableBlocks(startX, startY, endX, endY);

		// simulate enemy's movement on the X
		enemyRect.x += enemy.getVelocity().x;

		// clear collision boxes in world for debug
		world.getCollisionRects().clear();

		// if enemy collides, set state to dying
		for (Block block : collidable) {
			if (block == null)  continue;
			if (enemyRect.overlaps(block.getBounds())) {
				enemy.setState(AbstractEnemy.State.DYING);
				world.getCollisionRects().add(block.getBounds()); // for debug
				break;
			}
		}

		
		
		
		// reset the x position of the collision box to check Y /////////////////////////////////////////////////////////
		enemyRect.x = enemy.getPosition().x;

		// the same thing but on the vertical Y axis
		startX = (int) enemy.getBounds().x;
		endX = (int) (enemy.getBounds().x + enemy.getBounds().width);
		if (enemy.getVelocity().y < 0) {
			startY = endY = (int) Math.floor(enemy.getBounds().y + enemy.getVelocity().y );
		} else {
			startY = endY = (int) Math.floor(enemy.getBounds().y + enemy.getVelocity().y + enemy.getBounds().height );
		}

		populateCollidableBlocks(startX, startY, endX, endY);

		enemyRect.y += enemy.getVelocity().y;

		for (Block block : collidable) {
			if (block == null) 	continue;
			if (enemyRect.overlaps(block.getBounds())) {
				System.out.println("Collision @ " + block.getPosition().toString());
				enemy.setState(AbstractEnemy.State.DYING);
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		// reset the collision box's position on Y
		enemyRect.y = enemy.getPosition().y;

		// update enemy's position
		enemy.getPosition().add(enemy.getVelocity());
		enemy.getBounds().x = enemy.getPosition().x;
		enemy.getBounds().y = enemy.getPosition().y;

		// un-scale velocity (not in frame time)
		enemy.getVelocity().scl(1 / delta);

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
}
