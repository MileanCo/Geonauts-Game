package com.me.geonauts.controller.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.anims.Explosion06;
import com.me.geonauts.model.entities.anims.Explosion10;
import com.me.geonauts.model.entities.anims.Explosion11;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;
import com.me.geonauts.model.entities.enemies.BossWidow;
import com.me.geonauts.model.entities.missiles.EnemyMissile;
import com.me.geonauts.model.enums.Achievement;
import com.me.geonauts.view.WorldRenderer;

public class BossWidowController extends EnemyController {	
	
	private float OLD_SPEED;
	/**
	 * Constructor to make the Controller for enemy
	 * @param world
	 */
	public BossWidowController(World world, AbstractEnemy e) {
		super(world, e);
		OLD_SPEED = e.SPEED;
	}

	
	
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
		
		checkCollideHero();
		
		// Convert acceleration to frame time
		enemy.acceleration.scl(delta);

		// apply acceleration to change velocity
		enemy.velocity.add(enemy.acceleration.x, enemy.acceleration.y);	
		
		// update enemy's position
		// If boss is too far to left, set direction -->
		if (enemy.position.x < world.getHero().position.x + 3 ) {
			enemy.direction.x = 1;
			enemy.SPEED = ( world.getHero().MAX_VEL.x + OLD_SPEED);
			enemy.MAX_VEL.x = enemy.SPEED*1.5f;
		// If boss is too far to right, set direction <--
		} else if (enemy.position.x > world.getHero().getCamOffsetPosX() + WorldRenderer.CAMERA_WIDTH/1.5f) {
			enemy.direction.x = -1;
			enemy.SPEED = OLD_SPEED*1.5f;  
		} 
		if (enemy.position.y > WorldRenderer.CAMERA_HEIGHT/1.5f) {
			enemy.direction.y = -1;
		} else if (enemy.position.y < 3) {
			enemy.direction.y = 1;
		}
		
		
		enemy.velocity.scl(delta); 	// scale velocity to frame units
		// checking collisions with the surrounding blocks depending on enemy's velocity
		//checkCollisionWithBlocks(delta);
		
		enemy.position.add(enemy.velocity);
		enemy.getBounds().x = enemy.position.x + BOUND_BOX_OFFSET.x / 2f;
		enemy.getBounds().y = enemy.position.y + BOUND_BOX_OFFSET.y / 2f;
		
		enemy.velocity.scl(1 / delta);	// un-scale velocity (not in frame time)

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
		
		enemy.update(delta);
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
	
	
	protected void die(boolean shotDown) {
		super.die(shotDown);
		world.bossMode = false;
		world.getScreen().getGame().getActionResolver().unlockAchievement(Achievement.BLACK_WIDOW);
	}
	
	private void checkCollideHero() {
		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle enemyRect = rectPool.obtain();
		// set the rectangle to enemy's bounding box
		enemyRect.set(enemy.getBounds().x, enemy.getBounds().y,
				enemy.getBounds().width, enemy.getBounds().height);
		
		// Does it collide? 
		if (enemyRect.overlaps(world.getHero().getBounds()) && world.getHero().shield == 0) {
			world.getHero().alive = false;
			
			return;
		} 
	}
	
}
