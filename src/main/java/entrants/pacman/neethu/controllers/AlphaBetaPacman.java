package main.java.entrants.pacman.neethu.controllers;

import static pacman.game.Constants.DELAY;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Random;


import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AlphaBetaPacman extends Controller<MOVE> {
	
	private Controller<EnumMap<Constants.GHOST, MOVE>> ghost;
	private static final boolean COMPLETE_LEVEL = true; 
	private static final int MIN_GHOST_DISTANCE = 20;
	private static final int MIN_EDIBLE_GHOST_DISTANCE = 100;
	
	public AlphaBetaPacman( Controller<EnumMap<Constants.GHOST, MOVE>> ghost ){
        this.ghost = ghost;
    }

    public MOVE getMove(Game game, long timeDue) {
    	MOVE alphaBetaMove = MOVE.NEUTRAL;
    	double bestScore = Double.NEGATIVE_INFINITY;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        
        int depth = 4;
        Game bestGame = game;
        double score = 0;
        
       ghost = main.java.Main.SG;
        
       
        
        HashMap<Game,MOVE> gameMove = new HashMap<>();

        //while ( System.currentTimeMillis() < timeDue ) {
                MOVE moves[] = game.getPossibleMoves( game.getPacmanCurrentNodeIndex() );
                //System.out.println("possible move "+moves.length);
                if(moves.length>2) {
                	for(int i=0;i<moves.length;i++) {
                	//	System.out.print(" "+moves[i]);
                	}
                }
                //System.out.println();
                for (MOVE move: moves) {
                	//System.out.println("next move "+move);
                	if(move == game.getPacmanLastMoveMade().opposite()) {
                		continue;
                	}
                    Game myGame = game.copy();

                    myGame.updatePacMan(move);
                   // myGame.updateGhosts(ghost.getMove(game,-1));
                    myGame.advanceGame(move, ghost.getMove(game,-1));

                    if( !gameMove.containsKey(myGame) ){
                        gameMove.put(myGame, move);
                    }
                    score = max(game, depth-1, alpha, beta, timeDue); 
                    if(moves.length>2) {                   
                   System.out.println(score+" "+move);
                                        }
                    if (score > bestScore) {
                    	
                        bestScore = score;
                        bestGame = myGame;
 
                    }
                    if (score < beta)
                        alpha = Math.max(alpha, score);
                    else
                        break;    
                }
                //System.out.println("------");
       // }
       // System.out.println("------------");
        
        alphaBetaMove = gameMove.get(bestGame);
        return alphaBetaMove;
    }

    public double min(Game game, int depth, double alpha, double beta, long timeDue) {
    
        if (depth < 1) 
        	{
        	//System.out.println("fitness min");
        	return fitness(game);
        	}
      //  double value[] = new double[4];
      //  for(int i=0;i<value.length;i++) {
     //   	value[i] = Double.MAX_VALUE;
     //   }
        int j=0;
      
        double value = Double.MAX_VALUE;
      //  for (GHOST gh: GHOST.values()){
      //  GHOST gh[] = GHOST.values();
        
        GHOST Blinky = GHOST.BLINKY;
        MOVE Blinky_moves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(Blinky), game.getGhostLastMoveMade(Blinky));
        
        GHOST Inky = GHOST.INKY;
        MOVE Inky_moves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(Inky), game.getGhostLastMoveMade(Inky));
        
        if(Blinky_moves.length>=Inky_moves.length && Blinky_moves.length>0) {
        	for(MOVE move: Blinky_moves) {
        		Game myGame = game.copy();
        		EnumMap<GHOST, MOVE> ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);
        		ghostMove.put(Blinky, move);
        		if(Inky_moves.length>0) {
        		for(MOVE inkymove: Inky_moves) {
        		//	System.out.println("here1");
        			ghostMove.put(Inky, inkymove);
        			myGame.updateGhosts(ghostMove);
					value = Math.min(value,max(myGame, depth-1, alpha, beta, timeDue));
					//System.out.println("depth "+depth);
		            if (value <= alpha) {
		                return value;
		            } 
		                beta = Math.min(beta,value);
        			
        		}
        		}
        		else {
        			//System.out.println("here2");
        			myGame.updateGhosts(ghostMove);
					value = Math.min(value,max(myGame, depth-1, alpha, beta, timeDue));
		            if (value <= alpha) {
		                return value;
		            } 
		                beta = Math.min(beta,value);
        			
        		}
        	}
        }
        
       // MOVE moves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(gh[0]), game.getGhostLastMoveMade(gh[0]));
        //for (MOVE move: moves) {
        //	EnumMap<GHOST, MOVE> ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);
          //  Game myGame = game.copy();
           /* ghostMove.put(gh[0], move);
           // System.out.println("ghost move "+ghost.getMove(game, timeDue));
            if(move!=game.getGhostLastMoveMade(gh[0])) {
            		MOVE moves2[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(gh[1]), game.getGhostLastMoveMade(gh[1]));
            		
            		for(MOVE move2: moves2) {
            			if(move2!=game.getGhostLastMoveMade(gh[1])) {
            					MOVE moves3[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(gh[2]), game.getGhostLastMoveMade(gh[2]));
            					for(MOVE move3: moves3) {
            						if(move3!=game.getGhostLastMoveMade(gh[2])) {
            							ghostMove.put(gh[2], move3);
            								MOVE moves4[] =game.getPossibleMoves(game.getGhostCurrentNodeIndex(gh[3]), game.getGhostLastMoveMade(gh[3]));
            								for(MOVE move4: moves4) {
            									if(move4!=game.getGhostLastMoveMade(gh[3])) {           									           											            											
            											ghostMove.put(gh[3], move4);
            											myGame.updateGhosts(ghostMove);
            											value = Math.min(value,max(myGame, depth-1, alpha, beta, timeDue));
            								            if (value <= alpha) {
            								                return value;
            								            } 
            								                beta = Math.min(beta,value);
            										}
            									}
            								}
            							}
            						}
            					}
            }*/

           // myGame.advanceGame(move, ghost.getMove(game,-1));
             
            
       // }
        
        
        return value;
    }

    public double max(Game game, int depth, double alpha, double beta, long timeDue) {
        if (depth < 1) 
        	{
        	//System.out.println("fitness max");
        	return fitness(game);
        	}
        	
        double value = Double.NEGATIVE_INFINITY;
        MOVE moves[] = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
        for (MOVE move: moves) {
            Game myGame = game.copy();
    //myGame.updatePacMan(move);
            myGame.updatePacMan(move);
           // myGame.updateGhosts(ghost.getMove(game,-1));
           // myGame.advanceGame(move, ghost.getMove(game,-1));
            value = Math.max(value,min(myGame, depth-1, alpha, beta, timeDue));
            if (value >= beta) {
            	//System.out.println("returned value");
                return value;
            } 
                alpha = Math.max(alpha,value);
            
        }
        //System.out.println("oyt value");
        return value;
    }
    
    public static int fitness(Game gameState) {
	/*	int pacmanNode = gameState.getPacmanCurrentNodeIndex();
		
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
    	return gameState.getScore();
  
    }
		
}
