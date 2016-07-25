package main.java.entrants.pacman.neethu.controllers;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import pacman.Executor;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class alphabeta4 extends Controller<MOVE> {

	private static final boolean COMPLETE_LEVEL = true; 
	private static final int MIN_GHOST_DISTANCE = 20;
	private static final int MIN_EDIBLE_GHOST_DISTANCE = 100;
	private Controller<EnumMap<Constants.GHOST, MOVE>> ghost;
	double value ;
	EnumMap<GHOST, MOVE> ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);

	public alphabeta4( Controller<EnumMap<Constants.GHOST, MOVE>> ghost ){
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

		while ( System.currentTimeMillis() < timeDue ) {
			MOVE moves[] = game.getPossibleMoves( game.getPacmanCurrentNodeIndex() );
			for (MOVE move: moves) {
				Game myGame = game.copy();
				if( !gameMove.containsKey(myGame) ){
					gameMove.put(myGame, move);
				}
				score = min(game, depth-1, alpha, beta,timeDue,1,move); 
				if (score > bestScore) {
					bestScore = score;
					bestGame = myGame;
				}

				if (score < beta)
					alpha = Math.max(alpha, score);
				else
					break;  
			}
		}
		alphaBetaMove = gameMove.get(bestGame);
		return alphaBetaMove;
	}

	public double min(Game game, 
			int depth, double alpha, double beta,long timeDue,int s,MOVE pacmanMove) {
		if (depth < 1) return fitness(game);

		GHOST ghosts[] = GHOST.values();
		if(s==1) {
			value = Double.POSITIVE_INFINITY;
			ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);	
		}
		if(s<=4) {
			GHOST currentGhost = ghosts[s-1];
			MOVE ghostMoves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(currentGhost), game.getGhostLastMoveMade(currentGhost));
			if(ghostMoves.length>0) {
				for(MOVE move: ghostMoves) {
					ghostMove.put(currentGhost,move);
					min(game,depth, alpha, beta,timeDue,s+1,pacmanMove);
				}
			}
			else {
				ghostMove.put(currentGhost, null);
				min(game,depth, alpha, beta,timeDue,s+1,pacmanMove);
			}
		}
		else {
			s =1;
			Game myGame = game.copy();
			myGame.advanceGame(pacmanMove, ghostMove);
			value = Math.min(value,max(myGame, depth, alpha, beta, -1,pacmanMove,s));
			System.out.println("value "+value);
			if (value <= alpha) {
				return value;
			} 
			beta = Math.min(beta,value);
		}
		return value;
	}

	public double max(Game game, int depth, double alpha, double beta,long timeDue,MOVE pacmanMove,int s) {
		if (depth < 1) return fitness(game);
		double value = Double.NEGATIVE_INFINITY;
		MOVE moves[] = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		for (MOVE move: moves) {
			value = Math.max(value,min(game, depth-1, alpha, beta,timeDue,1,move));
			if (value >= beta) {
				break;
			} 
			alpha = Math.max(alpha,value);
		}
		return value;
	}

	public double fitness ( Game myGame ){
		double score =0;
		int pacmanNode = myGame.getPacmanCurrentNodeIndex();
		int distanceFromGhost = 0;
		int shortestEdibleGhostDistance = Integer.MAX_VALUE, shortestGhostDistance = Integer.MAX_VALUE,secondShortestGhostDistance = Integer.MAX_VALUE ;

		for (GHOST ghost : GHOST.values()) {
			if (myGame.getGhostLairTime(ghost) > 0) continue;
			int distance = myGame.getShortestPathDistance(pacmanNode,
					myGame.getGhostCurrentNodeIndex(ghost));

			if (myGame.isGhostEdible(ghost)) {
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
		if (shortestEdibleGhostDistance != Integer.MAX_VALUE && shortestEdibleGhostDistance != -1
				&& shortestEdibleGhostDistance < MIN_EDIBLE_GHOST_DISTANCE) {
			// multiplier needs to be high
			distanceFromGhost += (MIN_EDIBLE_GHOST_DISTANCE - shortestEdibleGhostDistance) * 130;
		}

		//Keeps on updating with pill indices
		int[] activePillIndices = myGame.getActivePillsIndices();
		int[] activePowerPillIndices = myGame.getActivePowerPillsIndices();
		int[] pillIndices = new int[activePillIndices.length + activePowerPillIndices.length];
		System.arraycopy(activePillIndices, 0, pillIndices, 0, activePillIndices.length);
		System.arraycopy(activePowerPillIndices, 0, pillIndices, activePillIndices.length, activePowerPillIndices.length);

		int shortestPillDistance =  myGame.getShortestPathDistance(pacmanNode,
				myGame.getClosestNodeIndexFromNodeIndex(pacmanNode, pillIndices, DM.PATH));
		score = distanceFromGhost + myGame.getScore() * 100 + myGame.getPacmanNumberOfLivesRemaining() * 10000000 + (220 - shortestPillDistance);
		return score;
	}

}

