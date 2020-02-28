package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph {
	HashMap<Integer,Nodes> nodes;
	int first;
	
	public Graph(ArrayList<ArrayList<Integer>> adj,ArrayList<Integer> Nodes ) {
		nodes = new HashMap<Integer,Nodes>();
		first = Nodes.get(0);
		for(int i = 0 ; i< Nodes.size();i++) {
			nodes.put(Nodes.get(i),new Nodes());
		}
		int x1;
		int x2;
		ArrayList<Integer> l;
		for(int i = 0; i< adj.size();i++) {
			x1 = adj.get(i).get(0);
			x2 = adj.get(i).get(1);
			for(int j = 0;j<Nodes.size();j++ ) {
				for(int w = 0 ; w< Nodes.size();w++) {
					if(j != w) {
						if(x1 == Nodes.get(j) && x2 == Nodes.get(w)) {
							nodes.get(x1).addNeighbour(x2);
							nodes.get(x2).addNeighbour(x1);
						}
						
					}
				}
			}
		}
	}
	public void depthParcour(Nodes n) {
		ArrayList<Integer> m = n.getNeighbours();
		if(! n.is_visited()) {
			n.visit();
			for(int i = 0 ; i< m.size();i++) {
				depthParcour(nodes.get(m.get(i)));
			}
		}
		
	}
	public boolean connexity() {
		depthParcour(nodes.get(first));
		for(Nodes n : nodes.values()) {
			if(! n.visited) {
				return false;
			}
		}
		
		return true;
	}

	
	
}
