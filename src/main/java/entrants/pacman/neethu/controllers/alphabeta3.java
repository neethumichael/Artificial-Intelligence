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

public class alphabeta3 extends Controller<MOVE> {
	//private Controller<EnumMap<Constants.GHOST, MOVE>> ghost;
	private static final boolean COMPLETE_LEVEL = true; 
	private static final int MIN_GHOST_DISTANCE = 20;
	private static final int MIN_EDIBLE_GHOST_DISTANCE = 100;
	private Controller<EnumMap<Constants.GHOST, MOVE>> ghost;

	public alphabeta3( Controller<EnumMap<Constants.GHOST, MOVE>> ghost ){
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
			//depth++;
			MOVE moves[] = game.getPossibleMoves( game.getPacmanCurrentNodeIndex() );
			for (MOVE move: moves) {

				Game myGame = game.copy();
				//Game gameCopy = game.copy();
				myGame.advanceGame(move, ghost.getMove(game,-1));
				if( !gameMove.containsKey(myGame) ){
					gameMove.put(myGame, move);
				}

				score = min(game, depth-1, alpha, beta,timeDue,move); 
				//if(move == game.getPacmanLastMoveMade().opposite())
				//	score -=10;

				if (score > bestScore) {
					bestScore = score;
					bestGame = myGame;
				}

				if (score < beta)
					alpha = Math.max(alpha, score);
				else
					break;  
				System.out.println("-----------------");
				System.out.println("score "+score+"move "+move);
			}



			System.out.println("--------------");

		}

		alphaBetaMove = gameMove.get(bestGame);
		System.out.println("selected move "+alphaBetaMove);
		return alphaBetaMove;
	}

	public double min(Game game, int depth, double alpha, double beta,long timeDue, MOVE pacmanMove) {
		//System.out.println("inside min");
		if (depth < 1) return fitness(game,pacmanMove);
		double value = Double.POSITIVE_INFINITY;
		MOVE moves[] = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		// double value = Double.MAX_VALUE;
		//  for (GHOST gh: GHOST.values()){
		//  GHOST gh[] = GHOST.values();

		GHOST Blinky = GHOST.BLINKY;
		MOVE Blinky_moves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(Blinky), game.getGhostLastMoveMade(Blinky));

		GHOST Inky = GHOST.INKY;
		MOVE Inky_moves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(Inky), game.getGhostLastMoveMade(Inky));

		GHOST Pinky = GHOST.PINKY;
		MOVE Pinky_moves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(Pinky), game.getGhostLastMoveMade(Pinky));

		GHOST Sue = GHOST.SUE;
		MOVE Sue_moves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(Sue), game.getGhostLastMoveMade(Sue));
		HashMap<GHOST, MOVE[]> allmoves = new HashMap<GHOST, MOVE[]>();
		allmoves.put(Blinky, Blinky_moves);
		allmoves.put(Inky, Inky_moves);
		allmoves.put(Pinky,Pinky_moves);
		allmoves.put(Sue, Sue_moves);

		//TreeMap<GHOST,Integer> ghost_move_length = new TreeMap<GHOST,Integer>();
		HashMap<GHOST, Integer> map = new HashMap<GHOST, Integer>();
		ValueComparator bvc = new ValueComparator(map);
		TreeMap<GHOST, Integer> sorted_map = new TreeMap<GHOST, Integer>(bvc);
		map.put(Blinky, Blinky_moves.length);
		map.put(Inky, Inky_moves.length);
		map.put(Pinky, Pinky_moves.length);
		map.put(Sue, Sue_moves.length);
		sorted_map.putAll(map);
		Game myGame = null;
		EnumMap<GHOST, MOVE> ghostMove = null;
		Iterator<Map.Entry<GHOST, Integer>> it = sorted_map.entrySet().iterator();
		//while (it.hasNext()) 
		//{}
		if(it.hasNext()) {
			Map.Entry<GHOST, Integer> pair = (Map.Entry<GHOST, Integer>)it.next();
			int val = (Integer) pair.getValue();
			if(val>0) {
				for(MOVE move: allmoves.get(pair.getKey())) {
					myGame = game.copy();
					ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);
					ghostMove.put(pair.getKey(), move);
					if(it.hasNext()) {
						pair = (Map.Entry<GHOST, Integer>)it.next();
						val = (Integer) pair.getValue();
						if(val>0) {
							for(MOVE move2: allmoves.get(pair.getKey())) {
								myGame = game.copy();
								ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);
								ghostMove.put(pair.getKey(), move2);
								if(it.hasNext()) {
									pair = (Map.Entry<GHOST, Integer>)it.next();
									val = (Integer) pair.getValue();
									if(val>0) {
										for(MOVE move3: allmoves.get(pair.getKey())) {
											myGame = game.copy();
											ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);
											ghostMove.put(pair.getKey(), move3);
											if(it.hasNext()) {
												pair = (Map.Entry<GHOST, Integer>)it.next();
												val = (Integer) pair.getValue();
												if(val>0) {
													for(MOVE move4: allmoves.get(pair.getKey())) {
														myGame = game.copy();
														ghostMove = new EnumMap<GHOST, MOVE>(GHOST.class);

														ghostMove.put(pair.getKey(), move4);
														//myGame.updateGhosts(ghostMove);
														System.out.println("ghost mcoe size "+ghostMove.size());
														myGame.advanceGame(pacmanMove, ghostMove);
														value = Math.min(value,max(myGame, depth, alpha, beta, -1,pacmanMove));
														if (value <= alpha) {
															return value;
														} 
														beta = Math.min(beta,value);

													}
												}
												else {
													myGame = game.copy();
													//myGame.updateGhosts(ghostMove);
													System.out.println("ghost mcoe size "+ghostMove.size());
													myGame.advanceGame(pacmanMove, ghostMove);
													value = Math.min(value,max(myGame, depth, alpha, beta, -1,pacmanMove));
													if (value <= alpha) {
														return value;
													} 
													beta = Math.min(beta,value);
												}
											}
										}
									}
									else {
										myGame = game.copy();
										//myGame.updateGhosts(ghostMove);
										System.out.println("ghost mcoe size "+ghostMove.size());
										myGame.advanceGame(pacmanMove, ghostMove);
										value = Math.min(value,max(myGame, depth, alpha, beta, -1,pacmanMove));
										if (value <= alpha) {
											return value;
										} 
										beta = Math.min(beta,value);
									}
								}
							}
						}
						//sdsadas
						else {
							myGame = game.copy();
							//myGame.updateGhosts(ghostMove);
							System.out.println("ghost mcoe size "+ghostMove.size());
							myGame.advanceGame(pacmanMove, ghostMove);
							value = Math.min(value,max(myGame, depth, alpha, beta, -1,pacmanMove));
							if (value <= alpha) {
								return value;
							} 
							beta = Math.min(beta,value);
						}
					}
				}
			}
		}
		return value;
	}

	public double max(Game game, int depth, double alpha, double beta,long timeDue,MOVE pacmanMove) {
		if (depth < 1) return fitness(game,pacmanMove);
		double value = Double.NEGATIVE_INFINITY;
		MOVE moves[] = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		for (MOVE move: moves) {

			Game myGame = game.copy();
			myGame.advanceGame(move, ghost.getMove(game,-1));
			value = Math.max(value,min(myGame, depth-1, alpha, beta,timeDue,move));
			if (value >= beta) {
				break;
			} 
			alpha = Math.max(alpha,value);
		}

		return value;
	}

	public double fitness ( Game myGame,MOVE pacmanMove ){

		/*double score = myGame.getScore();
        int current = myGame.getPacmanCurrentNodeIndex();
        if ( myGame.wasPacManEaten() ) {
            score = 0;
        }
        for(Constants.GHOST aGhost : Constants.GHOST.values()) {
            double x = myGame.getShortestPathDistance(current, myGame.getGhostCurrentNodeIndex(aGhost));

            if (x < 20) {
                score -= 300;
            }
        }
        for (GHOST ghost : GHOST.values()) {
            // If it is > 0 then it is visible so no more PO checks
            if (myGame.getGhostEdibleTime(ghost) > 0) {
                int distance = myGame.getShortestPathDistance(current, myGame.getGhostCurrentNodeIndex(ghost));

                if (distance < 20) {
                	score +=400;

                }
            }
        }

        Random generator = new Random();
        double factor = generator.nextDouble();
        return score ;
		 */
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
		//if (!COMPLETE_LEVEL) {
		if (shortestEdibleGhostDistance != Integer.MAX_VALUE && shortestEdibleGhostDistance != -1
				&& shortestEdibleGhostDistance < MIN_EDIBLE_GHOST_DISTANCE) {
			// multiplier needs to be high
			distanceFromGhost += (MIN_EDIBLE_GHOST_DISTANCE - shortestEdibleGhostDistance) * 130;
		}
		//	}
		//Keeps on updating with pill indices
		int[] activePillIndices = myGame.getActivePillsIndices();
		int[] activePowerPillIndices = myGame.getActivePowerPillsIndices();
		int[] pillIndices = new int[activePillIndices.length + activePowerPillIndices.length];
		System.arraycopy(activePillIndices, 0, pillIndices, 0, activePillIndices.length);
		System.arraycopy(activePowerPillIndices, 0, pillIndices, activePillIndices.length, activePowerPillIndices.length);

		int shortestPillDistance =  myGame.getShortestPathDistance(pacmanNode,
				myGame.getClosestNodeIndexFromNodeIndex(pacmanNode, pillIndices, DM.PATH));
		//if(gameState.getPacmanLastMoveMade().opposite()==gameState.)
		return distanceFromGhost + myGame.getScore() * 100 + myGame.getPacmanNumberOfLivesRemaining() * 10000000 + (200 - shortestPillDistance);


	}
	class ValueComparator implements Comparator<GHOST> {
		Map<GHOST, Integer> base;

		public ValueComparator(Map<GHOST, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		public int compare(GHOST a, GHOST b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}
}

