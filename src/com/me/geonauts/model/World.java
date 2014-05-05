package com.me.geonauts.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.geonauts.controller.EnemyController;
import com.me.geonauts.controller.EnemyMissileController;
import com.me.geonauts.controller.MissileController;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.anims.AbstractAnimation;
import com.me.geonauts.model.entities.enemies.BlueMob;
import com.me.geonauts.model.entities.enemies.Dwain;
import com.me.geonauts.model.entities.enemies.FireMob;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.heroes.Sage;
import com.me.geonauts.screens.GameScreen;
import com.me.geonauts.view.WorldRenderer;

/**
 * @author joel
 *
 */
public class World {
	/** Time spent the Hero is dead. */
	private static final long DEAD_TIME = 2500;
	/** Amount the game speed increases over time */
	public static final float SPEED_INCREMENT = 0.01f;
	
	/** The collision boxes for debug drawing, that's it. **/
	private Array<Rectangle> collisionRects = new Array<Rectangle>();
	/** Our player controlled hero **/
	private Hero hero;
	
	// List of main categories of entities in the world
	private List<EnemyMissileController> enemyMissiles;
	private List<MissileController> missiles;
	private List<EnemyController> enemies;
	private List<AbstractAnimation> anims;
	
	public Random randomGen = new Random();
	
	/** Screen of the Geonauts Game, (first file loaded when starting the program, 
	 * sets the different screens of gameplay) */
	private GameScreen screen;
	/** List of chunks that are to move through the world. */
	private LinkedList<Chunk> chunks;
	/** Number of chunks to use */
	public static int NUM_CHUNKS = 2;
	
	
	/** Score of the game */
	public int score;
	/** Money of the game */
	public int money;
	
	/** How far the Hero went in this world */
	private int distance = 0;
	
	/** Spawning variables */
	private int SPAWN_THRESHOLD = 450;
	private final int MIN_SPAWN = 100;
	private int INCREASE_SPAWN_EVERY = 30; //units
	private int SPAWN_INCREASE_RATE = 25;
	
	private boolean changed_spawn = false;
	
	private int total_upgrades;
	
	public World(GameScreen s) { //, float CAMERA_WIDTH, float CAMERA_HEIGHT) {	
		screen = s;
		
		// Create default hero Sage.
		hero = new Sage(new Vector2(WorldRenderer.CAM_OFFSET, 6));		
		
		// Create default lists
		enemies = new ArrayList<EnemyController>();
		missiles = new ArrayList<MissileController> ();
		anims = new ArrayList<AbstractAnimation> ();
		enemyMissiles = new ArrayList<EnemyMissileController> ();
		
		resetChunks();

		
		// Get preferences
		Preferences prefs = Gdx.app.getPreferences("game-prefs");
		money = prefs.getInteger("Money");
		
		total_upgrades = prefs.getInteger("total upgrades");
		
		// Make game harder based on total upgrades
		Dwain.health = 55 + total_upgrades * 5;
		Dwain.damage = 15 + total_upgrades * 2;
		FireMob.health = 45 + total_upgrades * 5;
		FireMob.damage = 10 + total_upgrades * 2;
		BlueMob.health = 35 + total_upgrades * 2;
		BlueMob.damage = 15 + total_upgrades * 2;
		
		
		SPAWN_THRESHOLD -= total_upgrades * 8;
		if (SPAWN_THRESHOLD <= 250) {
			SPAWN_THRESHOLD = 250;
		}
		
		System.out.println(Dwain.health);
		System.out.println(Dwain.damage);
		System.out.println(FireMob.health);
		System.out.println(FireMob.damage);
		System.out.println(SPAWN_THRESHOLD);
		
	}
	
	/**
	 * Updates the state of the world.
	 * @param delta
	 */
	public void update(float delta) {
		// Check the Hero's position relative to the current chunk.		
		if (hero.getCamOffsetPosX() > (getCurrentChunk().position.x + Chunk.WIDTH)) {
			Chunk move_me = chunks.pop();
			System.out.println("Swapped chunk @ " + move_me.position.x);
			move_me.position.x = chunks.getLast().position.x + Chunk.WIDTH;
			move_me.newBuild();
			chunks.addLast(move_me);
		}
		
		// If hero is dying, and time spent dead is long enough, go back to menu.
		if (hero.state == Hero.State.DYING) {
			if (System.currentTimeMillis() - hero.getTimeDied() >= DEAD_TIME ) {
				screen.toShopMenu();
			}
		}
				
		// Spawn some enemies!
		int spawn = randomGen.nextInt(SPAWN_THRESHOLD - 0) + 0;
		int y = randomGen.nextInt(WorldRenderer.HEIGHT - 1) + 1;

		if (spawn == 50 && total_upgrades >= 7) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			EnemyController ec = new EnemyController(this, new Dwain(pos));
			enemies.add(ec);
			
		} else if (spawn == 51 || (spawn == 50 && total_upgrades < 7)) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			EnemyController ec = new EnemyController(this, new FireMob(pos, hero));
			enemies.add(ec);
		} else if (spawn == 53) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			EnemyController ec = new EnemyController(this, new BlueMob(pos, hero));
			enemies.add(ec);
		}
		
		distance = (int) hero.position.x;
		
		// Check if we need to increase spawn rate
		if (distance % INCREASE_SPAWN_EVERY == 0 && SPAWN_THRESHOLD >= MIN_SPAWN && !changed_spawn) {
			SPAWN_THRESHOLD -= SPAWN_INCREASE_RATE;
			System.out.println("increased spawn: " + SPAWN_THRESHOLD);
			changed_spawn = true;
		} else if (distance % INCREASE_SPAWN_EVERY != 0)
			changed_spawn = false;
		
		

		// WORLD RENDERER HANDLES DRAWING AND UPDATING OF ALL ENTITIES
		
		
	}
	
	
	/**
	 * Reset the chunk for a new game.
	 */
	public void resetChunks() {
		chunks = new LinkedList<Chunk>();
		
		for (int i = 0; i < NUM_CHUNKS; i++) {
			Vector2 cpos = new Vector2( i * Chunk.WIDTH, 0);
			chunks.add(i, new Chunk(cpos, this));
		}
		
	}
	
	public Array<Rectangle> getCollisionRects() {
		return collisionRects;
	}
		
	/**
	 * Current chunk is the first one in the Linked List. Aka the one at the front of the chunk-queue.
	 * @return
	 */
	public Chunk getCurrentChunk() {
		return chunks.getFirst();
	}
	public Chunk getNextChunk() {
		return chunks.get(1); // 1 will ALWAYS be the 2nd element in the linked list.
	}
	public Hero getHero() {
		return hero;
	}
	public List<EnemyController> getEnemyControllers() {
		return enemies;
	}
	public List<MissileController> getMissileControllers() {
		return missiles;
	}
	public List<EnemyMissileController> getEnemyMissileControllers() {
		return enemyMissiles;
	}
	public List<AbstractAnimation> getAnimations() {
		return anims;
	}
	
	public Block[][] getBlocks() {
		return chunks.getFirst().getBlocks();
	}
	public Block getBlock(int col, int row) {
		// Map to chunk coordinates
		col -= getCurrentChunk().position.x;
		row -= getCurrentChunk().position.y;
		
		if (row >= Chunk.HEIGHT) row = Chunk.HEIGHT - 1;
		
		//System.out.println(col + " " + row);
		
		// LOL WUT THIS SHOULD NEVA HAPPEN
		if (col < 0) col = 0;
		if (row < 0) row = 0;
		
		// Get block from next chunk, since col is out of bounds for current chunk.
		if (col >= Chunk.WIDTH) 
			return getNextChunk().getBlock(col - Chunk.WIDTH, row);
		else
			return chunks.getFirst().getBlock(col, row);
		
	}


	public int getDistance() {
		return distance;
	}

	public int getScore() {
		return score;
	}
}