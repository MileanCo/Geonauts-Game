package com.me.geonauts.model.entities;

import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.enums.BlockType;

public class Block extends Entity {
	/** Size of the entity */
	private static final Vector2 sz = new Vector2(1f, 1f);
	private BlockType type;
	private int damage = 2;
	
	public Block(Vector2 pos, BlockType t) {
		super(pos, sz);
		type = t;
	}


	
	public BlockType getType() {
		return type;
	}
	public int getDamage() {
		return damage;
	}
}
