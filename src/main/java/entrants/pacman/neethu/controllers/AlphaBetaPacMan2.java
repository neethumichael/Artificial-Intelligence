package main.java.entrants.pacman.neethu.controllers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class AlphaBetaPacMan2 extends Controller<MOVE>{
	private Controller<EnumMap<Constants.GHOST, MOVE>> ghost;
	private static final boolean COMPLETE_LEVEL = true; 
	private static final int MIN_GHOST_DISTANCE = 20;
	private static final int MIN_EDIBLE_GHOST_DISTANCE = 100;

	public AlphaBetaPacMan2( Controller<EnumMap<Constants.GHOST, MOVE>> ghost ){
        this.ghost = ghost;
    }
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		MOVE alphaBetaMove = MOVE.NEUTRAL;
		double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double bestScore = Double.NEGATIVE_INFINITY;
        int depth = 30;
        int count =0;
        Game bestGame = game;
        double score = 0;
        ghost = main.java.Main.SG;
        HashMap<Game,MOVE> gameMove = new HashMap<>();
        
        MOVE moves[] = game.getPossibleMoves( game.getPacmanCurrentNodeIndex() );
        for (MOVE move: moves) {
        	if(move == game.getPacmanLastMoveMade().opposite()) {
        		continue;
        	}
        	Game myGame = game.copy();
        	System.out.println("Inital "+myGame.getScore());
        	//myGame.updatePacMan(move);
        	//myGame.updateGhosts(ghost.getMove(myGame, timeDue));
        	myGame.advanceGame(move, ghost.getMove(myGame, timeDue));
        	//System.out.println("current index "+myGame.getPacmanCurrentNodeIndex());
        	if( !gameMove.containsKey(myGame) ){
                gameMove.put(myGame, move);
            }
        	count =0;
        	score = max(game, depth-1, alpha, beta, timeDue,count);
        	System.out.println("score in "+move+" is "+score);
        	if (score > bestScore) {
            	
                bestScore = score;
                bestGame = myGame;

            }
        }
        System.out.println("-----------------");
        alphaBetaMove = gameMove.get(bestGame);
        return alphaBetaMove;
	}
	
	
	public double max(Game game, int depth, double alpha, double beta, long timeDue, int count) {
        if (depth <1)        	{
        	return fitness(game,count);
        	}
       // System.out.println("depth "+depth);	
        double value = Double.NEGATIVE_INFINITY;
        MOVE moves[] = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
        for (MOVE move: moves) {
            Game myGame = game.copy();
           // myGame.updatePacMan(move);
           // myGame.updateGhosts(ghost.getMove(myGame, timeDue));
            myGame.advanceGame(move, ghost.getMove(myGame, timeDue));
           
            value = Math.max(value,max(myGame, depth-1, alpha, beta, timeDue, count));
        }
        return value;
    }
    
	  public static double fitness(Game gameState,int count) {
		  	/*int pacmanNode = gameState.getPacmanCurrentNodeIndex();
			
			int distanceFromGhost = 0;
			
			int shortestEdibleGhostDistance = Integer.MAX_VALUE, shortestGhostDistance = Integer.MAX_VALUE,secondShortestGhostDistance = Integer.MAX_VALUE ;
			
			for (GHOST ghost : GHOST.values()) {

				if (gameState.getGhostLairTime(ghost) > 0) continue;
				
				int distance = gameState.getShortestPathDistance(pacmanNode,
						gameState.getGhostCurrentNodeIndex(ghost));
				
				if (gameState.isGhostEdible(ghost)) {
					if (distance < shortestEdibleGhostDistance) {
						shortestEdibleGhostDistance = distance;
					}
				} else {
					if (distance < shortestGhostDistance) {
						secondShortestGhostDistance = shortestGhostDistance;
						shortestGhostDistance = distance;
					}
				}
			}
			
			if (shortestGhostDistance != Integer.MAX_VALUE && shortestGhostDistance != -1
					&& shortestGhostDistance < MIN_GHOST_DISTANCE) {
				if (secondShortestGhostDistance != Integer.MAX_VALUE && secondShortestGhostDistance != -1
						&& secondShortestGhostDistance < MIN_GHOST_DISTANCE) {


					int avgGhostDistance = (shortestGhostDistance + secondShortestGhostDistance) / 2;
					distanceFromGhost += avgGhostDistance * 10000;
				} else {
					// increase heuristic the farther pacman is from the nearest ghost
					distanceFromGhost += shortestGhostDistance * 10000;
				}
			} else {

	                    // this prevents pacman from staying near MIN_GHOST_DISTANCE
				distanceFromGhost += (MIN_GHOST_DISTANCE + 10) * 10000;
			}
	                
	                //Goes towards edible ghost for points if COMPLETE_LEVEL is set False else it goes away
			if (!COMPLETE_LEVEL) {
				if (shortestEdibleGhostDistance != Integer.MAX_VALUE && shortestEdibleGhostDistance != -1
						&& shortestEdibleGhostDistance < MIN_EDIBLE_GHOST_DISTANCE) {
					// multiplier needs to be high
					distanceFromGhost += (MIN_EDIBLE_GHOST_DISTANCE - shortestEdibleGhostDistance) * 130;
				}
			}
			 //Keeps on updating with pill indices
			int[] activePillIndices = gameState.getActivePillsIndices();
			int[] activePowerPillIndices = gameState.getActivePowerPillsIndices();
			int[] pillIndices = new int[activePillIndices.length + activePowerPillIndices.length];
			System.arraycopy(activePillIndices, 0, pillIndices, 0, activePillIndices.length);
			System.arraycopy(activePowerPillIndices, 0, pillIndices, activePillIndices.length, activePowerPillIndices.length);
			
			int shortestPillDistance =  gameState.getShortestPathDistance(pacmanNode,
					gameState.getClosestNodeIndexFromNodeIndex(pacmanNode, pillIndices, DM.PATH));
			//if(gameState.getPacmanLastMoveMade().opposite()==gameState.)
			return distanceFromGhost + gameState.getScore() * 100 + gameState.getPacmanNumberOfLivesRemaining() * 10000000 + (200 - shortestPillDistance);
			*/	
		   // return gameState.getScore();
		  //int[] activePillIndices = gameState.getActivePillsIndices();
		return gameState.getScore();
}
}