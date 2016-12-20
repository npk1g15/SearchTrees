import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;;

public class BlockPuzzle {
	/**
	 * Main method to run the searches. This is where the grid is created and the searches are called on the grid
	 */
	public static void main(String[] args) {
		Grid grid = new Grid(4,4);
		grid.setStart(0,0, 1,0, 2,0, 3,0);
		grid.setFinal(2,1, 1,1, 1,0);
//		grid.addDTile(1,1, 2,1);
		BlockPuzzle main = new BlockPuzzle();
//		main.bfs(grid);
//		main.dfs(grid);
		main.ids(grid);
//		main.astar(grid);
	}

	/**
	 * Checks the position of the agent in the grid and works out the available moves
	 * @param grid The original state of the grid 
	 * @param node The node the grid is stored in
	 * @return An ArrayList containing the directions of movements available
	 */
	public ArrayList<String> movesAvailable(Grid grid, Node node) {
		ArrayList<String> directions = new ArrayList<String>();
		if (node.state.agent.getY() < grid.yLim) directions.add("up");
		if (node.state.agent.getY() > 0) directions.add("down");	
		if (node.state.agent.getX() > 0) directions.add("left");	
		if (node.state.agent.getX() < grid.xLim) directions.add("right");	
		return directions;
	}


	/**
	 * Runs Breadth-First Search on the grid and prints out the nodes expanded and the depth of the solution
	 * @param grid The grid for the search to run on
	 */
	public void bfs(Grid grid) {
		//Creates a Queue as this is First In First Out which is how bfs works
		Queue<Node> queue = new LinkedList<Node>();
		//Add the original state of the grid to the queue
		queue.add(new Node(null, grid));
		int i = 0;
		Node node = null;
		//Loops through all the nodes in the queue
		while (!queue.isEmpty()) {
			node = queue.remove();
			//Checks if the node is in the final state and beraks out the loop if it is
			if (node.checkState()) break;
			//Adds the nodes children to the queue
			for (String d : movesAvailable(grid, node)) {
				queue.add(new Node(node, node.state.moveAgent(d)));
			}
			i++;
		}
		System.out.println("Breadth-First Search\nNodes Expanded: " + i + "\nDepth: " + node.state.depth);
		node.showPath();
	}
	
	/**
	 * Runs Depth-First Search on the grid and prints out the nodes expanded and the depth of the solution
	 * @param grid The grid for the search to run on
	 */
	public void dfs(Grid grid) {
		//Creates a stack as this is Last In First Out which is how dfs works
		Stack<Node> stack = new Stack<Node>();
		ArrayList<String> directions = new ArrayList<String>();
		//Adds origianl state to the stack
		stack.add(new Node(null, grid));
		int i = 0;
		Node node = null;
		//Loops through the nodes in the stack
		while (!stack.isEmpty()) {
			//Take first node in the stack and check the state, breaks out loop if it is final
			node = stack.pop();
			if (node.checkState()) break;
			//Shuffles the order of the nodes children to reduce chance of getting stuck
			directions = movesAvailable(grid, node);
			Collections.shuffle(directions);
			for (String d : directions) {
				//Adds the children to the stack
				stack.push(new Node(node, node.state.moveAgent(d)));
			}
			i++;
			directions.clear();
		}
		System.out.println("Depth-First Search\nNodes Expanded: " + i + "\nDepth: " + node.state.depth);
//		node.showPath();
	}

	int depth = 0;
	/**
	 * Runs Iterative Deepening Search on the grid and prints out the nodes expanded and the depth of the solution
	 * @param grid The grid for the search to run on
	 */
	public void ids(Grid grid) {
		boolean solved = false;
		int i = 0;
		//Loops until the solution is found, increasing the depth of the search each loop
		while (!solved) {
			//Creates a stack as this is Last In First Out
			Stack<Node> stack = new Stack<Node>();
			stack.add(new Node(null, grid));
			Node node = null;
			//Loops through all the nodes in the stack
			while (!stack.isEmpty()) {
				node = stack.pop();
				//Checks if the node is the final solution and sets solved to true to end the method
				if (node.checkState()) { solved = true; break; }
				else {
					//Checks the depth of the node is within the limit and then adds it children to the stack
					if (node.state.depth < depth) {
						for (String d : movesAvailable(grid, node)) {
							stack.add(new Node(node, node.state.moveAgent(d)));
						}
						i++;
					}
				}
			}
			depth++; 
			//Adds the original grid back to the stack if no solution is found so it can loops through again
			if (stack.size()==0) stack.push(new Node(null, grid)); 
			else { 
				System.out.println("Iterative Deepening Search\nNodes Expanded: " + i + "\nDepth: " + node.state.depth);
				node.showPath(); 
			}
		}
	}

	/**
	 * Runs A* Heuristic Search on the grid and prints out the nodes expanded and the depth of the solution
	 * @param grid The grid for the search to run on
	 */
	public void astar(Grid grid) {
		//Creates a PriorityQueue of nodes to ensure the node with the lowest cost is expanded first
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(new Node(null, grid));
		Node node = null;
		int i = 0;
		//Loops through nodes in the PriorityQueue
		while (!queue.isEmpty()) {
			node = queue.remove();
			i++;
			//Checks if the node is in the final state
			if (node.checkState()) break;
			//Adds the nodes children to the PriorityQueue
			for (String d : movesAvailable(grid, node)) {
				queue.add(new Node(node, node.state.moveAgent(d)));
			}
		}
		System.out.println("A* Search\nNodes Expanded: " + i + "\nDepth: " + node.state.depth);
		node.showPath();
	}

	class Node implements Comparable<Node> {
		Node parent;
		Grid state;
		/**
		 * Constructor to set the nodes parent and the state of the node
		 * @param par The nodes parent
		 * @param grid The nodes state
		 */
		public Node(Node par, Grid grid) {
			parent = par;
			state = grid;
		}

		public boolean checkState() {
			Tile a = state.aTile;
			Tile b = state.bTile;
			Tile c = state.cTile;
			Tile d = state.dTile;
			boolean aF = false, bF = false, cF = false, dF = false;
			//Checks the position of each tile against the final position and sets boolean value accordingly
			if (a.getX()==state.aF[0] && a.getY()==state.aF[1]) aF = true; 
			if (b.getX()==state.bF[0] && b.getY()==state.bF[1]) bF = true; 
			if (c.getX()==state.cF[0] && c.getY()==state.cF[1]) cF = true;
			if (d==null) dF = true;
			else if (d.getX()==state.dF[0] && d.getY()==state.dF[1]) dF = true;

			//returns true if all tiles are in final position
			return (aF && bF && cF && dF);
		}

		public void showPath() {
			Node p = parent;
			Grid grid = state;
			//prints the current state if node has no parent
			if (p == null) grid.printGrid();
			else {
				//Loops through all the parents of the node to print the full path
				while (p.parent != null) {
					grid.printGrid();
					grid = p.state;
					p = p.parent;
				}
				if (p.parent == null) {
					grid.printGrid();
					grid = p.state;
					grid.printGrid();
				}
			}
		}

		@Override
		public int compareTo(Node n) {
			//Return the difference in cost between two nodes, to sort in PriorityQueue
			return this.state.calculateCost() - n.state.calculateCost();
		}
	}
}
