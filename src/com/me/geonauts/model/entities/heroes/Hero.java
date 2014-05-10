/**
 * 
 */
package com.me.geonauts.model.entities.heroes;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.World;
import com.me.geonauts.model.entities.Entity;
import com.me.geonauts.model.entities.Target;
import com.me.geonauts.model.entities.enemies.AbstractEnemy;
import com.me.geonauts.view.WorldRenderer;

/**
 * @author joel
 *
 */
public abstract class Hero extends Entity {
	// State stuff
	public enum State {
		FLYING, FALLING, DYING
	}	
	public State		state = State.FALLING;
	private float		stateTime = 0;
	private long		timeDied = 0;
	
	// Movement attributes
	protected float 	SPEED;
	protected float 	DAMP;
	public Vector2 		acceleration;
	public Vector2 		velocity;
	public Vector2		MAX_VEL;

	// Rotation stuff
	public float 		ROTATION_SPEED;
	private float 		ROTATION_SPEED_INCREMENT = 0.0025f;
	protected float 	PITCH;

	// Other attributes
	private Preferences prefs = Gdx.app.getPreferences("game-prefs");
	public boolean grounded;
	public int health;
	protected double reloadTime;
	protected int damage;
	
	protected int DAMAGE_MULTIPLIER = 10 - prefs.getInteger("Reload"); // Decrease damage by # of reload upgrades
	protected int HEALTH_MULTIPLIER = 50;

	
	// Targetting stuff
	protected LinkedList<Target> targets;
	private int MAX_TARGETS;
	
	/** Money of the game */
	public int money;
	/** Money at the start of game */
	private int startMoney;
	
	/** Score of the game */
	public int score;
	
	/** How far the Hero went in this world */
	private int distance = 0;
	
	public int enemiesKilled = 0;
	public int coinsCollected = 0;

	
	/**
	 * Creates a new Hero that is an Entity.
	 * @param position
	 * @param SIZE
	 */
	public Hero(Vector2 position, Vector2 SIZE, float ROTATION_SPEED, float PITCH, float SPEED, int health) {
		super(position, SIZE);
		targets = new LinkedList<Target>();
		
		// Set movement constants
		this.ROTATION_SPEED = ROTATION_SPEED;
		this.PITCH = PITCH;	
		this.SPEED = SPEED;
		MAX_VEL = new Vector2(SPEED, SPEED);
		DAMP = 0.85f;
		
		// Make new movement vectors
		acceleration = new Vector2(SPEED, 0);
		velocity = new Vector2();
		
		// Load preferences
		MAX_TARGETS = prefs.getInteger("max targets");
		money = prefs.getInteger("Money") + 10000;
		startMoney = money;
		
		// Calculate reload
		int reloads = prefs.getInteger("Reload");
		if (reloads <= 4) 
			reloadTime = (1.2f - reloads * 0.2f);
		else 
			reloadTime = (1f / reloads);
		
		// Make sure DAMAGE_MULTI doesnt go below 0.
		if (DAMAGE_MULTIPLIER <= 0) DAMAGE_MULTIPLIER = 1;
		
		this.health = health + prefs.getInteger("Health") * HEALTH_MULTIPLIER;
		damage = 35 + prefs.getInteger("Attack") * DAMAGE_MULTIPLIER;
		
		/**
		System.out.println(MAX_TARGETS);
		System.out.println(reloadTime);
		System.out.println(this.health);
		System.out.println(damage);
		*/
		
	}

	/**
	 * Update method to change hero's state.
	 * @param delta
	 */
	public void update(float delta) {
		// Add delta to the stateTime, used by animations.
		stateTime += delta;
		
		if (health <= 0) state = State.DYING;
		
		// If dying, stop movement and record time.
		if (state == State.DYING) {
			health = 0;
			if (timeDied == 0) 
				timeDied = System.currentTimeMillis();
		} else {
			timeDied = 0;
		}
				
		// If the Hero isn't on the ground, UPDATE ANGLE AND ACCELERATION
		if (! grounded) {
			// Update angle based State
			// Make hero go straight first couple meters
			if (position.x > WorldRenderer.WIDTH/2) { 
				if (state == State.FLYING) 
					angle += ROTATION_SPEED;
				else if ( state == State.FALLING || state == State.DYING)
					angle -= ROTATION_SPEED / 2.0f;
			}
				
			// Make sure angle isn't too big.
			if (angle > PITCH) angle = PITCH;
			else if (angle < -PITCH + 7) angle = -PITCH + 7;
			
			// Set acceleration
			acceleration.x = SPEED;
			acceleration.y = (float) (SPEED * angle);
		}
		
		// Set distance
		distance = (int) position.x;
		
		// Increase speed over time
		MAX_VEL.x += delta * World.SPEED_INCREMENT;
		MAX_VEL.y += delta * World.SPEED_INCREMENT;
		ROTATION_SPEED += delta * ROTATION_SPEED_INCREMENT;
		//System.out.println(ROTATION_SPEED);
	}
	
	public void addTarget(Target e) {
		if (targets.size() < MAX_TARGETS) {
			targets.addFirst(e);
		} else {
			targets.removeLast();
			addTarget(e);
		}
	}
	public void removeTarget(Target e) {
		targets.removeLast();
	}
	public List<Target> getTargets() {
		return targets;
	}
	public float getStateTime() {
		return stateTime;
	}
	public float getCamOffsetPosX() {
		return position.x - WorldRenderer.CAM_OFFSET + 1;
	}
	public float getDAMP() {
		return DAMP;
	}
	public Vector2 getMAX_VEL() {
		return MAX_VEL;
	}
	public long getTimeDied() {
		return timeDied;
	}
	public double getReloadTime() {
		return reloadTime;
	}
	
	
	// All Heros must implement the getFrames() method to return the proper images 
	public abstract TextureRegion[] getFrames ();

	/**
	 * Checks if the hero is targetting given enemy 
	 * @param e AbstractEnemy
	 * @return boolean
	 */
	public boolean targettingEnemy(AbstractEnemy e) {
		// Check if Hero is targetting given enemy e
		for (Target t : targets) {
			if (t.getEnemy().equals(e)) {
				return true;
			}
		}
		// Hero wasn't targetting enemy e
		return false;
	}

	public int getDamage() {
		return damage;
	}

	public int getScore() {
		return score;
	}

	public int getDistance() {
		return distance;
	}
	
	public int getMoneyEarned() {
		return money - startMoney;
	}
}
	