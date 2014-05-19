package com.me.geonauts.controller.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.anims.Explosion06;
import com.me.geonauts.model.entities.anims.Explosion10;
import com.me.geonauts.model.entities.anims.Explosion11;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.missiles.EnemyMissile;

public class EnemyController {

	// Collidable blocks.
	protected Array<Block> collidable = new Array<Block>();
	
	protected Vector2 BOUND_BOX_OFFSET;
	
	// Model objects
	protected World world;
	protected AbstractEnemy enemy;
	
	// Shooting fields
	protected float lastShootTime;
	
	/**
	 * Constructor to make the Controller for enemy
	 * @param world
	 */
	public EnemyController(World world, AbstractEnemy e) {
		this.world = world;
		this.enemy = e;
		BOUND_BOX_OFFSET = new Vector2(e.SIZE.x / 3f, e.SIZE.y / 3f);
		enemy.setBounds(enemy.SIZE.x - BOUND_BOX_OFFSET.x, enemy.SIZE.y - BOUND_BOX_OFFSET.y);
	}
	
	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	protected Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	
	
	
	/**
	 * Update the Enemy's position and check collisions
	 * @param delta
	 */
	public void update(float delta) {
		if (enemy.health <= 0) {
			die(true);
			return;
		}
		
		// Check for targets to shoot at IF it's time to shoot
		if (enemy.getStateTime() - lastShootTime > enemy.getReloadTime() 
				&& enemy.getReloadTime() > 0 
				&& enemy.position.x > world.getHero().position.x ) {
			
			// CREATE NEW MISSILE w/ TARGET
			EnemyMissile m = enemy.newMissile(enemy.getCenterPosition().add(enemy.SIZE.x/1.5f, 0), world.getHero());
			//, t.getEnemy(), 25);
			EnemyMissileController emc = new EnemyMissileController(world, m);

			world.getEnemyMissileControllers().add(emc);
		
			lastShootTime = enemy.getStateTime();
		}
		
		// Convert acceleration to frame time
		enemy.acceleration.scl(delta);

		// apply acceleration to change velocity
		enemy.velocity.add(enemy.acceleration.x, enemy.acceleration.y);
		
		
		enemy.velocity.scl(delta); // scale velocity to frame units
		
		// checking collisions with the surrounding blocks depending on enemy's velocity
		checkCollisionWithBlocks(delta);

		// update enemy's position
		enemy.position.add(enemy.velocity);
		enemy.getBounds().x = enemy.position.x + BOUND_BOX_OFFSET.x / 2f;
		enemy.getBounds().y = enemy.position.y + BOUND_BOX_OFFSET.y / 2f;

		enemy.velocity.scl(1 / delta); // un-scale velocity (not in frame time)
		
		// apply damping to halt enemy nicely
		enemy.velocity.y *= enemy.getDAMP();
		
		// ensure terminal X velocity is not exceeded
		if (enemy.velocity.x > enemy.MAX_VEL.x) 
			enemy.velocity.x = enemy.MAX_VEL.x;
		
		// ensure terminal Y velocity
		if (enemy.velocity.y >  enemy.MAX_VEL.y) 
			enemy.velocity.y = enemy.MAX_VEL.y;
		else if (enemy.velocity.y <  -enemy.MAX_VEL.y) 
			enemy.velocity.y = -enemy.MAX_VEL.y;
				
		// simply updates the state time
		enemy.update(delta);

	}

	/** Collision checking **/
	protected void checkCollisionWithBlocks(float delta) {
		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle enemyRect = rectPool.obtain();
		// set the rectangle to enemy's bounding box
		enemyRect.set(enemy.getBounds().x, enemy.getBounds().y,
				enemy.getBounds().width, enemy.getBounds().height);

		
		// Get hero object so we can get rects
		Hero hero = world.getHero();
		
		// Check if enemy collides with Hero
		Rectangle heroRect = rectPool.obtain();
		// set the rectangle to hero's bounding box
		heroRect.set(hero.getBounds().x, hero.getBounds().y, 
				hero.getBounds().width, hero.getBounds().height);
		
		// Does it collide? 
		if (enemyRect.overlaps(heroRect)) {
			hero.dealDamage(enemy.getDamage() * 2);
			die(false);
			return;
		} 
		
		
		
		// we first check the movement on the horizontal X axis
		int startX, endX;
		int startY = (int) enemy.getBounds().y;
		int endY = (int) (enemy.getBounds().y + enemy.getBounds().height);
		// if enemy is heading left then we check if he collides with the block on
		// his left
		// we check the block on his right otherwise
		if (enemy.velocity.x < 0) {
			startX = endX = (int) Math.floor(enemy.getBounds().x + enemy.velocity.x);
		} else {
			startX = endX = (int) Math.floor(enemy.getBounds().x + enemy.velocity.x + enemy.getBounds().width );
		}
		
		// get the block(s) enemy can collide with
		populateCollidableBlocks(startX, startY, endX, endY);

		// simulate enemy's movement on the X
		enemyRect.x += enemy.velocity.x;

		// clear collision boxes in world for debug
		world.getCollisionRects().clear();

		// if enemy collides, set state to dying
		for (Block block : collidable) {
			if (block == null)  continue;
			if (enemyRect.overlaps(block.getBounds())) {
				//System.out.println("Enemy Collision @ " + block.position.toString());
				die(false);
				world.getCollisionRects().add(block.getBounds()); // for debug
				break;
			}
		}

		

		// reset the x position of the collision box to check Y /////////////////////////////////////////////////////////
		enemyRect.x = enemy.position.x;

		// the same thing but on the vertical Y axis
		startX = (int) enemy.getBounds().x;
		endX = (int) (enemy.getBounds().x + enemy.getBounds().width);
		if (enemy.velocity.y < 0) {
			startY = endY = (int) Math.floor(enemy.getBounds().y + enemy.velocity.y );
		} else {
			startY = endY = (int) Math.floor(enemy.getBounds().y + enemy.velocity.y + enemy.getBounds().height );
		}

		populateCollidableBlocks(startX, startY, endX, endY);

		enemyRect.y += enemy.velocity.y;

		for (Block block : collidable) {
			if (block == null) 	continue;
			if (enemyRect.overlaps(block.getBounds())) {
				//System.out.println("Enemy Collision @ " + block.position.toString());
				die(false);
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		// reset the collision box's position on Y
		enemyRect.y = enemy.position.y;
	}
	
	/** populate the collidable array with the blocks found in the enclosing coordinates **/
	protected void populateCollidableBlocks(int startX, int startY, int endX,	int endY) {
		collidable.clear();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				if (x >= 0 && y >= 0) {
					collidable.add(world.getBlock(x, y) );
				}
			}
		}
	}
	
	protected void die(boolean shotDown) {
		enemy.alive = false;
		enemy.state = AbstractEnemy.State.DYING;
		world.getEnemyControllers().remove(this);
		
		// add new explosion
		int r = world.randomGen.nextInt(2 - 0) + 0;
		
		// Check if enemy was shot down or not
		if (! shotDown) r = 10;
		else {
			// Increase the score
			world.getHero().score += enemy.getValue() * 10;
			world.getHero().money += enemy.getValue();
			world.getHero().enemiesKilled++;
		}
		
		// Center explosion position to middle of enemy
		Vector2 explPos = enemy.position.cpy();
		explPos.x -= enemy.SIZE.x/2f;
		explPos.y -= enemy.SIZE.y/2f;
		
		switch (r) {
			case 0: 
				world.getAnimations().add(new Explosion10(explPos, enemy.SIZE.x));
				break;
			case 1: 
				world.getAnimations().add(new Explosion11(explPos, enemy.SIZE.x));
				break;
			case 10:
				world.getAnimations().add(new Explosion06(explPos, enemy.SIZE.x));
				break;
		}		
	}
	
	/**
	 * Get the frames that belong to this Enemy. 
	 * @return
	 */
	public TextureRegion[] getFrames() {
		return enemy.getFrames();
	}

	public Entity getEnemyEntity() {
		return enemy;
	}
	public AbstractEnemy getEnemy() {
		return enemy;
	}
	
	
}
