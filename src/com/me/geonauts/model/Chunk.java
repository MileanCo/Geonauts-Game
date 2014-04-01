package com.me.geonauts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.model.enums.BlockType;
import com.me.geonauts.view.WorldRenderer;

/**
 * Class that keeps a set of Blocks to move through the World.
 * @author joel
 *
 */
public class Chunk {
	/** Width and Height, block wise, of the chunk */
	public static int WIDTH = (int) (WorldRenderer.CAMERA_WIDTH * 2);
	public static int HEIGHT = (int) WorldRenderer.CAMERA_HEIGHT;
	
	/** The blocks of this chunk */
	private Block[][] blocks;
	
	/** Position of this Chunk in the world. Static. */
	public Vector2 position;
	
	private World world;
	
	// Random generator for the chunk.
	private final long SEED = 123456789L;
	private Random random = new Random(SEED);
	
	public Chunk(Vector2 pos, World world) {
		this.position = pos;
		this.world = world;
	
		System.out.println("cpos: " + pos);
		build();
	}
	
	/**
	 * Make new blocks for this chunk
	 */
	public void build() {
		reset();
		
		
		
		// Make the ROCK
		blocks[0][1] =  new Block(new Vector2(position.x, 1), BlockType.ROCK);
		int row = 0;
		for (int col = 0; col < WIDTH; col++ ) {
			// Make the block position relative to the WORLD. Not in the chunk
			Vector2 block_pos = new Vector2(position.x + col, row); 
			// Set the new block @ its right point in the chunk.
			blocks[col][row] = new Block(block_pos, BlockType.ROCK);
		}
		blocks[WIDTH-1][1] =  new Block(new Vector2(WIDTH-1 + position.x, 1), BlockType.ROCK);
		
		// Make some walls
		int col = random.nextInt(WIDTH-1 - 0) + 0;
		for (int r = 0; r < HEIGHT/2; r++) {
			// Make the block position relative to the WORLD. Not in the chunk
			Vector2 block_pos = new Vector2(position.x + col, r); 
			// Set the new block @ its right point in the chunk.
			blocks[col][r] = new Block(block_pos, BlockType.ROCK);
		}
		
		// Make some ceiling walls
		col = random.nextInt(WIDTH - WIDTH/2) +  WIDTH/2;
		for (int r = HEIGHT-1; r >= HEIGHT/2; r--) {
			// Make the block position relative to the WORLD. Not in the chunk
			Vector2 block_pos = new Vector2(position.x + col, r); 
			// Set the new block @ its right point in the chunk.
			blocks[col][r] = new Block(block_pos, BlockType.ROCK);
		}
		
	}
	
	/**
	 * Reset this chunk for a new 'level'.
	 */
	private void reset() {
		blocks = new Block[WIDTH][HEIGHT];
		// 'null out' the world
		for (int col = 0; col < WIDTH; col++) {
			for (int row = 0; row < HEIGHT; row++) {
				blocks[col][row] = null;
			}
		}
	}
	
	/**
	 * Returns the list of Block objects that are in the camera’s window and will be rendered.
	 * @param width of screen
	 * @param height of screen
	 * @return List<Block> 
	 */
	public List<Block> getDrawableBlocks() {
		// List to contain all drawable blocks
		List<Block> drawableBlocks = new ArrayList<Block>();
		
		// If the chunk's position is inside the screen
		if (position.x < world.getHero().getCamOffsetPosX() + WorldRenderer.CAMERA_WIDTH) {		
			// Get the X of the left-hand side of the screen
			int x1 = (int) (world.getHero().getCamOffsetPosX() - position.x) ;
			int y1 = 0; 
			if (x1 < 0) 	x1 = 0;	
	
			// get Right-hand side of screen		
			// TO-DO: For the next chunk, ONLY get the blocks that are INSIDE the screen... AKA x2 is wrong.
			int x2 = x1 + WorldRenderer.WIDTH + 1; 
			int y2 = WorldRenderer.HEIGHT - 1;
			
			if (x2 >= WIDTH) x2 = WIDTH - 1;
			if (y2 >= HEIGHT) y2 = HEIGHT - 1;
			
			//System.out.println(x1 + " | " + x2);
			
			// Make a list of all blocks within x...x2, y....y2
			Block block;
			for (int col = x1; col <= x2; col++) {
				for (int row = y1; row <= y2; row++) {
					block = blocks[col][row];
					// If there's a block here, add it to the list
					if (block != null) {
						drawableBlocks.add(block);
					}
				}
			}
		}
		
		return drawableBlocks;
	}
		
	public Block[][] getBlocks() {
		return blocks;
	}
	public Block getBlock(int col, int row) {
		return blocks[col][row];
	}	
}
