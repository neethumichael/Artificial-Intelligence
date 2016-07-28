package main.java.entrants.pacman.neethu.controllers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import main.java.entrants.pacman.neethu.util.MazeNode;
import main.java.entrants.pacman.neethu.util.Utilities;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import pacman.game.internal.Node;


public class AStarPacMan extends Controller<MOVE>{

	// graph stores the structure of maze 
	// path stores the A-star path to target O(d) Space complexity where d is the depth of the maze
	// aStarMove stores the next move
	// stepsTaken stores all the nodes visited by the Pacman O(n) Space complexity where n is the number of nodes in the maze
	private MazeNode[] graph;
	Utilities util = new Utilities();
	ArrayList<Integer> path = new ArrayList<Integer>();
	MOVE aStarMove = MOVE.NEUTRAL;
	HashSet<Integer> stepsTaken = new HashSet<Integer>();
	int level = -1;

	// getMove function takes current game state and timeDue as argument and returns the
	// next move to be taken by pacman
	public MOVE getMove(Game game, long timeDue)
	{
		ArrayList<Integer> targets = null;
		int dest = 0;
		int[] targetsArray = null;
		int[] bestPath = null;

		// reset steps taken and path if the level changes
		if(level!=-1) {
			if(game.getCurrentLevel()!=level) {
				System.out.println("level changed");
				path = new ArrayList<Integer>();
				stepsTaken = new HashSet<Integer>();
				level = game.getCurrentLevel();

			}
			else {
				level = game.getCurrentLevel();
			}
		}
		else {
			level = game.getCurrentLevel();
		}

		if(game.wasPacManEaten()){
			path = new ArrayList<Integer>();
		}


		int step = game.getPacmanCurrentNodeIndex();
		stepsTaken.add(step);

		if(path.isEmpty()){
			// get all the pills in the maze
			targets = new ArrayList<Integer>();
			int[] pills = game.getCurrentMaze().pillIndices;
			int[] powerPills = game.getCurrentMaze().powerPillIndices;

			for(int i=0;i<pills.length;i++) {
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

			// creates a graph using all the nodes present in currentMaze
			graph = util.createGraph(game.getCurrentMaze().graph);

			if(targetsArray.length>0) {
				// selects the closest pill from target pills
				dest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, DM.PATH);
				//GameView.addLines(game,Color.MAGENTA,game.getPacmanCurrentNodeIndex(),dest);
				// returns the A-Star path and bestPath/path stores the A-Star path to the target
				bestPath = computePathsAStar(game.getPacmanCurrentNodeIndex(), dest, aStarMove, game);

				if (bestPath.length > 0) {
					for(int i = 1; i < bestPath.length; i++){
						path.add(bestPath[i]);
					}
				}
			}
			else {
				System.out.println("target array is empty:The pills available is zero(bug): error in framework provided");
			}
		}

		// selects the next move towards path.remove(0) , which is the node location in A-star traversal
		if (path.size() > 0) {
			int next = path.remove(0);
			aStarMove = graph[next].reached;
			//game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path.remove(0),game.getPacmanLastMoveMade(), DM.PATH);		
		}
		return aStarMove;
	}

	// returns the best path to target using A-star
	// A Priority queue ‘open’ maintains a list of nodes to be visited.
	// ArrayList 'visited' stores the nodes already visited by the algorithm.
	// The function takes a node from open list and consider all its neighbors and f value for each node is calculated as f =g + h, where cost of each node is initialized to 1.
	// h is the heuristic and is taken as the shortest distance from the node being considered to the target node.
	// g value for each child node is updated as initial cost + its parent’s g value
	// whenever a new node is found, its parent, reached move, and g value is updated.
	public synchronized int[] computePathsAStar(int s, int t, MOVE lastMoveMade, Game game) {

		MazeNode start = graph[s];
		MazeNode target = graph[t];

		PriorityQueue<MazeNode> open = new PriorityQueue<MazeNode>();
		ArrayList<MazeNode> visited = new ArrayList<MazeNode>();

		start.g = 0;
		start.h = game.getShortestPathDistance(start.index, target.index);
		start.reached = lastMoveMade;

		open.add(start);

		while (!open.isEmpty()) {
			MazeNode currentNode = open.poll();
			visited.add(currentNode);

			if (currentNode.isEqual(target))
				break;

			for (MOVE move : currentNode.neighbours.keySet()) {
				if (move != currentNode.reached.opposite()) {
					MazeNode next = currentNode.neighbours.get(move);
					if (!open.contains(next.index) && !visited.contains(next.index)) {
						next.g = 1 + currentNode.g;
						next.h = game.getShortestPathDistance(next.index, target.index);
						next.parent = currentNode;
						next.reached = move;
						open.add(next);
					} else if (1 + currentNode.g < next.g) {
						next.g = 1 + currentNode.g;
						next.parent = currentNode;
						next.reached = move;

						if (open.contains(next))
							open.remove(next);

						if (visited.contains(next))
							visited.remove(next);

						open.add(next);
					}
				}
			}
		}
		return util.extractPath(target);
	}

	public synchronized int[] computePathsAStar(int s, int t, Game game) {
		return computePathsAStar(s, t, MOVE.NEUTRAL, game);
	}
}
