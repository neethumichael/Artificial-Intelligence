package main.java.entrants.pacman.neethu.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

// Utilities class has two functions createGraph and extractPath
// createGraph() is used to create the graph from given list of nodes
// extractPath() traverses from the given node upwards till the source node and reverse the list to
// get the path from source to destination
public class Utilities {

	// createGraph function creates a graph using the list of nodes passed as input
	public MazeNode[] createGraph(Node[] nodes) {
		MazeNode[] graph = new MazeNode[nodes.length];

		//create graph
		for (int i = 0; i < nodes.length; i++)
			graph[i] = new MazeNode(nodes[i].nodeIndex);;

			//add neighbours
			for(int i=0;i<nodes.length;i++) {
				EnumMap<MOVE,Integer> edges=nodes[i].neighbourhood;
				MOVE[] moves=MOVE.values();
				for(int j=0;j<moves.length;j++) {
					if(edges.containsKey(moves[j])) {
						MazeNode x = graph[edges.get(moves[j])];
						graph[i].neighbours.put(moves[j], x);
					}
				}
			}
			return graph;
	}

	// extractPath() takes target node as input and traces up to the root (source).
	// This function then reverse the above traced path to extract the path from source to destination(target)
	public synchronized int[] extractPath(MazeNode target) {
		ArrayList<Integer> route = new ArrayList<Integer>();
		MazeNode current = target;
		route.add(current.index);

		while (current.parent != null) {
			route.add(current.parent.index);
			current = current.parent;
		}

		Collections.reverse(route);

		int[] routeArray = new int[route.size()];

		for (int i = 0; i < routeArray.length; i++)
			routeArray[i] = route.get(i);

		return routeArray;
	}
}
