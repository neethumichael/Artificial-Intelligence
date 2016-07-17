package main.java.entrants.pacman.neethu;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.EnumMap;

import static pacman.game.Constants.*;

import java.util.EnumMap;
/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan_AStar extends PacmanController {
    private MOVE myMove = MOVE.NEUTRAL;
    public MyPacMan_AStar(){
        
   }
    
    public MOVE getMove(Game game, long timeDue) {
    	
        //Place your game logic here to play the game as Ms Pac-Man

       return myMove;
    }
}