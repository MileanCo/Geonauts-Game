package com.me.geonauts.model.entities;

import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.enums.BlockType;

public class Block extends Entity {
	/** Size of the entity */
	public static final float SIZE = 1f;
	private BlockType type;
	
	public Block(Vector2 pos, BlockType t) {
		super(pos, SIZE);
		type = t;
	}


	
	public BlockType getType() {
		return type;
	}
}
