package main.java.entrants.pacman.neethu.controllers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
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
	// path stores the A-star path to target O(n) Space complexity where n is the number of nodes in the maze
	// aStarMove stores the next move
	// stepsTaken stores all the nodes visited by the Pacman O(n) Space complexity where n is the number of nodes in the maze
	private MazeNode[] graph;
	Utilities util = new Utilities();
	ArrayList<Integer> path = new ArrayList<Integer>();
	MOVE aStarMove = MOVE.NEUTRAL;
	ArrayList<Integer> stepsTaken = new ArrayList<Integer>();

	// getMove function takes current game state and timeDue as argument and returns the
	// next move to be taken by pacman
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

		//***************************************************************************************

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
				GameView.addLines(game,Color.MAGENTA,game.getPacmanCurrentNodeIndex(),dest);
				// returns the A-Star path and bestPath/path stores the A-Star path to the target
				bestPath = computePathsAStar(game.getPacmanCurrentNodeIndex(), dest, aStarMove, game);

				if (bestPath.length > 0) {
					for(int i = 1; i < bestPath.length; i++){
						path.add(bestPath[i]);
					}
				}
			}
		}

		// selects the next move towards path.remove(0) , which is the node location in bfs traversal
		if (path.size() > 0) {
			aStarMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path.remove(0),game.getPacmanLastMoveMade(), DM.PATH);		
		}

		return aStarMove;
	}

	// returns the best path to target using A-star
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
