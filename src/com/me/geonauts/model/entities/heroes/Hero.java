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
	protected float		stateTime = 0;
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
	protected int health;
	protected int reloads;
	protected int damage;
	
	protected int DAMAGE_MULTIPLIER = 10 - prefs.getInteger("Reload"); // Decrease damage by # of reload upgrades
	protected int HEALTH_MULTIPLIER = 50;
	
	// Shield stuff
	public int shield = 0; // If this is EVER set to above 0, a shield animation is started
	
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
		money = prefs.getInteger("Money");
		startMoney = money;
		
		// Calculate reload
		reloads = prefs.getInteger("Reload");
		
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
	
	
	// Animation to render the Hero
	public abstract TextureRegion getKeyFrame();
	
	/**
	 * Returns the time in seconds of the reload time
	 * @param upgrades # times Reload upgraded
	 * @return float
	 */
	public float getReloadTime(int upgrades) {
		if (upgrades <= 4) 
			return (1.2f - upgrades * 0.2f);
		else 
			return (1f / upgrades);
	}
	
	/**
	 * Gets current reloadTime of hero's current upgrades.
	 * If shopping, use getReloadTime(upgrades) to specify how many current upgrades.
	 * @return
	 */
	public float getReloadTime() {
		return getReloadTime(reloads);
	}

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
	public int getHealth() {
		return health;
	}
	public void addHealth(int amount) {
		this.health += amount;
	}

	public int getMoneyEarned() {
		return money - startMoney;
	}
		
	/**
	 * Deals damage to the hero
	 * @param amount
	 */
	public void dealDamage(int amount) {
		// Hurt shield if it's still up
		if (shield > 0) {
			shield -= amount;

			// If shield is out, turn it off
			if (shield <= 0) {
				// Remaining damage hurts health, which is negative.
				health += shield;
				shield = 0;
				
			}
		// No shield, hurt health
		} else {
			health -= amount;
		}
		System.out.println(shield);
		System.out.println(health);
	}

	public void deactivateShield() {
		this.shield = 0;
	}


	

}
	