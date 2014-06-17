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
import com.me.geonauts.controller.MissileController;
import com.me.geonauts.controller.enemies.BossWidowController;
import com.me.geonauts.controller.enemies.EnemyController;
import com.me.geonauts.controller.enemies.EnemyMissileController;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.anims.AbstractAnimation;
import com.me.geonauts.model.entities.anims.Coin;
import com.me.geonauts.model.entities.enemies.BlueMob;
import com.me.geonauts.model.entities.enemies.BossWidow;
import com.me.geonauts.model.entities.enemies.Dwain;
import com.me.geonauts.model.entities.enemies.FireMob;
import com.me.geonauts.model.entities.heroes.Bomber;
import com.me.geonauts.model.entities.heroes.Echo;
import com.me.geonauts.model.entities.heroes.Hero;
import com.me.geonauts.model.entities.particles.Particle;
import com.me.geonauts.model.entities.powerups.HealthPack;
import com.me.geonauts.model.entities.powerups.Powerup;
import com.me.geonauts.model.entities.powerups.ShieldPack;
import com.me.geonauts.screens.GameScreen;
import com.me.geonauts.view.WorldRenderer;

/**
 * @author joel
 *
 */
public class World {
	/** Time spent the Hero is dead. */
	private static final long DEAD_TIME = 2500; //milliseconds
	/** Amount the game speed increases over time */
	public static final float SPEED_INCREMENT = 0.02f;
	
	/** The collision boxes for debug drawing, that's it. **/
	private Array<Rectangle> collisionRects = new Array<Rectangle>();
	/** Our player controlled hero **/
	private Hero hero;
	
	// List of main categories of entities in the world
	private List<EnemyMissileController> enemyMissiles;
	private List<MissileController> missiles;
	private List<EnemyController> enemies;
	private List<AbstractAnimation> anims;
	private List<Particle> particles;
	private List<Powerup> powerups;
	
	public Random randomGen = new Random();
	
	/** Screen of the Geonauts Game, (first file loaded when starting the program, 
	 * sets the different screens of gameplay) */
	private GameScreen screen;
	/** List of chunks that are to move through the world. */
	private LinkedList<Chunk> chunks;
	/** Number of chunks to use */
	public static int NUM_CHUNKS = 2;
	
	
	/** Spawning variables */
	private int POWERUP_SPAWN_THRESHOLD = 2550;
	private int COIN_SPAWN_THRESHOLD = 225;
	private int SPAWN_THRESHOLD = 450;
	private final int MIN_SPAWN = 175;
	private int INCREASE_SPAWN_EVERY = 30; //units
	private int SPAWN_INCREASE_RATE = 25;
	
	private boolean changed_spawn = false;
	
	public boolean bossMode;
	private boolean bossSpawned = false;
	private int BOSS_LEVEL = 5;
	
	private int reload_upgrades;
	private int total_upgrades;
	private int games_played;
	
	public World(GameScreen s) { //, float CAMERA_WIDTH, float CAMERA_HEIGHT) {	
		screen = s;
		
		// Create default hero Sage.
		int h_type = randomGen.nextInt(2 - 0) + 0;
		if (h_type == 0) 
			hero = new Bomber(new Vector2(WorldRenderer.CAM_OFFSET, 6));	
		else
			hero = new Echo(new Vector2(WorldRenderer.CAM_OFFSET, 6));	
		
		
		// Create default lists
		enemies = new ArrayList<EnemyController>();
		missiles = new ArrayList<MissileController> ();
		anims = new ArrayList<AbstractAnimation> ();
		enemyMissiles = new ArrayList<EnemyMissileController> ();
		particles = new ArrayList<Particle> ();
		powerups = new ArrayList<Powerup> ();
				
		// Get preferences
		Preferences prefs = Gdx.app.getPreferences("game-prefs");
		total_upgrades = prefs.getInteger("total upgrades");
		reload_upgrades = prefs.getInteger("Reload");
		games_played = prefs.getInteger("games_played");
		
		// Make boss appear at game # 8 instead of #10 
		if (games_played < 10) {
			BOSS_LEVEL = 4;
		} else 
			BOSS_LEVEL = 5;
		
		bossMode = prefs.getBoolean("bossMode");
		
		// Make game harder based on total upgrades
		BossWidow.health = 1000 + total_upgrades * 50 + reload_upgrades * 70;
		Dwain.health = 55 + total_upgrades * 7;
		FireMob.health = 45 + total_upgrades * 7;
		BlueMob.health = 35 + total_upgrades * 5;
		
		BossWidow.damage = 125 + total_upgrades * 3;
		Dwain.damage = (int) (15 + total_upgrades * 1.5f);
		FireMob.damage = 10 + total_upgrades * 3;
		BlueMob.damage = 15 + total_upgrades * 3;
		
		ShieldPack.SHIELD = 150 + total_upgrades*5	;
		HealthPack.HEALTH = 200 + total_upgrades*6	;
		
		SPAWN_THRESHOLD -= total_upgrades * 8;
		if (SPAWN_THRESHOLD <= 250) {
			SPAWN_THRESHOLD = 250;
		}	
		
		resetChunks();
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
				screen.toEndGame();
			}
		}
				
		// Spawn some enemies!
		int spawn;
		if (bossMode) // if boss, spawn less enemies
			spawn = randomGen.nextInt(SPAWN_THRESHOLD*2 - 0) + 0;
		else
			spawn = randomGen.nextInt(SPAWN_THRESHOLD - 0) + 0;
		
		int y = randomGen.nextInt(WorldRenderer.HEIGHT - 1) + 1;

		
		// Dwain
		if (spawn == 1 && total_upgrades >= 7) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			EnemyController ec = new EnemyController(this, new Dwain(pos));
			enemies.add(ec);
		
		// Fire Mob
		} else if (spawn == 2 || (spawn == 1 && total_upgrades < 7)) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			EnemyController ec = new EnemyController(this, new FireMob(pos, hero));
			enemies.add(ec);

		// Blue Mob
		} else if (spawn == 3) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			EnemyController ec = new EnemyController(this, new BlueMob(pos, hero));
			enemies.add(ec);
		}
			
		spawn = randomGen.nextInt(COIN_SPAWN_THRESHOLD - 0) + 0;
		// Spawn some Coins!
		if (spawn == 4) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			// Dont spawn coins on blocks.
			if (getBlock((int)pos.x, (int)pos.y) == null )
				anims.add(new Coin(pos, 1, hero));
		}
		
		// Spawn some powerups!
		spawn = randomGen.nextInt(POWERUP_SPAWN_THRESHOLD - 0) + 0;
		// HEALTH PACK
		if (spawn == 50) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			Vector2 vel = new Vector2(randomGen.nextFloat() - 0.5f, randomGen.nextFloat() - 0.5f);
			// Dont spawn powerups on blocks.
			if (getBlock((int)pos.x, (int)pos.y) == null )
				powerups.add(new HealthPack(pos, vel, screen.getHeroController()));

		// SHIELD
		} else if (spawn == 51) {
			Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
			Vector2 vel = new Vector2(randomGen.nextFloat() - 0.5f, randomGen.nextFloat() - 0.5f);
			// Dont spawn powerups on blocks.
			if (getBlock((int)pos.x, (int)pos.y) == null )
				powerups.add(new ShieldPack(pos, vel, screen.getHeroController()));
		}
		
		
		// Check if we need to increase spawn rate
		if (hero.getDistance() % INCREASE_SPAWN_EVERY == 0 && SPAWN_THRESHOLD >= MIN_SPAWN && !changed_spawn) {
			SPAWN_THRESHOLD -= SPAWN_INCREASE_RATE;
			System.out.println("increased spawn: " + SPAWN_THRESHOLD);
			changed_spawn = true;
		} else if (hero.getDistance() % INCREASE_SPAWN_EVERY != 0)
			changed_spawn = false;
		
		// Spawn a boss if distance is right, no boss mode currently, and it's a boss level
		if (hero.getDistance() > Chunk.WIDTH-12  && (games_played % BOSS_LEVEL == 0 || bossMode) && !bossSpawned) {
			// If it isn't a boss level, and no boss mode is set, dont make a boss
			if (games_played % BOSS_LEVEL != 0 && !bossMode) {
				//omfg
			
			// Only spawn the boss if more than 3 games have been played
			} else if (games_played > 7) {
				y = randomGen.nextInt((int) (WorldRenderer.HEIGHT-BossWidow.SIZE.y - BossWidow.SIZE.y)) + (int)BossWidow.SIZE.y;
				Vector2 pos = new Vector2(hero.getCamOffsetPosX() + WorldRenderer.WIDTH, y);
				EnemyController ec = new BossWidowController(this,  new BossWidow(pos));
				
				enemies.add(ec);
				bossMode = true;
				bossSpawned = true;
			}
		}
		// WORLD RENDERER HANDLES DRAWING AND UPDATING OF ALL ENTITIES
		
		
	}
	
	
	/**
	 * Reset the chunk for a new game.
	 */
	public void resetChunks() {
		chunks = new LinkedList<Chunk>();
		
		
		for (int i = 0; i < NUM_CHUNKS; i++) {
			Vector2 cpos = new Vector2( i * Chunk.WIDTH, 0);
			
			// Make 2nd chunk blank if 2nd chunk and it's a boss level
			if (i >= 1 && games_played % BOSS_LEVEL == 0) 
				chunks.add(i, new Chunk(cpos, this, true));
			else
				chunks.add(i, new Chunk(cpos, this, false));
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
	public List<Particle> getParticles() {
		return particles;
	}
	public List<Powerup> getPowerups() {
		return powerups;
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

	public GameScreen getScreen() {
		return screen;
	}
	
	
}