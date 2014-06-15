package com.me.geonauts.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class FloatingText {
	public Vector2 position;
	public Vector2 velocity;
	
	public String text = "";
	public Color color = new Color(1, 1, 1, 1);
	
	protected double FLOAT_TIME = 2.5; // seconds
	protected boolean alive = true;
	
	public FloatingText(String text, Vector2 pos, Vector2 velocity) {
		this.position = pos;
		
		// LOL JUST OVER WRITE!!!
		//this.velocity = velocity;
		this.velocity = new Vector2(1, 1);
		
		
		
		this.text = text;
	}
	
	
	public void update(float delta) {
		// Move the text
		position.add(velocity.scl(delta));
		velocity.scl(1f/delta);		
		
		// Only show text for a certain amount of time
		FLOAT_TIME -= delta;
		if (FLOAT_TIME < 0) {
			alive = false;
		}
	}
	
	public boolean isAlive() {
		return alive;
	}
}
