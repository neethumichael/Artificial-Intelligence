package main.java;

import main.java.entrants.pacman.neethu.controllers.AStarPacMan;
import main.java.entrants.pacman.neethu.controllers.BFSPacMan;
import main.java.examples.commGhosts.POCommGhosts;
import pacman.Executor;
import pacman.controllers.examples.RandomGhosts;
import main.java.examples.poPacMan.POPacMan;


/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {
	public static POCommGhosts ghosts = null;
	public static RandomGhosts randomGhosts = null;
	
    public static void main(String[] args) {
    	
    	// initializing BFS and AStar implementation objects
    	BFSPacMan bfs = new BFSPacMan();
    	AStarPacMan astar = new AStarPacMan(); 
    	
    	// initializing POGhosts and randomGhosts
    	ghosts = new POCommGhosts(50);
        randomGhosts =  new RandomGhosts();

    	// intialization for Executor class.
        // Update 1st argument as true for PO Environment
        // Update 1st argument as false for fully observable environment
        Executor executor = new Executor(false, true);
         
        // BFS ( uncomment the below call to runGameTimed to execute BFS search )
        executor.runGameTimed(bfs, ghosts, true);
        // Astar ( uncomment the below call to runGameTimed to execute Astar search )
        //executor.runGameTimed(astar, ghosts, true);
    }
}
