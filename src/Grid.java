import java.util.ArrayList;

public class Grid {
	Tile aTile, bTile, cTile, dTile, agent;
	int xLim, yLim;
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	int depth = 0;
	int[] aF = new int[2];
	int[] bF = new int[2];
	int[] cF = new int[2];
	int[] dF = new int[2];

	/**
	 * Construct the grid with no tiles
	 * @param xLim Width
	 * @param yLim Height
	 */
	public Grid(int xLim, int yLim) {
		if (xLim*yLim < 4) { 
			System.err.println("Grid must have at least 4 spaces");
			System.exit(0);
		}
		this.xLim = xLim-1;
		this.yLim = yLim-1;
	}

	/**
	 * Constructs a Grid with 3 Tiles and an agent along with storing the final position
	 */
	public Grid(int xLim, int yLim, Tile a, Tile b, Tile c, Tile ag, int agentMoves, int[] aF, int[] bF, int[] cF) {
		aTile = a; tiles.add(aTile);
		bTile = b; tiles.add(bTile);
		cTile = c; tiles.add(cTile);
		agent = ag;
		this.xLim = xLim; this.yLim = yLim;
		this.depth = agentMoves;
		this.aF = aF; this.bF = bF;	this.cF = cF;
	}

	/**
	 * Constructs a Grid with 4 Tiles and an agent along with storing the final position
	 */
	public Grid(int xLim, int yLim, Tile a, Tile b, Tile c, Tile d, Tile ag, int agentMoves, int[] aF, int[] bF, int[] cF, int[] dF) {
		aTile = a; tiles.add(aTile);
		bTile = b; tiles.add(bTile);
		cTile = c; tiles.add(cTile);
		dTile = d; tiles.add(dTile);
		agent = ag;
		this.xLim = xLim; this.yLim = yLim;
		this.depth = agentMoves;
		this.aF = aF; this.bF = bF;	this.cF = cF; this.dF = dF;
	}

	/**
	 * Sets the start position of A,B,C and agent tiles
	 */
	public void setStart(int aX, int aY, int bX, int bY, int cX, int cY, int agX, int agY) {
		aTile = new Tile("A");
		aTile.setCoords(aX, aY);
		tiles.add(aTile);

		bTile = new Tile("B");
		bTile.setCoords(bX, bY);
		tiles.add(bTile);

		cTile = new Tile("C");
		cTile.setCoords(cX, cY);
		tiles.add(cTile);

		agent = new Tile("Agent");
		agent.setCoords(agX, agY);
	}

	/**
	 * Sets the final position of A,B and C tiles
	 */
	public void setFinal(int ax, int ay, int bx, int by, int cx, int cy) {
		this.aF[0] = ax; this.aF[1] = ay;
		this.bF[0] = bx; this.bF[1] = by;
		this.cF[0] = cx; this.cF[1] = cy;
	}

	/**
	 * Adds a fourth tile, D, with its start position and its final position
	 */
	public void addDTile(int x, int y, int xF, int yF) {
		dTile = new Tile("D");
		dTile.setCoords(x, y);
		tiles.add(dTile);
		this.dF[0] = xF; this.dF[1] = yF;
	}

	/**
	 * @return The tiles in the grid
	 */
	public ArrayList<Tile> getState() {
		return tiles;
	}

	/**
	 * Moves the agent 1 tile in a certain direction
	 * @param direction The direction to move the agent
	 * @return The new state of the grid
	 */
	public Grid moveAgent(String direction) {
		int x = agent.getX();
		int y = agent.getY();
		int nx = x; int ny  = y;

		Tile newAg = new Tile("Agent");
		//Sets the new coordinates of the agent depending on the direction moved
		switch (direction) {
		case "up": 
			newAg.setCoords(x, y+1);
			ny = y+1;
			break;
		case "down":
			newAg.setCoords(x, y-1);
			ny = y-1;
			break;
		case "left":
			newAg.setCoords(x-1, y);
			nx = x-1;
			break;
		case "right":
			newAg.setCoords(x+1, y);
			nx = x+1; 
			break;
		}

		Tile ti = null;
		//Loops through the tiles in the grid and moves them if the agent moved into their position
		for (Tile t : tiles) {
			int tx = t.getX(); 
			int ty = t.getY();
			if (tx == nx && ty == ny) {
				ti = new Tile(t.getName());
				ti.setCoords(x, y);
			}
		}
		
		//Returns new state of grid if there is 3 tiles on the grid
		if (tiles.size()==3) {
			if (ti!=null ) {
				if (ti.getName().equals("A")) return new Grid(xLim, yLim, ti, bTile, cTile, newAg, depth+1, aF, bF, cF);
				if (ti.getName().equals("B")) return new Grid(xLim, yLim, aTile, ti, cTile, newAg, depth+1, aF, bF, cF);
				if (ti.getName().equals("C")) return new Grid(xLim, yLim, aTile, bTile, ti, newAg, depth+1, aF, bF, cF);
			}
			return new Grid(xLim, yLim, aTile, bTile, cTile, newAg, depth+1, aF, bF, cF);
		}
		//Returns new state of grid if there is 4 tiles on the grid
		if (tiles.size()==4) {
			if (ti!=null ) {
				if (ti.getName().equals("A")) return new Grid(xLim, yLim, ti, bTile, cTile, dTile, newAg, depth+1, aF, bF, cF, dF);
				if (ti.getName().equals("B")) return new Grid(xLim, yLim, aTile, ti, cTile, dTile, newAg, depth+1, aF, bF, cF, dF);
				if (ti.getName().equals("C")) return new Grid(xLim, yLim, aTile, bTile, ti, dTile, newAg, depth+1, aF, bF, cF, dF);
				if (ti.getName().equals("D")) return new Grid(xLim, yLim, aTile, bTile, cTile, ti, newAg, depth+1, aF, bF, cF, dF);
			}
			return new Grid(xLim, yLim, aTile, bTile, cTile, dTile, newAg, depth+1, aF, bF, cF, dF);
		}
		return null;
	}

	/**
	 * Prints the state of the grid to the console
	 */
	public void printGrid() {
		int x = 0;
		int y = yLim;
		//Loops through each row in the grid
		for (int i = 0; i < yLim+1; i++) {
			//Loops through each column in the row
			for (int j = 0; j < xLim+1; j++) {
				boolean tileThere = false;
				//Checks if there is a tile in that position
				for (Tile t : tiles) {
					if (t.getX() == x && t.getY() == y) {
						System.out.print("|" + t.getName());
						tileThere = true;
					}
				}
				//Checks if the agent is in that position
				if (agent.getX() == x && agent.getY() == y) {
					System.out.print("|a");
					tileThere = true;
				}
				if (!tileThere) System.out.print("| ");
				x++;
			}
			x = 0;
			System.out.println("|");
			y--;
		}
		System.out.println("");
	}

	/**
	 * Calculates the cost of the grid using the Manhattan distance
	 * @return The cost of the state
	 */
	public int calculateCost() {
		int cost = (Math.abs(aTile.getX()-aF[0]) + Math.abs(aTile.getY()-aF[1]) + Math.abs(bTile.getX()-bF[0]) + Math.abs(bTile.getY()-bF[1])
		+ Math.abs(cTile.getX()-cF[0]) + Math.abs(cTile.getY()-cF[1]) + depth);
		if (dTile!=null) cost = cost + Math.abs(dTile.getX()-dF[0]) + Math.abs(dTile.getY()-dF[1]);
		return cost;
	}
}
