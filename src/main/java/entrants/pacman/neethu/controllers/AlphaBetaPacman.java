package main.java.entrants.pacman.neethu.controllers;
import java.util.EnumMap;
import java.util.HashMap;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AlphaBetaPacMan extends Controller<MOVE> {
	// MIN_GHOST_DISTANCE is used in the heuristic function to set minimum ghost distance (to run away from them) and
	// MIN_EDIBLE_GHOST_DISTANCE is the minimum distance within which if there is any edible ghost, the pacman would move towards it
	private static final int MIN_GHOST_DISTANCE = 15;
	private static final int MIN_EDIBLE_GHOST_DISTANCE = 100;
	private Controller<EnumMap<Constants.GHOST, MOVE>> ghost;
	// value used in minimum function
	double value ;
	// ghostMove is used to update game copy and stores the next set of moves for all the 4 ghosts
	EnumMap<GHOST, MOVE> ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);

	public AlphaBetaPacMan() {
		// TODO Auto-generated constructor stub
	}

	public MOVE getMove(Game game, long timeDue) {
		MOVE alphaBetaMove = MOVE.NEUTRAL;
		double bestScore = Double.NEGATIVE_INFINITY;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;

		// depth of the search
		int depth = 4;
		Game bestGame = game;
		double score = 0;
		// the ghost controller
		ghost = main.java.Main.SG;
		HashMap<Game,MOVE> gameMove = new HashMap<>();

		// 1st step in alpha beta pruning algorithm.
		// thsi is the max step for the pacman
		// consider all the available actions for the pacman( maximizing agent) 
		// pacman tries to maximize the score provided by the minimizing agents(all 4 ghosts)
		// and considers the move that maximizes its score
		// if the value provided by the minimizer is greater than the best already explored
		// option along path to the root for minimizer(beta value), then we ignore that value (pruning)
		// else update alpha as required
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
		alphaBetaMove = gameMove.get(bestGame);
		return alphaBetaMove;
	}

	// min function acts as minimizer for the pacman
	// This function takes as argument
	//         - the game passed by maximizer
	//         - depth value
	//         - alpha,beta
	//         - pacman move (used to update game state)
	//         - an integer s, which is used to keep track of the ghosts
	public double min(Game game, 
			int depth, double alpha, double beta,long timeDue,int s,MOVE pacmanMove) {
		// evaluation function is called if depth =0
		if (depth < 1) return evaluation(game);

		GHOST ghosts[] = GHOST.values();
		if(s==1) {
			value = Double.POSITIVE_INFINITY;
			ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);	
		}
		// consider the move for the ghost (s-1)
		if(s<=4) {
			GHOST currentGhost = ghosts[s-1];
			MOVE ghostMoves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(currentGhost), game.getGhostLastMoveMade(currentGhost));
			if(ghostMoves.length>0) {
				for(MOVE move: ghostMoves) {
					ghostMove.put(currentGhost,move);
					min(game,depth, alpha, beta,timeDue,s+1,pacmanMove);
				}
			}
			// this part of the code is executed if the ghost under consideration does not have any
			// possible moves left
			else {
				ghostMove.put(currentGhost, null);
				min(game,depth, alpha, beta,timeDue,s+1,pacmanMove);
			}
		}
		// this part of the code is executed when all the 4 ghosts next move is selected
		// A copy of the game is made and we advance the game , passing pacmanMove and ghostMove as parameter
		// then, we take the minimum value of all values provided by the maximizers.
		// pruning is applied f the value being considered is less than best already explored option 
		// along path to root for the maximizer
		else {
			s =1;
			Game myGame = game.copy();
			myGame.advanceGame(pacmanMove, ghostMove);
			value = Math.min(value,max(myGame, depth, alpha, beta, -1,pacmanMove,s));
			if (value <= alpha) {
				return value;
			} 
			beta = Math.min(beta,value);
		}
		return value;
	}

	// max function acts as maximizer for the pacman
	// This function takes as argument
	//         - the game copy passed by minimizer
	//         - depth value
	//         - alpha,beta
	//         - pacman move
	//         - an integer s, which is used to keep track of the ghosts
	public double max(Game game, int depth, double alpha, double beta,long timeDue,MOVE pacmanMove,int s) {
		// calls evaluation function once the depth is 0
		if (depth < 1) return evaluation(game);
		// initializes the value as -infinity
		double value = Double.NEGATIVE_INFINITY;
		// this function gets all the possible moves of pacman and take maximum value from the values
		// provided by the minimizer function.
		// pruning occurs if the value chosen is greater than beta. Else update alpha accordingly
		MOVE moves[] = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		for (MOVE move: moves) {
			value = Math.max(value,min(game, depth-1, alpha, beta,timeDue,1,move));
			if (value >= beta) {
				break;
			} 
			alpha = Math.max(alpha,value);
		}
		return value;
	}

	// evaluation function for a game state.
	public double evaluation ( Game myGame ){
		int ghostScore = 0;
		int shortestEdibleGhostDistance = Integer.MAX_VALUE, shortestGhostDistance = Integer.MAX_VALUE ;

		// gets the shortest ghost distance and shortest edible ghost distance
		for (GHOST ghost : GHOST.values()) {
			if (myGame.getGhostLairTime(ghost) > 0) continue;
			int distance = myGame.getShortestPathDistance(myGame.getPacmanCurrentNodeIndex(),
					myGame.getGhostCurrentNodeIndex(ghost));

			if (myGame.isGhostEdible(ghost)) {
				if (distance < shortestEdibleGhostDistance) {
					shortestEdibleGhostDistance = distance;
				}
			} else {
				if (distance < shortestGhostDistance) {
					shortestGhostDistance = distance;
				}
			}
		}

		// if the pacman is near the ghost
		if (shortestGhostDistance != Integer.MAX_VALUE && shortestGhostDistance != -1
				&& shortestGhostDistance < MIN_GHOST_DISTANCE) {
			ghostScore += shortestGhostDistance * 10000;

		} 
		// if the pacman is far away from the ghost
		else {

			ghostScore += MIN_GHOST_DISTANCE * 10000;
		}

		//Goes towards edible ghost 
		// increase the score if edible ghost is nearby
		if (shortestEdibleGhostDistance != Integer.MAX_VALUE && shortestEdibleGhostDistance != -1
				&& shortestEdibleGhostDistance < MIN_EDIBLE_GHOST_DISTANCE) {
			ghostScore += (MIN_EDIBLE_GHOST_DISTANCE - shortestEdibleGhostDistance) * 100;
		}

		//gets the distance to nearest pill
		int[] activePills = myGame.getActivePillsIndices();
		int[] activePowerPills = myGame.getActivePowerPillsIndices();
		int[] pillIndices = new int[activePills.length + activePowerPills.length];
		System.arraycopy(activePills, 0, pillIndices, 0, activePills.length);
		System.arraycopy(activePowerPills, 0, pillIndices, activePills.length, activePowerPills.length);

		int shortestPillDistance =  myGame.getShortestPathDistance(myGame.getPacmanCurrentNodeIndex(),
				myGame.getClosestNodeIndexFromNodeIndex(myGame.getPacmanCurrentNodeIndex(), pillIndices, DM.PATH));
		int pillScore = 200-shortestPillDistance;
		
		// final score calculated as ghost score + game score + pills score
		return ghostScore + myGame.getScore() * 1000 + pillScore;
	}
}

