package graph;

import java.util.ArrayList;

public class Nodes {
	ArrayList<Integer> neighbours;
	boolean visited = false ;
	
	
	public Nodes() {
		neighbours = new ArrayList<Integer>();
		
	}
	public void visit() {
		visited = true;
	}
	public boolean is_visited() {
		return visited;
	}
	public void addNeighbour(int n) {
		neighbours.add(n);
	}
	public ArrayList<Integer> getNeighbours() {
		return neighbours;
	}
	
}
