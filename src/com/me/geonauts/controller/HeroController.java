package com.me.geonauts.controller;

import java.util.HashMap;
import java.util.Map;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.geonauts.model.EntityAccessor;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.Missile;
import com.me.geonauts.model.entities.Target;
import com.me.geonauts.model.entities.anims.Explosion06;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;
import com.me.geonauts.model.entities.heroes.Hero;

public class HeroController {
	enum Keys {
		FLY, FIRE
	}

	// Collidable blocks.
	private Array<Block> collidable = new Array<Block>();
	// Used to make the bounding box for the hero smaller.
	private Vector2 BOUND_BOX_OFFSET;

	// Model objects
	private World world;
	private Hero hero;
	private TweenManager manager;
	
	// Shooting fields
	private float lastShootTime;
	private int MAX_TARGET_RADIUS = 5;


	// Keys
	static Map<Keys, Boolean> keys = new HashMap<HeroController.Keys, Boolean>();
	static {
		keys.put(Keys.FLY, false);
		keys.put(Keys.FIRE, false);
	};
	
	/** When the fly button was pressed */
	private long flyPressedTime;

	/**
	 * Constructor to make the Controller for hero
	 * @param world
	 */
	public HeroController(World world) {
		this.world = world;
		this.hero = world.getHero();
		BOUND_BOX_OFFSET = new Vector2(hero.SIZE.x/3, hero.SIZE.y / 3);

		// Change bounds to make colliding box smaller
		hero.setBounds(hero.SIZE.x - BOUND_BOX_OFFSET.x, hero.SIZE.y - BOUND_BOX_OFFSET.y);
		
		
		// Tween stuff
		Tween.registerAccessor(Entity.class, new EntityAccessor());
		manager = new TweenManager();
		
	}

	// This is the rectangle pool used in collision detection
	// Good to avoid instantiation each frame
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};

	// ** Key presses and touches **************** //

	/**
	 * 
	 * @param x of the mouse/touch in WORLD COORDINATES (not pixels)
	 * @param y of the mouse/touch in WORLD COORDINATES (not pixels)
	 */
	public void targetPressed(float x, float y) {
		keys.get(keys.put(Keys.FIRE, true));
		
		// Add hero's position to X so it's inside the camera view
		x += hero.getCamOffsetPosX();
		
		Vector2 touch = new Vector2(x, y);
		float dist;
		float closestDist = 99; //dummy value for first comparison
		AbstractEnemy closestEnemy = null;
		
		// Try to find a target
		for (EnemyController ec : world.getEnemyControllers() ) {
			AbstractEnemy e = ec.getEnemy();
			
			// See if current enemy is closer than previous enemy
			dist = touch.dst(e.position);
			if (dist < closestDist && ! hero.targettingEnemy(e) ) {
				closestDist = dist;
				closestEnemy = e;
			}
			
			// If actually the touch enemy, add that to list of targets
			if (ec.getEnemyEntity().getBounds().contains(x, y)) {
				closestDist = 0;
				closestEnemy = e;
				break;
			}
			
		}
		// If we found a closest enemy
		if (closestEnemy != null && closestDist < MAX_TARGET_RADIUS) {
			Target t = new Target(closestEnemy);
			hero.addTarget(t);
		}
		
		/**
		Tween.from(hero, EntityAccessor.POSITION_XY, 1.0f)
			.target(hero.position.x, hero.position.y)
			//.ease(Elastic.INOUT)
			.start(manager);
		
		//Entity e = new Entity(Missile.frames[0]);
		Tween.to(m, EntityAccessor.POSITION_XY, 1.0f)
			.target(target.x, target.y)
			//.ease(Elastic.INOUT)
			.start(manager);
		*/
	}
	public void targetReleased() {
		keys.get(keys.put(Keys.FIRE, false));
	}
	
	public void flyPressed() {
		keys.get(keys.put(Keys.FLY, true));
	}
	public void flyReleased() {
		keys.get(keys.put(Keys.FLY, false));
	}

	/**
	 * Update the Hero's position and check collisions
	 * @param delta
	 */
	public void update(float delta) {
		// Processing the input - setting the states of Hero
		if (hero.state != Hero.State.DYING) 
			processInput();
		
		// Check for targets to shoot at IF it's time to shoot
		if (hero.getStateTime() - lastShootTime > hero.getReloadTime()) {
			for (Target t : hero.getTargets()) {
				// CREATE NEW MISSILE w/ TARGET
				Missile m = new Missile(hero.position.cpy(), t.getEnemy(), 25);
				MissileController mc = new MissileController(world, m);

				world.getMissileControllers().add(mc);
				
			}

			lastShootTime = hero.getStateTime();
		}
		
		// Convert acceleration to frame time
		hero.acceleration.scl(delta);

		// apply acceleration to change velocity
		hero.velocity.add(hero.acceleration.x, hero.acceleration.y);
				
		// checking collisions with the surrounding blocks depending on Hero's
		// velocity
		checkCollisionWithBlocks(delta);

		// apply damping to halt Hero nicely
		hero.velocity.y *= hero.getDAMP();
		
		// ensure terminal X velocity is not exceeded
		if (hero.velocity.x > hero.MAX_VEL.x) 
			hero.velocity.x = hero.MAX_VEL.x;
		
		// ensure terminal Y velocity
		if (hero.velocity.y >  hero.MAX_VEL.y) 
			hero.velocity.y = hero.MAX_VEL.y;
		else if (hero.velocity.y <  -hero.MAX_VEL.y) 
			hero.velocity.y = -hero.MAX_VEL.y;
		
		// Update the rest of the Hero object
		manager.update(delta); // elapsed seconds != delta
		hero.update(delta);
	}

	/** Collision checking **/
	private void checkCollisionWithBlocks(float delta) {
		// scale velocity to frame units
		hero.velocity.scl(delta);

		// Obtain the rectangle from the pool instead of instantiating it
		Rectangle heroRect = rectPool.obtain();
		// set the rectangle to hero's bounding box
		heroRect.set(hero.getBounds().x, hero.getBounds().y,
				hero.getBounds().width, hero.getBounds().height);

		// we first check the movement on the horizontal X axis
		int startX, endX;
		int startY = (int) hero.getBounds().y;
		int endY = (int) (hero.getBounds().y + hero.getBounds().height);
		// if Hero is heading left then we check if he collides with the block on
		// his left we check the block on his right otherwise
		if (hero.velocity.x < 0) {
			startX = endX = (int) Math.floor(hero.getBounds().x + hero.velocity.x);
		} else {
			startX = endX = (int) Math.floor(hero.getBounds().x + hero.velocity.x + hero.getBounds().width );
		}
		
		// get the block(s) hero can collide with
		populateCollidableBlocks(startX, startY, endX, endY);

		// simulate hero's movement on the X
		heroRect.x += hero.velocity.x;

		// clear collision boxes in world for debug
		world.getCollisionRects().clear();

		// if hero collides, set state to dying
		for (Block block : collidable) {
			if (block == null)  continue;
			if (heroRect.overlaps(block.getBounds())) {
				//System.out.println("Collision @ " + block.position.toString());
				dead();
				world.getCollisionRects().add(block.getBounds()); // for debug
				break;
			}
		}

		
		
		// reset the x position of the collision box to check Y /////////////////////////////////////////////////////////
		heroRect.x = hero.position.x;

		// the same thing but on the vertical Y axis
		startX = (int) hero.getBounds().x;
		endX = (int) (hero.getBounds().x + hero.getBounds().width);
		if (hero.velocity.y < 0) {
			startY = endY = (int) Math.floor(hero.getBounds().y + hero.velocity.y );
		} else {
			startY = endY = (int) Math.floor(hero.getBounds().y + hero.velocity.y + hero.getBounds().height );
		}

		populateCollidableBlocks(startX, startY, endX, endY);

		heroRect.y += hero.velocity.y;

		for (Block block : collidable) {
			if (block == null) 	continue;
			if (heroRect.overlaps(block.getBounds())) {
				//System.out.println("Collision @ " + block.position.toString());
				dead();
				world.getCollisionRects().add(block.getBounds());
				break;
			}
		}
		// reset the collision box's position on Y
		heroRect.y = hero.position.y;

		// update Hero's position
		hero.position.add(hero.velocity);
		hero.getBounds().x = hero.position.x + BOUND_BOX_OFFSET.x / 2;
		hero.getBounds().y = hero.position.y + BOUND_BOX_OFFSET.y / 2;

		// un-scale velocity (not in frame time)
		hero.velocity.scl(1 / delta);

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
	
	private void dead() {
		System.out.println("Hero dead");
		hero.state = Hero.State.DYING;
		hero.velocity.x = 0;
		hero.velocity.y = 0;
		hero.grounded = true;
		world.getAnimations().add(new Explosion06(hero.position, hero.SIZE.x));
	}

	/** Change Hero's state and parameters based on input controls **/
	private boolean processInput() {
		if (keys.get(Keys.FLY)) {	
			hero.state = Hero.State.FLYING;
			flyPressedTime = System.currentTimeMillis();
			
		// If he's not flying, he's falling.
		} else {
			hero.state = Hero.State.FALLING;
		}
		if (keys.get(Keys.FIRE)) {

		}

		return false;
	}
}
