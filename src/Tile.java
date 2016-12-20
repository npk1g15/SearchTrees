
public class Tile {
	private String name;
	private int x, y;
	
	/**
	 * Constructs a tile
	 * @param name The name of the tile
	 */
	public Tile(String name) {
		this.name = name;
	}
	
	/**
	 * @return The tiles name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the coordinates of the tile
	 * @param xc X-Coordinate
	 * @param yc Y-Coordinate
	 */
	public void setCoords(int xc, int yc) {
		this.x = xc;
		this.y = yc;
	}
	
	/**
	 * @return The X-Coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return The Y-Coordinate
	 */
	public int getY() {
		return y;
	}
}
