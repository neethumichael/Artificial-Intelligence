package main.java.entrants.pacman.neethu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.PriorityQueue;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;


public class MyPacMan_AStar2 extends Controller<MOVE>{

	final int MAX_DEPTH = 100;
	private N[] graph;
	ArrayList<Integer> path = new ArrayList<Integer>();
	MOVE aStarMove = MOVE.NEUTRAL;
	ArrayList<Integer> stepsTaken = new ArrayList<Integer>();
	public MOVE getMove(Game game, long timeDue)
	{
		ArrayList<Integer> targets = null;
		int dest = 0;
		int[] targetsArray = null;
		int[] bestPath = null;
		if(game.wasPacManEaten()){
			path = new ArrayList<Integer>();
		}
		int step = game.getPacmanCurrentNodeIndex();
        stepsTaken.add(step);
        
        
        //********************************************************************************************
        // *************************** GHOST *********************************************************
         int current = game.getPacmanCurrentNodeIndex();
     	// Strategy 1: Adjusted for PO
         for (GHOST ghost : GHOST.values()) {
             // If can't see these will be -1 so all fine there
             if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                 int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                 if (ghostLocation != -1) {
                     if (game.getShortestPathDistance(current, ghostLocation) < 20) {
                     	GameView.addLines(game,Color.MAGENTA,current,ghostLocation);
                     	// GameView can be used to debug             
                     	path = new ArrayList<Integer>();
                         return game.getNextMoveAwayFromTarget(current, ghostLocation, DM.PATH);
                     }
                 }
             }
         }

         /// Strategy 2: Find nearest edible ghost and go after them
         int minDistance = Integer.MAX_VALUE;
         GHOST minGhost = null;
         for (GHOST ghost : GHOST.values()) {
             // If it is > 0 then it is visible so no more PO checks
             if (game.getGhostEdibleTime(ghost) > 0) {
                 int distance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));

                 if (distance < minDistance) {
                     minDistance = distance;
                     minGhost = ghost;
                 }
             }
         }

         if (minGhost != null) {
         	GameView.addLines(game,Color.MAGENTA,current,game.getGhostCurrentNodeIndex(minGhost));
         	path = new ArrayList<Integer>();
             return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minGhost), DM.PATH);
         }
         
         //***************************************************************************************
        
		if(path.isEmpty()){
			// get all the pills in the maze
       	 targets = new ArrayList<Integer>();
   		int[] pills = game.getCurrentMaze().pillIndices;
           int[] powerPills = game.getCurrentMaze().powerPillIndices;
           
           for(int i=0;i<pills.length;i++) {
           	/*Boolean pillStillAvailable = game.isPillStillAvailable(i);
           	if(!(pillStillAvailable == null) && !(pillStillAvailable == false))*/
           	if(!stepsTaken.contains(pills[i])) {
           		targets.add(pills[i]);
           	
           	}
           	
           }
        // add power pills to target array (pills which are not eaten)
           for(int i=0;i<powerPills.length;i++) {
           	if(!stepsTaken.contains(powerPills[i])) {
           		targets.add(powerPills[i]);
           	}          	
           }
        // move targets to targetsArray 
       	targetsArray = new int[targets.size()]; 
	        if (!targets.isEmpty()) {
	            for (int i = 0; i < targets.size(); i++) {
	                targetsArray[i] = targets.get(i);
	            }
	        }
	        
			createGraph(game.getCurrentMaze().graph);
			//int s, int t, MOVE lastMoveMade, Game game
			
			if(targetsArray.length>0) {
	        	dest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, DM.PATH);
	        	GameView.addLines(game,Color.MAGENTA,game.getPacmanCurrentNodeIndex(),dest);
			bestPath = computePathsAStar(game.getPacmanCurrentNodeIndex(), dest, aStarMove, game);
			
			if (bestPath.length > 0) {
				for(int i = 1; i < bestPath.length; i++){
					path.add(bestPath[i]);
				}
			}
			}
		}
		
		if (path.size() > 0) {
			aStarMove = game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(), path.remove(0));
		}
		else {
			aStarMove = MOVE.NEUTRAL;
		}
		return aStarMove;
	}
	
	
	 public void createGraph(Node[] nodes) {
	        graph = new N[nodes.length];

	        //create graph
	        for (int i = 0; i < nodes.length; i++)
	            graph[i] = new N(nodes[i].nodeIndex);

	        //add neighbours
	        for (int i = 0; i < nodes.length; i++) {
	            EnumMap<MOVE, Integer> neighbours = nodes[i].neighbourhood;
	            MOVE[] moves = MOVE.values();

	            for (int j = 0; j < moves.length; j++)
	                if (neighbours.containsKey(moves[j]))
	                    graph[i].adj.add(new E(graph[neighbours.get(moves[j])], moves[j], 1));
	        }
	    }
	
	
	
	 public synchronized int[] computePathsAStar(int s, int t, MOVE lastMoveMade, Game game) {
	        N start = graph[s];
	        N target = graph[t];

	        PriorityQueue<N> open = new PriorityQueue<N>();
	        ArrayList<N> closed = new ArrayList<N>();

	        start.g = 0;
	        start.h = game.getShortestPathDistance(start.index, target.index);

	        start.reached = lastMoveMade;

	        open.add(start);

	        while (!open.isEmpty()) {
	            N currentNode = open.poll();
	            closed.add(currentNode);

	            if (currentNode.isEqual(target))
	                break;

	            for (E next : currentNode.adj) {
	                if (next.move != currentNode.reached.opposite()) {
	                    double currentDistance = next.cost;

	                    if (!open.contains(next.node) && !closed.contains(next.node)) {
	                        next.node.g = currentDistance + currentNode.g;
	                        next.node.h = game.getShortestPathDistance(next.node.index, target.index);
	                        next.node.parent = currentNode;

	                        next.node.reached = next.move;

	                        open.add(next.node);
	                    } else if (currentDistance + currentNode.g < next.node.g) {
	                        next.node.g = currentDistance + currentNode.g;
	                        next.node.parent = currentNode;

	                        next.node.reached = next.move;

	                        if (open.contains(next.node))
	                            open.remove(next.node);

	                        if (closed.contains(next.node))
	                            closed.remove(next.node);

	                        open.add(next.node);
	                    }
	                }
	            }
	        }

	        return extractPath(target);
	    }

	    public synchronized int[] computePathsAStar(int s, int t, Game game) {
	        return computePathsAStar(s, t, MOVE.NEUTRAL, game);
	    }

	    private synchronized int[] extractPath(N target) {
	        ArrayList<Integer> route = new ArrayList<Integer>();
	        N current = target;
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

	    public void resetGraph() {
	        for (N node : graph) {
	            node.g = 0;
	            node.h = 0;
	            node.parent = null;
	            node.reached = null;
	        }
	    }
}
