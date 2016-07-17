package main.java.entrants.pacman.neethu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import pacman.game.*;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


// Consists of Two Methods 
// 1) EvaluateGameState() : 
// 2) GetBestMove() :

public class Evaluation {
    
    // if true Pac-Man goes for level completion than high score
	private static final boolean COMPLETE_LEVEL = true; 
	private static final int MIN_GHOST_DISTANCE = 20;
	private static final int MIN_EDIBLE_GHOST_DISTANCE = 100;
	private static final int MIN_DISTANCE = 20;   
	private static Random random = new Random();
        //Defines the depth of the tree created. Should be balanced since deeper trees increases the execution time and 
        // shallow trees reduces the best probility of the best move really being the best move
	public static final int DEPTH = 6;  
  
	public static int evaluateGameState(Game gameState) {
		int pacmanNode = gameState.getPacmanCurrentNodeIndex();
		
		int distanceFromGhost = 0;
		
		int shortestEdibleGhostDistance = Integer.MAX_VALUE, shortestGhostDistance = Integer.MAX_VALUE,secondShortestGhostDistance = Integer.MAX_VALUE ;
		
		for (GHOST ghost : GHOST.values()) {

			if (gameState.getGhostLairTime(ghost) > 0) continue;
			
			int distance = gameState.getShortestPathDistance(pacmanNode,
					gameState.getGhostCurrentNodeIndex(ghost));
			
			if (gameState.getGhostEdibleTime(ghost)==0) {
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
		
		return distanceFromGhost + gameState.getScore() * 100 + gameState.getPacmanNumberOfLivesRemaining() * 10000000 + (200 - shortestPillDistance);
	}
	
        
	public static MOVE getBestMove(int leftValue, int rightValue, int upValue, int downValue) {
		
		MOVE bestMove = MOVE.NEUTRAL;
		int bestValue = Integer.MIN_VALUE;
		if (leftValue != Integer.MIN_VALUE && leftValue > bestValue) {
			bestMove = MOVE.LEFT;
			bestValue = leftValue;
		}
		if (rightValue != Integer.MIN_VALUE && rightValue > bestValue) {
			bestMove = MOVE.RIGHT;
			bestValue = rightValue;
		}
		if (upValue != Integer.MIN_VALUE && upValue > bestValue) {
			bestMove = MOVE.UP;
			bestValue = upValue;
		}
		if (downValue != Integer.MIN_VALUE && downValue > bestValue) {
			bestMove = MOVE.DOWN;
			bestValue = downValue;
		}
		
		return bestMove;
	}
	
/*	public static  MOVE evaluateGameState(Game game) {
	    // Should always be possible as we are PacMan
        int current = game.getPacmanCurrentNodeIndex();

        // Strategy 1: Adjusted for PO
        for (GHOST ghost : GHOST.values()) {
            // If can't see these will be -1 so all fine there
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                if (ghostLocation != -1) {
                    if (game.getShortestPathDistance(current, ghostLocation) < MIN_DISTANCE) {
                    	GameView.addLines(game,Color.MAGENTA,current,ghostLocation);
                    	// GameView can be used to debug
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
            return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minGhost), DM.PATH);
        }

        // Strategy 3: Go after the pills and power pills that we can see
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();

        ArrayList<Integer> targets = new ArrayList<Integer>();

        for (int i = 0; i < pills.length; i++) {
            //check which pills are available
        	System.out.println("pills[i]"+pills[i]);
            Boolean pillStillAvailable = game.isPillStillAvailable(pills[i]);
            //System.out.println(pillStillAvailable);
            if (pillStillAvailable == null) continue;
            if (game.isPillStillAvailable(pills[i])) {
                targets.add(pills[i]);
                
            }
        }

        for (int i = 0; i < powerPills.length; i++) {            //check with power pills are available
            Boolean pillStillAvailable = game.isPillStillAvailable(powerPills[i]);
            if (pillStillAvailable == null) continue;
            if (game.isPowerPillStillAvailable(powerPills[i])) {
                targets.add(powerPills[i]);
            }
        }

        if (!targets.isEmpty()) {
            int[] targetsArray = new int[targets.size()];        //convert from ArrayList to array

            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }
            //return the next direction once the closest target has been identified
            System.out.println("moving towards target");
            GameView.addLines(game,Color.MAGENTA,current,game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.PATH));
            return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.PATH), DM.PATH);
        }
        else
        {
        	System.out.println("target is empty");
        }


        // Strategy 4: New PO strategy as now S3 can fail if nothing you can see
        // Going to pick a random action here
        MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
        if (moves.length > 0) {
        	System.out.println("Random action");
            return moves[random.nextInt(moves.length)];
        }
        // Must be possible to turn around
        System.out.println("last move opposite");
        return game.getPacmanLastMoveMade().opposite();
    }
	
	
        
	/*public static MOVE getBestMove(int leftValue, int rightValue, int upValue, int downValue) {
		
		MOVE bestMove = MOVE.NEUTRAL;
		int bestValue = Integer.MIN_VALUE;
		if (leftValue != Integer.MIN_VALUE && leftValue > bestValue) {
			bestMove = MOVE.LEFT;
			bestValue = leftValue;
		}
		if (rightValue != Integer.MIN_VALUE && rightValue > bestValue) {
			bestMove = MOVE.RIGHT;
			bestValue = rightValue;
		}
		if (upValue != Integer.MIN_VALUE && upValue > bestValue) {
			bestMove = MOVE.UP;
			bestValue = upValue;
		}
		if (downValue != Integer.MIN_VALUE && downValue > bestValue) {
			bestMove = MOVE.DOWN;
			bestValue = downValue;
		}
		
		return bestMove;
	}*/
}