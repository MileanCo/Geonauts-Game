package com.me.geonauts.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.me.geonauts.model.entities.Block;
import com.me.geonauts.view.WorldRenderer;

/**
 * Class that keeps a set of Blocks to move through the World.
 * @author joel
 *
 */
public class Chunk {
	/** Width and Height, block wise, of the chunk */
	public static int WIDTH = (int) (WorldRenderer.CAMERA_WIDTH * 2); // This has to match the file length
	public static int HEIGHT = (int) WorldRenderer.CAMERA_HEIGHT;
	
	/** The blocks of this chunk */
	private Block[][] blocks;
	
	/** Position of this Chunk in the world. Static. */
	public Vector2 position;
	
	private World world;
	
	// Random generator for the chunk.
	private Random random = new Random();
	
	public Chunk(Vector2 pos, World world) {
		this.position = pos;
		this.world = world;
	
		// System.out.println("cpos: " + pos);
		//loadChunkFile("chunk1");
		//();
		newBuild();
	}
	
	
	/**
	 * STRUCTURES
	 */
	private String[] pyramid = {"......", 
	                            "..Cc..",
	                            ".CBBc.",
	                            "CBBBBc",
	                            };
	
	private String[] ceilingPyramid =  {"tBBBBt", 
							            ".tBBt.",
							            "..tt..",
							            "......",
							            "......",
							            "......",
							            "......",
							            "......",
							            "......",
							            "......",
							            "......",
							            "......",
							            };
	
	private String[] box33 = {"CTc",
							  "SBs",
							  "SBs",
							  };
	
	private String[] box44 ={ "CTTc",
			  				  "SBBs",
			  				  "SBBs",
							  };
	
	private String[] floor = {"CTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTc"};
	
	private String[] ceiling =  {"tttttttttttttttttttttttttttttttttttttttt", 
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            "......",
            };
	
	private String[] wall_floor41 = {"E",
								   "W",
								   "W",
								   "B"};
	
	private String[] wall_floor51 = {"E",
								   "W",
								   "W",
								   "W",
								   "B"};
	
	private String[] wall_ceiling41 = { "W", 
							            "W",
							            "W",
							            "e",
							            ".",
							            ".",
							            ".",
							            ".",
							            ".",
							            ".",
							            ".",
							            ".",
							            };
	
	private String[] wall_ceiling51 = { "W", 
							            "W",
							            "W",
							            "W",
							            "e",
							            ".",
							            ".",
							            ".",
							            ".",
							            ".",
							            ".",
							            ".",
							            };
	
	private String[] stairFloor1 = {"E....",
									"Sc...",
									"SBc..",
									"SBBc.",
									"SBBBc" };
	
	private String[] stairFloor2 = {"....E",
									"...Cs",
									"..CBs",
									".CBBs",
									"CBBBs" };

	
	
	private String[] path1 = {".......CTc.......",
							  "......CBBs.......",
							  ".....CBBBs.......",
							  ".FwwwttttF......E",
							  "...............Cs",
							  "...............Ss",
							  "..............CBs" ,
							  ".CTTTTTTTTTTTTBBs"};
	
	private String[] divider1 = {".......CTc.......",
							  	"......CBBs.......",
							  	".....CBBBs.......",
							  	"FwwwwtttBtwwwwwwF",
							  	"........e........",
							  	".................",
							  	"................." ,
							  	"................."};
	
	
	/**
	 * Determines which build to make
	 */
	public void newBuild() {
		int build = random.nextInt(7 - 1) + 1;
		
		switch (build) {
			case 1: 
				build1();
				break;
			case 2:
				build2();
				break;
			case 3:
				build3();
				break;
			case 4:
				build4();
				break;
			case 5:
				build5();
				break;
			case 6:
				build6();
				break;
		}
	}
	
	private void build2() {
		reset();
		
		// Make some structures
		addStructure(pyramid, random.nextInt(10 - 0) + 0);
		
		addStructure(pyramid, random.nextInt(30 - 15) + 15);
		
		addStructure(ceilingPyramid, random.nextInt(33 - 15) + 15);
	}
	
	private void build3() {
		reset();
		
		addStructure(box33, random.nextInt(10 - 0) + 0);
		
		addStructure(pyramid, random.nextInt(30 - 15) + 15);
		
		addStructure(box44, random.nextInt(35 - 15) + 15);
		
		addStructure(wall_ceiling51, random.nextInt(WIDTH/2-2 - 0) + 0);
	}
	
	private void build4() {
		reset();
		
		addStructure(ceiling, 0);
		
		addStructure(box33, random.nextInt(20 - 0) + 0);
		
		addStructure(wall_floor41, random.nextInt(WIDTH - 15) + 15);
	}
	
	/**
	 * Make new blocks for this chunk
	 */
	private void build1() {
		reset();
		
		int row = 0;//random.nextInt(2 - 0) + 2;
		int col = random.nextInt(WIDTH/2-2 - 0) + 0;
		
		// Make the FLOOR
		/**
		blocks[0][1] =  new Block(new Vector2(position.x, 1), BlockType.TILE_TOP);
		blocks[0][0] =  new Block(new Vector2(position.x, 0), BlockType.BLANK);
		
		for (int col = 0; col < WIDTH; col++ ) {
			// Make the block position relative to the WORLD. Not in the chunk
			Vector2 block_pos = new Vector2(position.x + col, row); 
			// Set the new block @ its right point in the chunk.
			blocks[col][row] = new Block(block_pos, BlockType.TILE_TOP);
		}
		blocks[WIDTH-1][1] =  new Block(new Vector2(WIDTH-1 + position.x, 1), BlockType.TILE_TOP);
		blocks[WIDTH-1][0].setType(BlockType.BLANK);
		*/
		addStructure(floor, 0);
		
		// Make some walls
		/**
		
		blocks[col][0].setType(BlockType.BLANK);
		for (int r = 1; r < HEIGHT/2 - 1; r++) {
			// Make the block position relative to the WORLD. Not in the chunk
			Vector2 block_pos = new Vector2(position.x + col, r); 
			// Set the new block @ its right point in the chunk.
			blocks[col][r] = new Block(block_pos, BlockType.WALL);
		}
		blocks[col][HEIGHT/2 - 2].setType(BlockType.WALL_END_TOP);
		*/
		addStructure(wall_floor41, random.nextInt(WIDTH/2-2 - 0) + 0);
		
		
		// Make some ceiling walls
		/**
		col = random.nextInt(WIDTH - WIDTH/2) +  WIDTH/2 - 1;
		for (int r = HEIGHT-1; r >= HEIGHT/2+2; r--) {
			// Make the block position relative to the WORLD. Not in the chunk
			Vector2 block_pos = new Vector2(position.x + col, r); 
			// Set the new block @ its right point in the chunk.
			blocks[col][r] = new Block(block_pos, BlockType.WALL);
		}
		blocks[col][HEIGHT/2+2].setType(BlockType.WALL_END_BOT);
		*/
		addStructure(wall_ceiling51,random.nextInt(WIDTH - WIDTH/2) +  WIDTH/2 - 1);
		
		// Make a random floating block
		col = random.nextInt(WIDTH-1 - WIDTH/2) + WIDTH/2;
		row = random.nextInt(HEIGHT-3 - 3) + 3;
		Vector2 block_pos = new Vector2(position.x + col, row); 
		blocks[col][row] = new Block(block_pos, BlockType.FLOATING);
			
	}
	
	private void build5() {
		reset();
		
		addStructure(stairFloor1, random.nextInt(15 - 5) + 5);
		
		addStructure(wall_ceiling41, random.nextInt(20 - 0) + 0);
		
		addStructure(box44, random.nextInt(30 - 15) + 15);
		
		addStructure(wall_floor51, random.nextInt(WIDTH-wall_floor51[0].length()  - 25) + 25);
		
		addStructure(ceilingPyramid, random.nextInt(WIDTH-ceilingPyramid[0].length() - 20) + 20);
	}
	
	private void build6() {
		reset();
		
		addStructure(stairFloor2, random.nextInt(10 - 0) + 0);
		
		addStructure(wall_ceiling51, random.nextInt(15 - 2) + 2);
		
		//addStructure(box33, random.nextInt(20 - 12) + 12);
		
		//addStructure(r51, random.nextInt(WIDTH-wall_floor51[0].length()  - 25) + 25);
		
		addStructure(divider1, random.nextInt(WIDTH-divider1[0].length() - 20) + 20);
	}
	
	/**
	 * Adds a new "structure", or set of Blocks, to the chunk. 
	 * @param structure
	 * @param startCol
	 */
	private void addStructure(String[] structure, int startCol) {
		// Used to make the structure end at the bottom of the world.
		int endRow = structure.length-1;
	
		// Go through all the rows and make the blocks in them
		for (int row = 0; row < structure.length; row++) {
			// Used to iterate through all blocks in this row
			String s = structure[row];
			
			// Go through all blocks in this row
			for (int col = 0; col < s.length(); col++) {
				// Block at this column
				char tile = s.charAt(col);
				
				// Position of the Tile/Block
				int pos_x = startCol + col;
				int pos_y = endRow - row;
				
				// Ensure bounds aren't exceeded
				if (pos_x > WIDTH) pos_x = WIDTH;
				if (pos_y > HEIGHT) pos_y = HEIGHT;
				
				addTile(tile, pos_x, pos_y);
			}
		}
	}
	
	
	
	/**
	 * T = TOP
	 * W = WALL
	 * B = BLANK
	 * E = END FOR WALL
	 * C = CORNER
	 * F = FLOATING
	 * lower case letter = flipped type of tile
	 * @param filename
	 */
	private void loadChunkFile(String filename) {
		reset();
		
		FileHandle f = Gdx.files.internal("chunks/" + filename);
		//Scanner fs = new Scanner(f.readString());
		byte[] tiles = f.readBytes();
		
		//WIDTH = (int) tiles[0];
		//HEIGHT = (int) tiles[2]; //[1] is new line char
		
		// Read the file byte by byte
		int col = 0;
		int row = HEIGHT - 1; // goes from 0 to 11 in the array
		for (int i = 0; i < tiles.length; i++) {
			char tile = (char) tiles[i];
			
			// Check if we hit a new line
			if (tile == '\n') { 
				col = 0; // reset column
				row--;
				if (row < 0) break;
				continue;
			}
			addTile(tile, col, row);
			
			// Increase the column
			col++;
		}
	}
	
	/**
	 * Creates a new Block of type tile at the specified column and row.
	 * @param tile char
	 * @param col int
	 * @param row int
	 */
	private void addTile(char tile, int col, int row) {
		// Create the tile we read
		// Tile Top
		if (tile == 'T') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.TILE_TOP);
		// Tile Bot
		} else if (tile == 't') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.TILE_BOT);
		} else if (tile == 'S') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.TILE_SIDE_LEFT);
		// Tile Bot
		} else if (tile == 's') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.TILE_SIDE_RIGHT);
		// Blank
		} else if (tile == 'B') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.BLANK);
		// Wall
		} else if (tile == 'W') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.WALL);
		// Wall
		} else if (tile == 'w') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.WALL_HORIZ);
		// End Wall Top
		} else if (tile == 'E') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.WALL_END_TOP);
		// End Wall Bot
		} else if (tile == 'e') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.WALL_END_BOT);
		// Floating
		} else if (tile == 'F') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.FLOATING);
		// Corner Right
		} else if (tile == 'C') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.CORNER_RIGHT);
		// Corner Left
		} else if (tile == 'c') {
			blocks[col][row] =  new Block(new Vector2(position.x + col, row), BlockType.CORNER_LEFT);
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
			
	public Block[][] getBlocks() {
		return blocks;
	}
	public Block getBlock(int col, int row) {
		return blocks[col][row];
	}	
}
