package main.java;
import main.java.entrants.pacman.neethu.controllers.AStarPacMan;
import main.java.entrants.pacman.neethu.controllers.BFSPacMan;
import main.java.entrants.pacman.neethu.controllers.AlphaBetaPacMan;
import main.java.examples.commGhosts.POCommGhosts;
import pacman.Executor;
import pacman.controllers.examples.RandomGhosts;
import pacman.controllers.examples.StarterGhosts;

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

		// initializing POGhosts ,randomGhosts and starterGhosts
		ghosts = new POCommGhosts(50);
		randomGhosts =  new RandomGhosts();
		SG = new StarterGhosts();
		
		// initializing BFS ,AStar, Alpha beta pruning implementation objects
		BFSPacMan bfs = new BFSPacMan();
		AStarPacMan astar = new AStarPacMan(); 
		AlphaBetaPacMan alphabeta = new AlphaBetaPacMan();

		// BFS ( uncomment the below call to runGameTimed to execute BFS search )
		//executor.runGameTimed(bfs, ghosts, true);
		// Astar 
		//executor.runGameTimed(astar, ghosts, true);
		// AlphaBeta 
		executor.runGameTimed(alphabeta, SG, true);
	}
}
