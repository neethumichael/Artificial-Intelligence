package main.java.entrants.pacman.neethu;

import java.awt.Color;
import java.util.ArrayList;

import pacman.Executor;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MyPacMan_POBFS extends Controller<MOVE>{
	final int MAX_DEPTH = 100;
	private MOVE bfsMove=MOVE.NEUTRAL;
	ArrayList<Integer> path = new ArrayList<Integer>();
	Utilities util = new Utilities();
	private MazeNode[] graph;

	public MOVE getMove(Game game, long timeDue)
	{
		if(game.wasPacManEaten()){
			path = new ArrayList<Integer>();
		}
		 
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
			graph = util.createGraph(game.getCurrentMaze().graph);

			int[] bestPath = getPath(game.getPacmanCurrentNodeIndex(), game, MAX_DEPTH);
			
			if (bestPath.length > 0) {
				for(int i = 1; i < bestPath.length; i++){
					path.add(bestPath[i]);
				}
			}
		}
		
		if (path.size() > 0) {
			bfsMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path.remove(0),
					game.getPacmanLastMoveMade(), DM.PATH);
		}
		else {
			bfsMove = MOVE.NEUTRAL;
		}
		return bfsMove;
	}
	
	public int[] getPath(int s, Game game, int depth)
    {
		ArrayList<MazeNode> open = new ArrayList<MazeNode>();
		ArrayList<MazeNode> closed = new ArrayList<MazeNode>();
		MazeNode start = graph[s];
		start.gameState = game.copy();
		start.reachedBy = MOVE.NEUTRAL;
		open.add(start);
		MazeNode current = null;
		while (!open.isEmpty()) {
			
			current = open.remove(0);
			closed.add(current);
			System.out.println("next "+current.gameState.getScore());
			System.out.println("current "+game.getScore());
			if (current.gameState.getScore() > game.getScore()) {
				break;
			}
			
			for (MOVE move : current.neighbors.keySet()) {
				if(move != current.reachedBy.opposite()) {
					MazeNode child = current.neighbors.get(move);
					if (closed.contains(child)) {
						continue;
					}
					child.g = current.g + 1;
					if(child.g < depth){
						child.parent = current;
						child.reachedBy = move;
						Game gameState = current.gameState.copy();
						gameState.advanceGame(move, main.java.Main.ghosts.getMove(gameState, -1));
						if (!gameState.wasPacManEaten()) {
							child.gameState = gameState;
							open.add(child);
						}
					}
				}
			}
		}

        return util.getPath(current);
    }
	
}

