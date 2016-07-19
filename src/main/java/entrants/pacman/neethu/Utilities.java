package main.java.entrants.pacman.neethu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

public class Utilities {
	
	public MazeNode[] createGraph(Node[] nodes)
	{
		MazeNode[] graph = new MazeNode[nodes.length];
	
		for(int i=0;i<nodes.length;i++) {
			graph[i]=new MazeNode(nodes[i].nodeIndex);
		}
		
		for(int i=0;i<nodes.length;i++) {
			EnumMap<MOVE,Integer> edges=nodes[i].neighbourhood;
			MOVE[] moves=MOVE.values();
			for(int j=0;j<moves.length;j++) {
				if(edges.containsKey(moves[j])) {
					MazeNode x = graph[edges.get(moves[j])];
					graph[i].neighbors.put(moves[j], x);
				}
			}
		}
		
		return graph;
	}
	
	public int[] getPath(MazeNode target)
    {
		ArrayList<Integer> route = new ArrayList<Integer>();
		MazeNode current = target;
		route.add(current.index);
		while (current.parent != null) {
			current = current.parent;
			if(route.contains(current.index))
				break;
			else
				route.add(current.index);
			//TODO: Check loop detection
			/*if(current == target)
				break;*/
		}
		Collections.reverse(route);
		int[] routeArray=new int[route.size()];
        for(int i=0;i<routeArray.length;i++) {
        	routeArray[i]=route.get(i);
    	}
        return routeArray;
    }

}
