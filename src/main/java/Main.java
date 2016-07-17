package main.java;


import main.java.entrants.pacman.neethu.MyPacMan_BFS;
import main.java.examples.commGhosts.POCommGhosts;
import pacman.Executor;
import pacman.controllers.examples.RandomGhosts;
import main.java.examples.poPacMan.POPacMan;


/**
 * Created by pwillic on 06/05/2016.
 */
public class Main {
	public static POCommGhosts ghosts = null;
	public static RandomGhosts ghosts2 = null;

    public static void main(String[] args) {

        Executor executor = new Executor(true, true);
         ghosts = new POCommGhosts(50);
         ghosts2 =  new RandomGhosts();

        executor.runGameTimed(new MyPacMan_BFS(), ghosts2, true);
    }
}
