package com.me.geonauts.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	/** Position (x, y) of the Entity in the world */
	public Vector2 position;
	
	/** Rectangle used for collision detection*/
	protected Rectangle bounds;

	/** Width and height of the entity */
	protected float width;
	protected float height; 
	
	//protected float rotation;
	
	/**
	 * Constructor to make a new Entity object.
	 * @param x
	 * @param y
	 */
	public Entity(Vector2 pos, float SIZE) {
		position = pos;
		
		// Make bounds/collision rectangle
		bounds = new Rectangle();
		bounds.setX(pos.x);
		bounds.setY(pos.y);
		setBounds(SIZE, SIZE);
		
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
	
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 pos) {
		position = pos;
	}
}
