package main.java;

import java.util.EnumMap;

import main.java.entrants.pacman.neethu.controllers.AStarPacMan;
import main.java.entrants.pacman.neethu.controllers.AlphaBetaPacMan2;
import main.java.entrants.pacman.neethu.controllers.AlphaBetaPacman;
import main.java.entrants.pacman.neethu.controllers.BFSPacMan;
import main.java.entrants.pacman.neethu.controllers.alphabeta3;
import main.java.entrants.pacman.neethu.controllers.alphabeta4;
import main.java.examples.commGhosts.POCommGhosts;
import pacman.Executor;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import main.java.examples.poPacMan.POPacMan;


/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {
	public static POCommGhosts ghosts = null;
	public static RandomGhosts randomGhosts = null;
	public static StarterGhosts SG = null;

	public static void main(String[] args) {

		// intialization for Executor class.
		// Update 1st argument as true for PO Environment
		// Update 1st argument as false for fully observable environment
		Executor executor = new Executor(false, true); 
		
		// initializing POGhosts and randomGhosts
		//ghosts = new POCommGhosts(50);
		randomGhosts =  new RandomGhosts();
		SG = new StarterGhosts();
		// initializing BFS and AStar implementation objects
		BFSPacMan bfs = new BFSPacMan();
		AStarPacMan astar = new AStarPacMan(); 
		AlphaBetaPacman alphabeta = new AlphaBetaPacman(SG);
		AlphaBetaPacMan2 alphabeta2 = new AlphaBetaPacMan2(SG);
alphabeta3 alphabeta3 = new alphabeta3(SG);
alphabeta4 alphabeta4 = new alphabeta4(SG);
		

	


		// BFS ( uncomment the below call to runGameTimed to execute BFS search )
		//executor.runGameTimed(bfs, ghosts, true);
		// Astar ( uncomment the below call to runGameTimed to execute Astar search )
		//executor.runGameTimed(astar, ghosts, true);
		// AlphaBeta ( uncomment the below call to runGameTimed to execute Astar search )
		executor.runGameTimed(alphabeta4, SG, true);
		//EnumMap<GHOST, MOVE> moves = ghosts.getMove();
		
	}
}
