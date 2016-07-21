package main.java.entrants.pacman.neethu.controllers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import main.java.entrants.pacman.neethu.util.MazeNode;
import main.java.entrants.pacman.neethu.util.Utilities;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class BFSPacMan extends Controller<MOVE>{

	// path array stores the BFS path to the target. O(n) Space complexity where n is the number of nodes in the maze
	// stepsTaken stores all the nodes visited by the Pacman O(n) Space complexity where n is the number of nodes in the maze
	// bfsMove stores the next move. 
	ArrayList<Integer> path = new ArrayList<Integer>();
	HashSet<Integer> stepsTaken = new HashSet<Integer>();
	MOVE bfsMove = MOVE.NEUTRAL;
	Utilities util = new Utilities();
	private MazeNode[] graph;

	@Override
	// getMove function takes current game state and timeDue as argument and returns the
	// next move to be taken by pacman
	public MOVE getMove(Game game, long timeDue) {
		ArrayList<Integer> targets = null;
		int[] targetsArray = null;
		int[] bestPath = null;



		int dest = 0;

		// reset the path array if pacman was eaten by the ghost in current state
		if(game.wasPacManEaten()){
			path = new ArrayList<Integer>();
		}

		// updates the set stepsTaken with the current pacman location
		stepsTaken.add(game.getPacmanCurrentNodeIndex());

		//********************************************************************************************
		// *************************** GHOST *********************************************************



		//***************************************************************************************
		// following code adds all the pills and power pills and do BFS on nearest pill/power pill

		// check if the path is empty
		if(path.isEmpty()) {
			graph = util.createGraph(game.getCurrentMaze().graph);

			// get all the pills in the maze
			targets = new ArrayList<Integer>();
			int[] pills = game.getCurrentMaze().pillIndices;
			int[] powerPills = game.getCurrentMaze().powerPillIndices;

			//add pills to target array (pills which are not eaten)
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


			if(targetsArray.length>0) {

				// selects the closest pill from target pills
				dest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, DM.PATH);

				// returns the BFS path and bestPath/path stores the BFS path to the target
				bestPath = getBFSPath(game.getPacmanCurrentNodeIndex(), game,dest);

				if(bestPath!=null){
					if (bestPath.length > 0) {
						for(int i = 1; i < bestPath.length; i++){
							path.add(bestPath[i]);
						}
					}
				}
			}       
		}

		if (path.size() > 0) {
			int tempPath[] = new int[path.size()];
			for(int i=0;i<path.size();i++) {
				tempPath[i] = path.get(i);
			}

			// selects the next move towards path.remove(0) , which is the node location in bfs traversal
			bfsMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path.remove(0), game.getPacmanLastMoveMade(), DM.PATH);				
		}	       
		return bfsMove;
	}

	// takes current game state (game) , start node index (s) and target node index (d)

	public int[] getBFSPath(int s, Game game, int d)
	{
		Queue<MazeNode> q = new LinkedList<MazeNode>();
		ArrayList<MazeNode> closed = new ArrayList<MazeNode>();
		MazeNode start=graph[s];
		MazeNode target=graph[d];
		start.reached = MOVE.NEUTRAL;
		q.add(start);
		MazeNode current = null;
		while (!q.isEmpty()) {

			current = q.peek();
			q.poll();
			closed.add(current);

			if (current.index == target.index) {
				break;
			}

			for (MOVE move : current.neighbours.keySet()) {
				if(move != current.reached.opposite()) {
					MazeNode child = current.neighbours.get(move);
					if (closed.contains(child)) {
						continue;
					}
					child.g = current.g + 1;
					child.parent = current;
					child.reached = move;
					q.add(child);											
				}
			}
		}

		return util.extractPath(current);
	}
}
