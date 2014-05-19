package com.me.geonauts.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	/** Position (x, y) of the Entity in the world */
	public Vector2 position;
	public final Vector2 SIZE;
	
	/** Rectangle used for collision detection*/
	protected Rectangle bounds;

	/** Width and height of the entity */
	protected float width;
	protected float height; 
	
	// Angle of the entity.
	public float angle;
	
	/**
	 * Constructor to make a new Entity object.
	 * @param x
	 * @param y
	 */
	public Entity(Vector2 pos, Vector2 SIZE) {
		this.SIZE = SIZE;
		position = pos;
		angle = 0f;
		
		// Make bounds/collision rectangle
		bounds = new Rectangle();
		bounds.setX(pos.x);
		bounds.setY(pos.y);
		setBounds(SIZE.x, SIZE.y);
		
	}	
		
	/**
	 * Sets the bounds of the collision Rectangle
	 * @param width
	 * @param height
	 */
	public void setBounds(float width, float height) {
		bounds.width = width;
		bounds.height = height;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public float getAngle() {
		return angle;
	}

	// FOR TWEENS
	public void setX(float x) {
		this.position.x = x;
	}
	public void setY(float y) {
		this.position.y = y;
	}
	public float getX() {
		return position.x;
	}
	public float getY() {
		return position.y;
	}
	/** 
	 * @return Center position of enemy, not bottom-left corner.
	 */
	public Vector2 getCenterPosition() {
		Vector2 V = position.cpy();
		//V.x += (SIZE.x / 2f);
		V.y += (SIZE.y / 2f);
		return V;
	}
	
}
