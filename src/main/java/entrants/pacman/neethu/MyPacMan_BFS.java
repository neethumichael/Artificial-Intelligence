package main.java.entrants.pacman.neethu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MyPacMan_BFS extends Controller<MOVE>{
	ArrayList<Integer> path = new ArrayList<Integer>();
	ArrayList<Integer> stepsTaken = new ArrayList<Integer>();
	private static Random random = new Random();

	@Override
	public MOVE getMove(Game game, long timeDue) {
		ArrayList<Integer> targets = null;
		MOVE bfsMove = MOVE.NEUTRAL;
		int[] targetsArray = null;
		int[] bestPath = null;
		int dest = 0;
		// TODO Auto-generated method stub
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
         // following code adds all the pills and power pills and do bfs on nearest pill/power pill

        // check if the path is empty
        if(path.isEmpty()) {
        	// get all the pills in the maze
        	 targets = new ArrayList<Integer>();
    		int[] pills = game.getCurrentMaze().pillIndices;
            int[] powerPills = game.getCurrentMaze().powerPillIndices;
    		           
            //add pills to target array (pills which are not eaten)
            
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

	        if(targetsArray.length>0) {
	        	dest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, DM.PATH);
	        	GameView.addLines(game,Color.MAGENTA,game.getPacmanCurrentNodeIndex(),dest);

	        	bestPath = getPath(game.getPacmanCurrentNodeIndex(), game,dest);

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
				int x = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),
						tempPath, DM.PATH);
				int index = path.indexOf(x);
				path.remove(index);
				
				bfsMove = game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(),x);
	        }
	        return bfsMove;
	        }
	
	public int[] getPath(int s, Game game,int d) {
		int depth = 10000;
		int calc_depth = 0;
		Queue<Integer> q = new LinkedList<Integer>();
		Queue<Integer> visited = new LinkedList<Integer>();
		int current = s;
		q.add(current);
		int temp =-80;
		while(!q.isEmpty()){
			//System.out.println("working on bfs");
			 temp = q.peek();
			visited.add(temp);
			q.poll();
			
			
			if (temp==d) {
				break;
			}
			int neighbours[] = game.getNeighbouringNodes(temp);
			for (int i=0;i<neighbours.length;i++) {
				if (visited.contains(neighbours[i])) {
					continue;
				}
				//System.out.print(" "+neighbours[i]);
				q.add(neighbours[i]);
			}
			calc_depth ++;
			if(calc_depth == depth) {
				break;
			}
			
		}
		int i = 0;
		int[] paths = new int[visited.size()];;
			while(!visited.isEmpty()) {
				paths[i] = visited.peek();
				visited.poll();
				i++;
				}
		return paths;
	}
}
