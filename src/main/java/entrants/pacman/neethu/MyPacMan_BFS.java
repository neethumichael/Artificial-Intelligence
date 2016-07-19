package main.java.entrants.pacman.neethu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	HashSet<MOVE> movesTaken = new HashSet<MOVE>();
	MOVE bfsMove;
	private static Random random = new Random();

	@Override
	public MOVE getMove(Game game, long timeDue) {
		ArrayList<Integer> targets = null;
		
		int[] targetsArray = null;
		int[] bestPath = null;
		int dest = 0;
		// TODO Auto-generated method stub
		if(game.wasPacManEaten()){
			path = new ArrayList<Integer>();
		}
		MOVE lastMove = game.getPacmanLastMoveMade();
		movesTaken.add(lastMove);
		bfsMove = MOVE.NEUTRAL;
		 int step = game.getPacmanCurrentNodeIndex();
         stepsTaken.add(step);
        //********************************************************************************************
        // *************************** GHOST *********************************************************
     
         
         
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
	        
	        int activePills[] = game.getActivePillsIndices();
        	int activePowerPills[] = game.getActivePowerPillsIndices();
        	ArrayList<Integer> active = new ArrayList<Integer>();
        	for(int i=0;i<activePills.length;i++) {
        		if(!stepsTaken.contains(activePills[i]))
        		active.add(activePills[i]);
        	}
        	for(int i=0;i<activePowerPills.length;i++) {
        		if(!stepsTaken.contains(activePowerPills[i]))
        		active.add(activePowerPills[i]);
        	}
        	if(active.size()>0) {
        		int []activeArray = new int[active.size()];
        		for(int i=0;i<active.size();i++) {
        			activeArray[i] = active.get(i);
        		}
        		dest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), activeArray, DM.PATH);
	        	
        		//System.out.println("destination chosen is null"+dest);
        	
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
        	else {
	        if(targetsArray.length>0) {
	        	
	        	dest = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, DM.PATH);
	        	
	        		//System.out.println("destination chosen is null"+dest);
	        	
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
	        }
	        
	        if (path.size() > 0) {
				int tempPath[] = new int[path.size()];
	        	for(int i=0;i<path.size();i++) {
	        		tempPath[i] = path.get(i);
	        	}
	        	
	        	
				int x = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(),
						tempPath, DM.PATH);
				//System.out.println("before path size "+path.size());
				//System.out.println("chosen next location "+x);
				int index = path.indexOf(x);
				//System.out.println("xxx path size "+path.get(index));
				path.remove(index);
				
				
				bfsMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), x, game.getPacmanLastMoveMade(), DM.PATH);
						//game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(),x);
				
				
					
				
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
