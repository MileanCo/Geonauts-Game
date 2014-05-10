package com.me.geonauts.model.entities;

import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.enums.BlockType;

public class Block extends Entity {
	/** Size of the entity */
	private static final Vector2 SIZE = new Vector2(1, 1);
	private BlockType type;
	
	public Block(Vector2 pos, BlockType t) {
		super(pos, SIZE);
		type = t;
	}
	
	public BlockType getType() {
		return type;
	}
	public void setType(BlockType t) {
		this.type = t;
	}
}
