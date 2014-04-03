package com.me.geonauts.model;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.entities.Hero;
import com.me.geonauts.screens.GameScreen;
import com.me.geonauts.view.WorldRenderer;

/**
 * @author joel
 *
 */
public class World {

	// No idea what this is
    private Vector2 spanPosition;
	/** The collision boxes **/
	private Array<Rectangle> collisionRects = new Array<Rectangle>();
	/** Our player controlled hero **/
	private Hero hero;
	/** Screen of the game */
	private GameScreen screen;
	/** List of chunks that are to move through the world. */
	private LinkedList<Chunk> chunks;
	/** Number of chunks to use */
	public static int NUM_CHUNKS = 3;
	
	public World(GameScreen s) { //, float CAMERA_WIDTH, float CAMERA_HEIGHT) {	
		screen = s;
		
		hero = new Hero(new Vector2(WorldRenderer.CAM_OFFSET, 6));		
		
		resetChunks();
	}
	
	public void update(float delta) {
		// Check the Hero's position relative to the current chunk.		
		if (hero.getCamOffsetPosX() > (getCurrentChunk().position.x + Chunk.WIDTH)) {
			Chunk move_me = chunks.pop();
			System.out.println("Swapped chunk @ " + move_me.position.x);
			move_me.position.x = chunks.getLast().position.x + Chunk.WIDTH;
			move_me.build();
			chunks.addLast(move_me);
		}
		
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
	 * Converts a vector into Chunk coordinates.
	 * @param v
	 * @return
	 */
	public Vector2 toChunkCoords(Vector2 v) {
		return new Vector2(v.cpy().x - getCurrentChunk().position.x, v.y);
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
	
	public Block[][] getBlocks() {
		return chunks.getFirst().getBlocks();
	}
	public Block getBlock(int col, int row) {
		// Map to chunk coordinates
		col -= getCurrentChunk().position.x;
		row -= getCurrentChunk().position.y;
		
		if (row >= Chunk.HEIGHT) row = Chunk.HEIGHT - 1;
		
		//System.out.println(col + " " + row);
		
		// Get block from next chunk, since col is out of bounds for current chunk.
		if (col >= Chunk.WIDTH) 
			return getNextChunk().getBlock(col - Chunk.WIDTH, row);
		else
			return chunks.getFirst().getBlock(col, row);
		
	}
	public GameScreen getScreen() {
		return screen;
	}


	// --------------------

}